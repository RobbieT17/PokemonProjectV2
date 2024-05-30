package battle;

import stats.Counter;

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
    private static String weatherReport(int i) {
        return switch (i) {
            case CLEAR -> weatherStopped(BattleField.currentWeather);
            
            case SUNNY -> "The sunlight light grew harsh.";
            
            case RAIN -> "It began to rain!";
            
            default -> throw new IllegalArgumentException("Invalid weather id");
        };
    }

    // Methods
    public static String weatherStopped(int i) {
        return switch (i) {
            case Weather.SUNNY -> "The harsh sunlight faded!";
            case Weather.RAIN -> "It stopped raining!";
            default -> "";
        };
    }

    public static void change(int change) {
        BattleField.currentWeather = change;

        if (change == CLEAR) BattleField.weatherCount = null;
        else BattleField.weatherCount = new Counter(5);

        BattleLog.add(weatherReport(change));
    }

}
