package battle;

import java.util.InputMismatchException;
import java.util.Scanner;
import move.Move;
import move.MoveList;
import player.*;
import pokemon.Pokemon;
import pokemon.PokemonList;

public class Battle {

    public static boolean validPokemonChoice(PokemonTrainer pt, Pokemon p) {
        return !p.fainted() && (pt.pokemonInBattle() != null) ? !pt.pokemonInBattle().equals(p) : true;
    }

    public static void choosePokemon(PokemonTrainer trainer) {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        Pokemon pokemon = trainer.pokemonInBattle();

        BattleLog.addPrintln(trainer.showPokemon());

        while (!done) {
            try {
                BattleLog.addPrint(String.format("%s, choose a Pokemon: ", trainer));
                int i = scanner.nextInt();

                pokemon = trainer.team()[i];

                // Pokemon selected cannot be fainted or the same one that is already out
                done = validPokemonChoice(trainer, pokemon);
                
            } catch (IndexOutOfBoundsException e) {
                BattleLog.addPrintln("Invalid input try again");
            } catch (InputMismatchException e) {
                BattleLog.addPrintln("Invalid input try again");
                scanner.next();
            }

        }

        trainer.returns();
        trainer.sendOut(pokemon);
    }


    public static void chooseMove(PokemonTrainer pt) {
        Pokemon p = pt.pokemonInBattle();

        if (p.charged() || p.switchedIn()) return;

        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        Move move = p.moveSelected();

        if (p.hasNoMoves()) {
            BattleLog.add(String.format("%s has no moves!", p));
            p.setMove(MoveList.struggle());
        }

        BattleLog.addPrintln("\n" + p.showAllStats());
        BattleLog.addPrintln("[S]: Switch Pokemon");

        while (!done) {
            try {
                BattleLog.addPrint(String.format("What should %s do? ", p)); 
                String input = scanner.nextLine();

                if (Input.isChar(input, 's')){ 
                    choosePokemon(pt);
                    return;
                }

                if (Input.isNumeric(input)) {
                    move = p.moves()[Integer.parseInt(input)];
                    done = !move.pp().depleted();
                }
            
            } catch (IndexOutOfBoundsException e) {
                BattleLog.addPrintln("Invalid input try again");
            } catch (InputMismatchException e) {
                BattleLog.addPrintln("Invalid input try again");
                scanner.next();
            }

        }
        p.setMove(move);
    }

    public static void pokemonTurn(PokemonTrainer a, PokemonTrainer b) {
        Pokemon attacker = a.pokemonInBattle();
        Pokemon defender = b.pokemonInBattle();

        if (attacker.moveSelected() == null) return;
        attacker.useTurn(attacker.moveSelected(), defender);
    }

    public static void moveSelection(PokemonTrainer pt1, PokemonTrainer pt2) {
        try {
            chooseMove(pt1);
            chooseMove(pt2);

            pokemonTurn(pt1, pt2);
            pokemonTurn(pt2, pt1);
        } catch (PokemonFaintedException e) {
            BattleLog.add(e.getMessage());
        } 

        BattleField.endOfRound(pt1.pokemonInBattle(), pt2.pokemonInBattle());
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

        choosePokemon(player1);
        choosePokemon(player2);

        BattleLog.out();

        while (!player1.outOfPokemon() && !player2.outOfPokemon()) {
            while (!player1.pokemonInBattle().fainted() && !player2.pokemonInBattle().fainted()) 
                moveSelection(player1, player2);
            
            if (player1.pokemonInBattle().fainted()) choosePokemon(player1);
            if (player2.pokemonInBattle().fainted()) choosePokemon(player2);
        }

        if (player1.outOfPokemon()) {
            BattleLog.add(String.format("%s is out of Pokemon!", player1));
            BattleLog.add(String.format("%s wins the battle!", player2));
        }
        else {
            BattleLog.add(String.format("%s is out of Pokemon!", player2));
            BattleLog.add(String.format("%s wins the battle!", player1));
        }
    }

}
