package project.battle;

import project.exceptions.PokemonFaintedException;
import project.pokemon.Pokemon;
import project.stats.Type;
import project.utility.Counter;

public interface Weather {

// Weather Names
    public static final String NO_WEATHER = "No Weather";
    public static final String SUNNY_NAME = "Sunny";
    public static final String RAIN_NAME = "Raining";
    public static final String SANDSTORM_NAME = "Sandstorm";
    public static final String HAIL_NAME = "Hailing";

 // Weather IDs
    public static final int CLEAR = 0;
    public static final int SUNNY = 1;
    public static final int RAIN = 2;
    public static final int SANDSTORM = 3;
    public static final int HAIL = 4;

// Private Method
    /**
     * Called when a new weather effect happens
     * @param i Weather ID
     * @return String to display the change in weather
     */
    private static String weatherReport(int i) {
        return switch (i) {
            case Weather.CLEAR -> weatherStopped(BattleField.currentWeather);        
            case Weather.SUNNY -> "The sunlight light grew harsh.";    
            case Weather.RAIN -> "It began to rain!"; 
            case Weather.SANDSTORM -> "A sandstorm kicked up!";
            case Weather.HAIL -> "It began to hail!"; 
            default -> throw new IllegalArgumentException("Invalid weather id");
        };
    }

// Methods
    /**
     * Message depends on the current weather
     * on the BattleField
     * @param i Weather ID
     * @return String that display the weather stopped
     */
    public static String weatherStopped(int i) {
        return switch (i) {
            case Weather.SUNNY -> "The harsh sunlight faded...";
            case Weather.RAIN -> "It stopped raining!";
            case Weather.SANDSTORM -> "The sandstorm has calmed...";
            case Weather.HAIL -> "The hail stopped!"; 
            default -> "";
        };
    }

    /**
     * Changes the current weather on the battlefield
     * Each weather condition last for five turns
     * @param change Weather ID to change to
     */
    public static void change(int change) {
        BattleLog.add(weatherReport(change));
        BattleField.currentWeather = change;

        if (change == Weather.CLEAR) BattleField.weatherCount = null; // Clear weather last indefinitely
        else BattleField.weatherCount = new Counter(5);
    }

    // Pokemon takes damage from Sandstorm / Hail Weather
    public static void weatherEffect(Pokemon p) {
        switch (BattleField.currentWeather) {
            case Weather.SANDSTORM ->  {
                // Digital, Ground, Rock, and Steel types are immune to sandstorm
                if (p.isType(Type.DIGITAL) || 
                    p.isType(Type.GROUND) || 
                    p.isType(Type.ROCK) || 
                    p.isType(Type.STEEL)) return;

                int damage = (int) (p.getHp().getMaxHealthPoints() / 8.0);
                BattleLog.add("%s took %d damage from the sandstorm!", p, damage);
                p.takeDamage(damage);
            }
            case Weather.HAIL ->  {
                // Digital and Ice types are immune to hail
                if (p.isType(Type.DIGITAL) || p.isType(Type.ICE)) return;

                int damage = (int) (p.getHp().getMaxHealthPoints() / 16.0);  
                BattleLog.add("%s took %d damage from the hail!", p, damage);
                p.takeDamage(damage);
            }
        }
        if (p.getConditions().isFainted()) throw new PokemonFaintedException();
    }

}
