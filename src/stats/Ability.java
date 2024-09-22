package stats;

import battle.*;
import event.EventData;
import event.GameEvent;
import event.Observer;
import exceptions.MoveInterruptedException;
import move.Move;
import pokemon.Pokemon;

public interface Ability extends Observer {

    public static final String BLAZE_ID = "Blaze";
    public static final String CHLOROPHYLL_ID = "Chlorophyll";
    public static final String OVERGROW_ID = "Overgrow"; 
    public static final String TORRENT_ID = "Torrent";
    public static final String WATER_ABSORB_ID = "Water Absorb";

    private static boolean isUser(Pokemon p, EventData e) {
        return p == e.user();
    }

    private static boolean isTarget(Pokemon p, EventData e) {
        return p == e.target();
    }

    // Increases Fire-Type attacks by 50% while under 1/3 Max HP
    public static String blaze(Pokemon p) {
        GameEvent.onDamageMultiplier.addListener(e -> {
            if (!isUser(p, e)) return;

            Move move = e.moveUsed();
            if (move.isType(Type.FIRE) && p.hpLessThanPercent(33)) {
                move.changePowerByPercent(50);
                BattleLog.add("%s's Blaze increased the power of its Fire-Type attack!", p);
            } 
        });

        return Ability.BLAZE_ID;
    }

    // Doubles speed in the sun
    public static String chlorophyll(Pokemon p) {
        GameEvent.onMoveOrder.addListener(e -> {
            if (BattleField.currentWeather == Weather.SUNNY) {
                p.modifySpeedByPercent(200);
            }
        });

        return Ability.CHLOROPHYLL_ID;
    }

    // Increases Grass-Type attacks by 50% while under 1/3 Max HP
    public static String overgrow(Pokemon p) {
        GameEvent.onDamageMultiplier.addListener(e -> {
            if (!isUser(p, e)) return;

            Move move = e.moveUsed();
            if (move.isType(Type.GRASS) && p.hpLessThanPercent(33)) {
                move.changePowerByPercent(50);
                BattleLog.add("%s's Overgrow increased the power of its Grass-Type attack!", p);
            }         
        });

        return Ability.OVERGROW_ID;
    }

    // Increases Water-Type attacks by 50% while under 1/3 Max HP
    public static String torrent(Pokemon p) {
        GameEvent.onDamageMultiplier.addListener(e -> {
            if (!isUser(p, e)) return;

            Move move = e.moveUsed();
            if (move.isType(Type.WATER) && p.hpLessThanPercent(33)) {
                move.changePowerByPercent(50);
                BattleLog.add("%s's Overgrow increased the power of its Water-Type attack!", p);
            }        
        });

        return Ability.TORRENT_ID;
    }

    public static String waterAbsorb(Pokemon p) {
        GameEvent.onMoveEffectiveness.addListener(e -> {
            if (!isTarget(p, e)) return;

            if (e.moveUsed().isType(Type.WATER)) {
                throw new MoveInterruptedException("%s's Water Absorb soaked up the water!", p);
            }
        });

        return Ability.WATER_ABSORB_ID;
    }
}
