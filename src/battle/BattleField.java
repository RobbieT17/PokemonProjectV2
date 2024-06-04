package battle;

import pokemon.Pokemon;
import stats.Counter;

public class BattleField {

// Variables
    public static int currentWeather = Weather.CLEAR; 
    public static Counter weatherCount = null;

// Methods
    // Increments weather counter
    private static void weatherUpdate(){
        if (weatherCount != null){
            weatherCount.inc();
            // Clears weather when counter finishes
            if (weatherCount.terminated()) Weather.change(Weather.CLEAR);
        }
    }


    // Called at the end of each round
    public static void endOfRound(Pokemon p1, Pokemon p2) {
        BattleLog.addLine();
        weatherUpdate();
        p1.afterEffects();
        p2.afterEffects();
    }

}
