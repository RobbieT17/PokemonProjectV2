package battle;


import event.GameEvent;
import exceptions.BattleEndedException;
import player.PokemonTrainer;
import pokemon.Pokemon;
import utility.Counter;

public class BattleField {

// Variables
    public static Pokemon pokemon1; // Refers to Player 1's current Pokemon in battle
    public static Pokemon pokemon2; // Refers to Player 2's current Pokemon in battle

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
        onEvent(GameEvent.WEATHER_EFFECT);
        if (BattleField.weatherCount != null){
            weatherCount.inc();
            // Clears weather when counter finishes
            if (weatherCount.terminated()) Weather.change(Weather.CLEAR);
        }
    }

    // Called at the end of each round
    public static void endOfRound(PokemonTrainer pt1, PokemonTrainer pt2) {
        BattleLog.addLine();
        onEvent(GameEvent.END_OF_ROUND);
        isBattleOver(pt1, pt2);
        weatherUpdate();
        pt1.pokemonInBattle().afterEffects();
        pt2.pokemonInBattle().afterEffects();
        isBattleOver(pt1, pt2);
        Battle.skipRound = false;
    }

    public static void onEvent(String eventName){
        pokemon1.events().onEvent(eventName, null);
        pokemon2.events().onEvent(eventName, null);
    }

}
