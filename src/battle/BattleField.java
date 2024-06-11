package battle;

import exceptions.PokemonFaintedException;
import pokemon.Pokemon;
import utility.Counter;

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

    
    private static void afterEffects(Pokemon p) {
        try {
            p.conditions().setSwitchedIn(false);
            p.conditions().setHasMoved(false);
            p.conditions().setFlinched(false);
            p.resetDamageDealt();
            p.checkConditions(false);
        } catch (PokemonFaintedException e) {
        }
    }


    // Called at the end of each round
    public static void endOfRound(Pokemon p1, Pokemon p2) {
        BattleLog.addLine();
        weatherUpdate();
        afterEffects(p1);
        afterEffects(p2);
        Battle.skipRound = false;
    }

}
