package battle;

public interface Weather {

    // Weather Names
    public static final String NO_WEATHER = "No Weather";
    public static final String SUNNY_NAME = "Sunny";
    public static final String RAIN_NAME = "Raining";

    // Weather IDs
    public static final int CLEAR = 0;
    public static final int SUNNY = 1;
    public static final int RAIN = 2;

    // Private Methods
    private static String weatherReport(int i) {
        switch (i) {
            case CLEAR:
                return "The skys have cleared.";
            case SUNNY:
                return "The sunlight light grew harsh.";
            case RAIN:
                return "It began to rain!";
            default:
                throw new IllegalArgumentException("Invalid weather id");
        }
    }

    public static void changeWeather(int change) {
        BattleField.currentWeather = change;
        BattleLog.add(weatherReport(change));
    }

}
