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
        Blaze(AbilityManager::blaze),
        Chlorophyll(AbilityManager::chlorophyll),
        Overgrow(AbilityManager::overgrow),
        Rain_Dish(AbilityManager::rainDish),
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

    // Increases Fire-Type attacks by 50% while under 1/3 Max HP
    public static Ability blaze(Pokemon p) {
        String name = AbilityID.Blaze.name();
        EventID[] flags = new EventID[] {EventID.ATK_DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name , e -> {
            Move move = e.moveUsed;
            if (move.isType(Type.Fire) && p.hpLessThanPercent(33)) {
                e.otherMoveMods *= 1.5;
                BattleLog.add("%s's Blaze increased the power of its Fire-Type attack!", p);
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
            if (move.isType(Type.Grass) && p.hpLessThanPercent(33)) {
                e.otherMoveMods *= 1.5;
                BattleLog.add("%s's Overgrow increased the power of its Grass-Type attack!", p);
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
                p.takeDamagePercentMaxHP(1.0 / 8.0, " from its Solar Power");
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
            if (move.isType(Type.Water) && p.hpLessThanPercent(33)) {
                e.otherMoveMods *= 1.5; 
                BattleLog.add("%s's Overgrow increased the power of its Water-Type attack!", p);
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
                throw new MoveInterruptedException("%s's Water Absorb soaked up the water!", p);
            }
        });

        return new Ability(p, name, flags);
    }
}
