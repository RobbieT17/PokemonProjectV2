package battle;

import pokemon.Pokemon;
import stats.Counter;

public class BattleField {

    // Variables
    public static int currentWeather = Weather.CLEAR;
    public static Counter weatherCount = null;

    // Methods
    private static void weatherUpdate(){
        if (weatherCount != null){
            weatherCount.inc();
            if (weatherCount.terminated()) Weather.change(Weather.CLEAR);
        }
    }

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

    public static void endOfRound(Pokemon p1, Pokemon p2) {
        BattleLog.addLine();
        weatherUpdate();
        pokemonAfterEffects(p1);
        pokemonAfterEffects(p2);
    }

}
