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

    // Resets some Pokemon attributes then applies after conditions
    private static void pokemonAfterEffects(Pokemon p) {
        try {
            p.setSwitchedIn(false);
            p.setHasMoved(false);
            p.resetDamageDealt();
            p.checkConditions(false);
        } catch (PokemonFaintedException e) {
            BattleLog.add(e.getMessage());
        }
      
    }

    // Called at the end of each round
    public static void endOfRound(Pokemon p1, Pokemon p2) {
        BattleLog.addLine();
        weatherUpdate();
        pokemonAfterEffects(p1);
        pokemonAfterEffects(p2);
    }

}
