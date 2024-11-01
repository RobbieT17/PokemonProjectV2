package project.stats;

import project.event.EventData;
import project.event.GameEvent;
import project.battle.*;
import project.exceptions.MoveInterruptedException;
import project.exceptions.PokemonFaintedException;
import project.move.Move;
import project.pokemon.Pokemon;

public class Ability extends Effect {

// Public Class Variables
    public static final String BLAZE_ID = "Blaze";
    public static final String CHLOROPHYLL_ID = "Chlorophyll";
    public static final String OVERGROW_ID = "Overgrow";
    public static final String RAIN_DISH_ID = "Rain Dish"; 
    public static final String SOLAR_POWER_ID = "Solar Power";
    public static final String TORRENT_ID = "Torrent";
    public static final String WATER_ABSORB_ID = "Water Absorb";

// Object   
    public Ability(Pokemon p, String name, String[] flags) {
        super(p, name, flags);
    }

    @Override
    public void removeEffect() {
        this.bearer().events().removeEventListener(this.flags(), this.effectName());
        this.bearer().setAbility(null);
    }

// Public Class Methods
    // Increases Fire-Type attacks by 50% while under 1/3 Max HP
    public static Ability blaze(Pokemon p) {
        String name = Ability.BLAZE_ID;
        String[] flags = new String[] {GameEvent.DAMAGE_MULTIPLIER};

        p.events().addEventListener(flags[0], name , e -> {
            if (!EventData.isUser(p, e)) return;

            Move move = e.moveUsed;
            if (move.isType(Type.FIRE) && p.hpLessThanPercent(33)) {
                e.otherMoveMods *= 1.5;
                BattleLog.add("%s's Blaze increased the power of its Fire-Type attack!", p);
            } 
        });

        return new Ability(p, name, flags);
    }

    // Doubles speed in the sun
    public static Ability chlorophyll(Pokemon p) {
        String name = Ability.CHLOROPHYLL_ID;
        String[] flags = new String[] {GameEvent.FIND_MOVE_ORDER};

        p.events().addEventListener(flags[0], name, e -> {
            if (BattleField.currentWeather == Weather.SUNNY) {
                p.speed().setMod(200);
            }
        });

        return new Ability(p, name, flags);
    }

    // Increases Grass-Type attacks by 50% while under 1/3 Max HP
    public static Ability overgrow(Pokemon p) {
        String name = Ability.OVERGROW_ID;
        String[] flags = new String[] {GameEvent.DAMAGE_MULTIPLIER};

        p.events().addEventListener(flags[0], name, e -> {
            if (!EventData.isUser(p, e)) return;

            Move move = e.moveUsed;
            if (move.isType(Type.GRASS) && p.hpLessThanPercent(33)) {
                e.otherMoveMods *= 1.5;
                BattleLog.add("%s's Overgrow increased the power of its Grass-Type attack!", p);
            }         
        });

        return new Ability(p, name, flags);
    }

    // Recovers 1/16 of its maximum HP during rain, after each turn.
    public static Ability rainDish(Pokemon p) {
        String name = Ability.RAIN_DISH_ID;
        String[] flags = new String[] {GameEvent.WEATHER_EFFECT};

        p.events().addEventListener(flags[0], name, e -> {
            if (BattleField.currentWeather == Weather.RAIN) {
                p.restoreHpPercentMaxHP(1.0 / 16.0, " from its Rain Dish");
            }
        });

        return new Ability(p, name, flags);
    }

    /*
     * During harsh sunlight, raises Special Attack by 50%, 
     * but it also loses 1/8 of its maximum HP after each turn.
     */
    public static Ability solarPower(Pokemon p) {
        String name = Ability.SOLAR_POWER_ID;
        String[] flags = new String[] {GameEvent.DAMAGE_MULTIPLIER, GameEvent.WEATHER_EFFECT};

        p.events().addEventListener(flags[0], name, e -> {
            if (!EventData.isUser(p, e)) return;

            Move m = e.moveUsed;
            if (m.category().equals(Move.SPECIAL)) {
                p.specialAttack().setMod(150);
            }
        });

        p.events().addEventListener(flags[1], name, e -> {
            if (BattleField.currentWeather == Weather.SUNNY && !p.conditions().fainted()) {
                p.takeDamagePercentMaxHP(1.0 / 8.0, " from its Solar Power");
                if (p.conditions().fainted()) throw new PokemonFaintedException();
            }
        });

        return new Ability(p, name, flags);
    }

    // Increases Water-Type attacks by 50% while under 1/3 Max HP
    public static Ability torrent(Pokemon p) {
        String name = Ability.TORRENT_ID;
        String[] flags = new String[] {GameEvent.DAMAGE_MULTIPLIER};

        p.events().addEventListener(flags[0], name, e -> {
            if (!EventData.isUser(p, e)) return;

            Move move = e.moveUsed;
            if (move.isType(Type.WATER) && p.hpLessThanPercent(33)) {
                e.otherMoveMods *= 1.5; 
                BattleLog.add("%s's Overgrow increased the power of its Water-Type attack!", p);
            }        
        });

        return new Ability(p, name, flags);
    }

    // Water moves deal no effect and instead heal the Pokemon
    public static Ability waterAbsorb(Pokemon p) {
        String name = Ability.WATER_ABSORB_ID;
        String[] flags = new String[] {GameEvent.MOVE_EFFECTIVENESS};

        p.events().addEventListener(flags[0], name, e -> {
            if (!EventData.isTarget(p, e)) return;

            if (e.moveUsed.isType(Type.WATER)) {
                e.moveEffectiveness = 0;
                throw new MoveInterruptedException("%s's Water Absorb soaked up the water!", p);
            }
        });

        return new Ability(p, name, flags);
    }
}
