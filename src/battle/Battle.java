package battle;

import player.*;
import pokemon.Pokemon;
import pokemon.PokemonList;

public class Battle {
    public static void moveSelection(PokemonTrainer pt1, PokemonTrainer pt2) {
        Pokemon p1 = pt1.pokemonInBattle();
        Pokemon p2 = pt2.pokemonInBattle();

        try {
            p1.chooseMove(pt1);
            p2.chooseMove(pt2);

            p1.useMove(p1.moveSelected(), p2);
            p2.useMove(p2.moveSelected(), p1);
        } catch (PokemonFaintedException e) {
            BattleLog.add(e.getMessage());
        } 

        BattleField.endOfRound(p1, p2);
        BattleLog.out();
    }

    public static void main(String[] args) {

        PokemonTrainer player1 = new PokemonTrainerBuilder()
        .setName("Robbie")
        .addPokemon(PokemonList.bulbasaur("Bobby"))
        .addPokemon(PokemonList.charmander("Charlie"))
        .addPokemon(PokemonList.squirtle("Squirt"))
        .buildTrainer();

        PokemonTrainer player2 = new PokemonTrainerBuilder()
        .setName("Sammi")
        .addPokemon(PokemonList.bulbasaur("Bub"))
        .addPokemon(PokemonList.charmander("Chandler"))
        .addPokemon(PokemonList.squirtle("Tim"))
        .buildTrainer();

        player1.choosePokemon();
        player2.choosePokemon();

        BattleLog.out();

        while (!player1.outOfPokemon() && !player2.outOfPokemon()) {
            while (!player1.pokemonInBattle().fainted() && !player2.pokemonInBattle().fainted()) 
                moveSelection(player1, player2);
            
            if (player1.pokemonInBattle().fainted()) player1.choosePokemon();
            if (player2.pokemonInBattle().fainted()) player2.choosePokemon();
        }
    }

}
