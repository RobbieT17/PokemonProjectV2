package stats;

import battle.*;
import event.EventData;
import event.GameEvent;
import exceptions.MoveInterruptedException;
import move.Move;
import pokemon.Pokemon;

public class Ability {

// Public Class Variables
    public static final String BLAZE_ID = "Blaze";
    public static final String CHLOROPHYLL_ID = "Chlorophyll";
    public static final String OVERGROW_ID = "Overgrow";
    public static final String RAIN_DISH_ID = "Rain Dish"; 
    public static final String SOLAR_POWER_ID = "Solar Power";
    public static final String TORRENT_ID = "Torrent";
    public static final String WATER_ABSORB_ID = "Water Absorb";

// Object
    private final String abilityName;
    private final Pokemon abilityBearer; // Pokemon with this ability

    public Ability(Pokemon p, String name) {
        this.abilityName = name;
        this.abilityBearer = p;
    }

    public String abilityName() {return this.abilityName;}
    public Pokemon abilityBearer() {return this.abilityBearer;}

// Public Class Methods
    private static boolean isUser(Pokemon p, EventData e) {
        return p == e.user();
    }

    private static boolean isTarget(Pokemon p, EventData e) {
        return p == e.target();
    }

    // Increases Fire-Type attacks by 50% while under 1/3 Max HP
    public static Ability blaze(Pokemon p) {
        p.events().addEventSubscriber(GameEvent.DAMAGE_MULTIPLIER, e -> {
            if (!isUser(p, e)) return;

            Move move = e.moveUsed();
            if (move.isType(Type.FIRE) && p.hpLessThanPercent(33)) {
                move.changePowerByPercent(50);
                BattleLog.add("%s's Blaze increased the power of its Fire-Type attack!", p);
            } 
        });

        return new Ability(p, Ability.BLAZE_ID);
    }

    // Doubles speed in the sun
    public static Ability chlorophyll(Pokemon p) {
        p.events().addEventSubscriber(GameEvent.FIND_MOVE_ORDER, e -> {
            if (BattleField.currentWeather == Weather.SUNNY) {
                p.modifySpeedByPercent(200);
            }
        });

        return new Ability(p, Ability.CHLOROPHYLL_ID);
    }

    // Increases Grass-Type attacks by 50% while under 1/3 Max HP
    public static Ability overgrow(Pokemon p) {
        p.events().addEventSubscriber(GameEvent.DAMAGE_MULTIPLIER, e -> {
            if (!isUser(p, e)) return;

            Move move = e.moveUsed();
            if (move.isType(Type.GRASS) && p.hpLessThanPercent(33)) {
                move.changePowerByPercent(50);
                BattleLog.add("%s's Overgrow increased the power of its Grass-Type attack!", p);
            }         
        });

        return new Ability(p, Ability.OVERGROW_ID);
    }

    // Recovers 1/16 of its maximum HP during rain, after each turn.
    public static Ability rainDish(Pokemon p) {
        p.events().addEventSubscriber(GameEvent.WEATHER_EFFECT, e -> {
            if (BattleField.currentWeather == Weather.RAIN) {
                int heal = (int) (p.hp().max() / 16.0);
                p.healDamage(heal);
                BattleLog.add("%s restore %d HP from its Rain Dish!", p, heal);
            }
        });

        return new Ability(p, Ability.RAIN_DISH_ID);
    }

    /*
     * During harsh sunlight, raises Special Attack by 50%, 
     * but it also loses 1/8 of its maximum HP after each turn.
     */
    public static Ability solarPower(Pokemon p) {
        p.events().addEventSubscriber(GameEvent.DAMAGE_MULTIPLIER, e -> {
            if (!isUser(p, e)) return;

            Move m = e.moveUsed();
            if (m.category().equals(Move.SPECIAL)) {
                m.changePowerByPercent(50);
            }
        });

        p.events().addEventSubscriber(GameEvent.WEATHER_EFFECT, e -> {
            if (BattleField.currentWeather == Weather.SUNNY && !p.conditions().fainted()) {
                int damage = (int) (p.hp().max() / 8.0);
                p.takeDamage(damage);
                BattleLog.add("%s's Solar Power caused the sun to drain %d HP from it!", p, damage);
            }
        });

        return new Ability(p, Ability.SOLAR_POWER_ID);
    }

    // Increases Water-Type attacks by 50% while under 1/3 Max HP
    public static Ability torrent(Pokemon p) {
        p.events().addEventSubscriber(GameEvent.DAMAGE_MULTIPLIER, e -> {
            if (!isUser(p, e)) return;

            Move move = e.moveUsed();
            if (move.isType(Type.WATER) && p.hpLessThanPercent(33)) {
                move.changePowerByPercent(50);
                BattleLog.add("%s's Overgrow increased the power of its Water-Type attack!", p);
            }        
        });

        return new Ability(p, Ability.TORRENT_ID);
    }

    public static Ability waterAbsorb(Pokemon p) {
        p.events().addEventSubscriber(GameEvent.MOVE_EFFECTIVENESS, e -> {
            if (!isTarget(p, e)) return;

            if (e.moveUsed().isType(Type.WATER)) {
                throw new MoveInterruptedException("%s's Water Absorb soaked up the water!", p);
            }
        });

        return new Ability(p, Ability.WATER_ABSORB_ID);
    }
}
