package battle;

import utility.Counter;

public interface Weather {

// Weather Names
    public static final String NO_WEATHER = "No Weather";
    public static final String SUNNY_NAME = "Sunny";
    public static final String RAIN_NAME = "Raining";

 // Weather IDs
    public static final int CLEAR = 0;
    public static final int SUNNY = 1;
    public static final int RAIN = 2;

// Private Method
    /**
     * Called when a new weather effect happens
     * @param i Weather ID
     * @return String to display the change in weather
     */
    private static String weatherReport(int i) {
        return switch (i) {
            case CLEAR -> weatherStopped(BattleField.currentWeather);        
            case SUNNY -> "The sunlight light grew harsh.";    
            case RAIN -> "It began to rain!";  
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
            case Weather.SUNNY -> "The harsh sunlight faded!";
            case Weather.RAIN -> "It stopped raining!";
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

        if (change == CLEAR) BattleField.weatherCount = null; // Clear weather last indefinitely
        else BattleField.weatherCount = new Counter(5);
    }

}
