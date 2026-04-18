package project.game.pokemon.effects;

import java.util.function.Function;

import project.game.battle.BattleLog;
import project.game.battle.Weather.WeatherEffect;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.MoveInterruptedException;
import project.game.exceptions.PokemonFaintedException;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Type;

public interface AbilityManager {

    public enum AbilityID {
        All_Or_Nothing(AbilityManager::allOrNothing),
        Blaze(AbilityManager::blaze),
        Chlorophyll(AbilityManager::chlorophyll),
        Overgrow(AbilityManager::overgrow),
        Rain_Dish(AbilityManager::rainDish),
        Simple(AbilityManager::simple),
        Solar_Power(AbilityManager::solarPower),
        Torrent(AbilityManager::torrent),
        Water_Absorb(AbilityManager::waterAbsorb);

        private final Function<Pokemon, Ability> func;

        AbilityID(Function<Pokemon, Ability> func) {
            this.func = func;
        }    

        public Ability apply(Pokemon p) {
            return this.func.apply(p);
        }

    }

    /**
     * Pokemon move accuracy is decreased by 30% but all attack move
     * land a critical hit.
     * 
     */
    public static Ability allOrNothing(Pokemon p) {
        String name = AbilityID.All_Or_Nothing.name();
        EventID[] flags = new EventID[] {EventID.ATK_MOVE_ACCURACY, EventID.ATK_DAMAGE_MULTIPLIER};

        // Reduces move accuracy by 30%. Moves with perfect accuracy reduce to 70
        p.getEvents().addEventListener(flags[0], name, e -> {
            Move m = e.moveUsed;
            if (m.alwaysHit()) {
                m.setAccuracy(70);
            }
            else {
                m.changeAccuracyByPercent(70);
            }
        });

        // Always lands a critical hit
        p.getEvents().addEventListener(flags[1], name, e -> {
            e.criticalHit = true;
        });

        return new Ability(p, name, flags);
    }

    // Increases Fire-Type attacks by 50% while under 1/3 Max HP
    public static Ability blaze(Pokemon p) {
        String name = AbilityID.Blaze.name();
        EventID[] flags = new EventID[] {EventID.ATK_DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name , e -> {
            Move move = e.moveUsed;
            if (move.isType(Type.Fire) && p.isHpLessThanPercent(33)) {
                e.otherMoveMods *= 1.5;
                BattleLog.add("Blaze increased the power of %s's Fire-Type attack!", p);
            } 
        });

        return new Ability(p, name, flags);
    }

    // Doubles speed in the sun
    public static Ability chlorophyll(Pokemon p) {
        String name = AbilityID.Chlorophyll.name();
        EventID[] flags = new EventID[] {EventID.FIND_MOVE_ORDER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (e.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
                p.getSpeed().setMod(200);
            }
        });

        return new Ability(p, name, flags);
    }

    // Increases Grass-Type attacks by 50% while under 1/3 Max HP
    public static Ability overgrow(Pokemon p) {
        String name = AbilityID.Overgrow.name();
        EventID[] flags = new EventID[] {EventID.ATK_DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            Move move = e.moveUsed;
            if (move.isType(Type.Grass) && p.isHpLessThanPercent(33)) {
                e.otherMoveMods *= 1.5;
                BattleLog.add("Overgrow increased the power of %s's Grass-Type attack!", p);
            }         
        });

        return new Ability(p, name, flags);
    }

    // Recovers 1/16 of its maximum HP during rain, after each turn.
    public static Ability rainDish(Pokemon p) {
        String name = AbilityID.Rain_Dish.name();
        EventID[] flags = new EventID[] {EventID.WEATHER_EFFECT};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (e.battleData.isCurrentWeather(WeatherEffect.Rain)) {
                p.restoreHpPercentMaxHP(6.25, " from its Rain Dish");
            }
        });

        return new Ability(p, name, flags);
    }

    /**
     * Doubles the number of stages of all stat changes. 
     */
    public static Ability simple(Pokemon p) {
        String name = AbilityID.Simple.name();
        EventID[] flags = new EventID[] {EventID.DEF_STAT_CHANGE};

        p.getEvents().addEventListener(flags[0], name, e -> {
            for (int i = 0; i < e.statChanges.length; i++) {
                e.statChanges[i] *= 2;
            }
        });

        return new Ability(p, name, flags);
    }

    /*
     * During harsh sunlight, raises Special Attack by 50%, 
     * but it also loses 1/8 of its maximum HP after each turn.
     */
    public static Ability solarPower(Pokemon p) {
        String name = AbilityID.Solar_Power.name();
        EventID[] flags = new EventID[] {EventID.ATK_DAMAGE_MULTIPLIER, EventID.WEATHER_EFFECT};

        p.getEvents().addEventListener(flags[0], name, e -> {
            Move m = e.moveUsed;
            if (m.getCategory().equals(Move.MoveCategory.Special)) {
                p.getSpecialAttack().setMod(150);
            }
        });

        p.getEvents().addEventListener(flags[1], name, e -> {
            if (e.battleData.isCurrentWeather(WeatherEffect.Sunny)  && !p.getConditions().isFainted()) {
                p.takeDamagePercentMaxHP(12.5, " from its Solar Power");
                if (p.getConditions().isFainted()) throw new PokemonFaintedException();
            }
        });

        return new Ability(p, name, flags);
    }

    // Increases Water-Type attacks by 50% while under 1/3 Max HP
    public static Ability torrent(Pokemon p) {
        String name = AbilityID.Torrent.name();
        EventID[] flags = new EventID[] {EventID.ATK_DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            Move move = e.moveUsed;
            if (move.isType(Type.Water) && p.isHpLessThanPercent(33)) {
                e.otherMoveMods *= 1.5; 
                BattleLog.add("Torrent increased the power of %s's Water-Type attack!", p);
            }        
        });

        return new Ability(p, name, flags);
    }

    // Water moves deal no effect and instead heal the Pokemon
    public static Ability waterAbsorb(Pokemon p) {
        String name = AbilityID.Water_Absorb.name();
        EventID[] flags = new EventID[] {EventID.DEF_MOVE_EFFECTIVENESS};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (e.moveUsed.isType(Type.Water)) {
                e.moveEffectiveness = 0;
                BattleLog.add("%s's Water Absorb soaked up the water!", p);
                p.restoreHpPercentMaxHP(25, "");

                throw new MoveInterruptedException();
            }
        });

        return new Ability(p, name, flags);
    }
}
