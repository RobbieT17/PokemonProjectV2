package project.game.pokemon.effects;

import java.util.function.Function;

import project.game.battle.BattleField;
import project.game.battle.BattleLog;
import project.game.battle.Weather;
import project.game.event.EventData;
import project.game.event.GameEvents;
import project.game.exceptions.MoveInterruptedException;
import project.game.exceptions.PokemonFaintedException;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Type;

public interface AbilityManager {

    public enum AbilityIDs {
        BLAZE_ID(AbilityManager::blaze),
        CHLOROPHYLL_ID(AbilityManager::chlorophyll),
        OVERGROW_ID(AbilityManager::overgrow),
        RAIN_DISH_ID(AbilityManager::rainDish),
        SOLAR_POWER_ID(AbilityManager::solarPower),
        TORRENT_ID(AbilityManager::torrent),
        WATER_ABSORB_ID(AbilityManager::waterAbsorb);

        private final Function<Pokemon, Ability> func;

        AbilityIDs(Function<Pokemon, Ability> func) {
            this.func = func;
        }    

        public Ability apply(Pokemon p) {
            return this.func.apply(p);
        }

    }

    // Increases Fire-Type attacks by 50% while under 1/3 Max HP
    public static Ability blaze(Pokemon p) {
        String name = AbilityIDs.BLAZE_ID.name();
        String[] flags = new String[] {GameEvents.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name , e -> {
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
        String name = AbilityIDs.CHLOROPHYLL_ID.name();
        String[] flags = new String[] {GameEvents.FIND_MOVE_ORDER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (BattleField.currentWeather == Weather.SUNNY) {
                p.getSpeed().setMod(200);
            }
        });

        return new Ability(p, name, flags);
    }

    // Increases Grass-Type attacks by 50% while under 1/3 Max HP
    public static Ability overgrow(Pokemon p) {
        String name = AbilityIDs.OVERGROW_ID.name();
        String[] flags = new String[] {GameEvents.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
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
        String name = AbilityIDs.RAIN_DISH_ID.name();
        String[] flags = new String[] {GameEvents.WEATHER_EFFECT};

        p.getEvents().addEventListener(flags[0], name, e -> {
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
        String name = AbilityIDs.SOLAR_POWER_ID.name();
        String[] flags = new String[] {GameEvents.DAMAGE_MULTIPLIER, GameEvents.WEATHER_EFFECT};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!EventData.isUser(p, e)) return;

            Move m = e.moveUsed;
            if (m.getCategory().equals(Move.SPECIAL)) {
                p.getSpecialAttack().setMod(150);
            }
        });

        p.getEvents().addEventListener(flags[1], name, e -> {
            if (BattleField.currentWeather == Weather.SUNNY && !p.getConditions().isFainted()) {
                p.takeDamagePercentMaxHP(1.0 / 8.0, " from its Solar Power");
                if (p.getConditions().isFainted()) throw new PokemonFaintedException();
            }
        });

        return new Ability(p, name, flags);
    }

    // Increases Water-Type attacks by 50% while under 1/3 Max HP
    public static Ability torrent(Pokemon p) {
        String name = AbilityIDs.TORRENT_ID.name();
        String[] flags = new String[] {GameEvents.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
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
        String name = AbilityIDs.WATER_ABSORB_ID.name();
        String[] flags = new String[] {GameEvents.MOVE_EFFECTIVENESS};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!EventData.isTarget(p, e)) return;

            if (e.moveUsed.isType(Type.WATER)) {
                e.moveEffectiveness = 0;
                throw new MoveInterruptedException("%s's Water Absorb soaked up the water!", p);
            }
        });

        return new Ability(p, name, flags);
    }
}
