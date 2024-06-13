package battle;

import exceptions.BattleEndedException;
import exceptions.PokemonFaintedException;
import player.PokemonTrainer;
import pokemon.Pokemon;
import utility.Counter;

public class BattleField {

// Variables
    public static int currentWeather = Weather.CLEAR; 
    public static Counter weatherCount = null;

// Methods
    // Battle ends when a trainer has no more Pokemon
    private static void isBattleOver(PokemonTrainer pt1, PokemonTrainer pt2) {
        if (pt1.outOfPokemon() && pt2.outOfPokemon()) 
            throw new BattleEndedException("Both trainers are out of Pokemon! It's a tie!");

        if (pt1.outOfPokemon()) throw new BattleEndedException(pt2, pt1);
        if (pt2.outOfPokemon()) throw new BattleEndedException(pt1, pt2);
    }


    // Increments weather counter
    private static void weatherUpdate(){
        if (weatherCount != null){
            weatherCount.inc();
            // Clears weather when counter finishes
            if (weatherCount.terminated()) Weather.change(Weather.CLEAR);
        }
    }

    
    private static void afterEffects(Pokemon p) {
        p.conditions().setSwitchedIn(false);
        if (Battle.skipRound || p.conditions().fainted()) return;
            
        p.resetMove();
        p.resetDamageDealt();
        p.conditions().clearAtEndRound();

        try {
            p.weatherEffect();
            p.checkConditions(false);
        } catch (PokemonFaintedException e) {
        }   
    }


    // Called at the end of each round
    public static void endOfRound(PokemonTrainer pt1, PokemonTrainer pt2) {
        BattleLog.addLine();
        isBattleOver(pt1, pt2);
        weatherUpdate();
        afterEffects(pt1.pokemonInBattle());
        afterEffects(pt2.pokemonInBattle());
        isBattleOver(pt1, pt2);
        Battle.skipRound = false;
    }

}
