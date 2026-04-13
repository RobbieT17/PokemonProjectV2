package project.game.processors;

import project.game.battle.BattleData;
import project.game.battle.BattleLog;
import project.game.battle.Weather;
import project.game.battle.Weather.WeatherEffect;
import project.game.exceptions.PokemonFaintedException;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Type;

public class WeatherProcessor {

    private final BattleData battleData;

    public WeatherProcessor(BattleData data) {
        this.battleData = data;
    }

    /**
     * Message depends on the current weather
     * on the BattleField
     * @return String that display the weather stopped
     */
    private String weatherStopped(WeatherEffect e) {
        return switch (e) {
            case WeatherEffect.Sunny -> "The harsh sunlight faded...";
            case WeatherEffect.Rain -> "It stopped raining!";
            case WeatherEffect.Sandstorm -> "The sandstorm has calmed...";
            case WeatherEffect.Hail -> "The hail stopped!"; 
            default -> "";
        };
    }

    /**
     * Called when a new weather effect happens
     * @return String to display the change in weather
     */
    private String weatherReport(WeatherEffect e) {
        return switch (e) {
            case WeatherEffect.Clear -> this.weatherStopped(e);        
            case WeatherEffect.Sunny -> "The sunlight light grew harsh.";    
            case WeatherEffect.Rain -> "It began to rain!"; 
            case WeatherEffect.Sandstorm -> "A sandstorm kicked up!";
            case WeatherEffect.Hail -> "It began to hail!"; 
            default -> throw new IllegalArgumentException("Invalid weather id");
        };
    }
    
    /**
     * Changes the current weather on the battlefield
     * Each weather condition last for five turns
     * @param change Weather ID to change to
     */
    public void changeWeather(WeatherEffect change) {
        this.battleData.setCurrenWeather(new Weather(change));
        BattleLog.add(this.weatherReport(change));
    }

    // Pokemon takes damage from Sandstorm / Hail Weather
    public void pokemonTakeWeatherDamage(Pokemon p) {        
        switch (this.battleData.getCurrenWeather().getWeatherEffect()) {
            case WeatherEffect.Sandstorm ->  {
                // Digital, Ground, Rock, and Steel types are immune to sandstorm
                if (p.isType(Type.Digital) || 
                    p.isType(Type.Ground) || 
                    p.isType(Type.Rock) || 
                    p.isType(Type.Steel)) return;

                int damage = (int) (p.getHp().getMaxHealthPoints() / 8.0);
                BattleLog.add("%s took %d damage from the sandstorm!", p, damage);
                p.takeDamage(damage);
            }
            case WeatherEffect.Hail ->  {
                // Digital and Ice types are immune to hail
                if (p.isType(Type.Digital) || p.isType(Type.Ice)) return;

                int damage = (int) (p.getHp().getMaxHealthPoints() / 16.0);  
                BattleLog.add("%s took %d damage from the hail!", p, damage);
                p.takeDamage(damage);
            }
            default -> throw new IllegalArgumentException("Unexpected weather value");
        }
        if (p.getConditions().isFainted()) throw new PokemonFaintedException();
    }
}
