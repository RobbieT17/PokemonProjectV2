package project.battle;


import project.exceptions.BattleEndedException;
import project.player.PokemonTrainer;
import project.utility.Counter;

public class BattleField {

// Variables
    public static PokemonTrainer player1;
    public static PokemonTrainer player2;

    public static int currentWeather = Weather.CLEAR; 
    public static Counter weatherCount = null;

// Methods
    // Battle ends when a trainer has no more Pokemon
    private static void isBattleOver() {
        if (player1.outOfPokemon() && player2.outOfPokemon()) 
            throw new BattleEndedException("Both trainers are out of Pokemon! It's a tie!");

        if (player1.outOfPokemon()) throw new BattleEndedException(player2, player1);
        if (player2.outOfPokemon()) throw new BattleEndedException(player1, player2);
    }


    // Increments weather counter
    private static void weatherUpdate(){
        if (BattleField.weatherCount != null){
            // Clears weather when counter finishes
            if (weatherCount.inc()) Weather.change(Weather.CLEAR);
        }
    }

    // Called at the end of each round
    public static void endOfRound() {
        BattleLog.addLine();
        isBattleOver();
        weatherUpdate();
        player1.getPokemonInBattle().afterEffects();
        player2.getPokemonInBattle().afterEffects();
        isBattleOver();
        BattleA.skipRound = false;
    }
}
