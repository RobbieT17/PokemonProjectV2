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
            if (weatherCount.terminated()){
                Weather.change(Weather.CLEAR);
                BattleLog.add(Weather.weatherStopped(currentWeather));
            }
        }
    }

    private static void pokemonAfterEffects(Pokemon p) {
        try {
            p.applyEffects(false);
            p.resetDamageDealt();
        } catch (PokemonFaintedException e) {
            BattleLog.add(e.getMessage());
        }
      
    }

    public static void endOfRound(Pokemon p1, Pokemon p2) {
        weatherUpdate();
        pokemonAfterEffects(p1);
        pokemonAfterEffects(p2);
    }

}
