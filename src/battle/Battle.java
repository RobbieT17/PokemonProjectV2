package battle;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import move.Move;
import move.MoveList;
import player.*;
import pokemon.Pokemon;
import pokemon.PokemonList;
import stats.StatusCondition;

public class Battle {

    // Can switch pokemon if and only if the Pokemon hasn't fainted and isn't the current one out
    public static boolean validPokemonChoice(PokemonTrainer pt, Pokemon p) {
        return !p.fainted() && (pt.pokemonInBattle() != null) ? !pt.pokemonInBattle().equals(p) : true;
    }

    // Trainer chooses a Pokemon to send out to battle
    public static void choosePokemon(PokemonTrainer trainer) {
        if (trainer.outOfPokemon()) return;

        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        Pokemon pokemon = trainer.pokemonInBattle();

        BattleLog.addPrintln("\n" + trainer.showPokemon());

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

        // Sets trainer's in-battle Pokemon to the one selected
        trainer.returns();
        trainer.sendOut(pokemon);
    }


    // Pokemon chooses a move
    public static void chooseMove(PokemonTrainer pt) {
        Pokemon p = pt.pokemonInBattle();

        /*
         * Unable to choose a move if just switched in
         * or charging (If changing, the pokemon uses last move selected)
         */
        if (p.charged() || p.switchedIn()) return;

        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        Move move = p.moveSelected();

        // Default to struggle if all the Pokemon's move has no more PP
        if (p.hasNoMoves()) {
            BattleLog.add(String.format("%s has no moves!", p));
            p.setMove(MoveList.struggle());
            return;
        }

        BattleLog.addPrintln("\n" + p.showAllStats());
        BattleLog.addPrintln("[S]: Switch Pokemon");

        while (!done) {
            try {
                BattleLog.addPrint(String.format("What should %s do? ", p)); 
                String input = scanner.nextLine();

                /*
                 * Switches Pokemon out, not possible if trainer has one pokemon
                 * or Pokemon is unable to switch
                 */
                if (Input.isChar(input, 's') && pt.pokemonAvailable() > 1){ 
                    choosePokemon(pt);
                    return;
                }

                // Selects one of the Pokemon's move pool to use
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
        // Locks in Pokemon's chosen move
        p.setMove(move);
    }

    // Finds the order which the Pokemon in battle will move
    public static Pokemon[] turnOrder(Pokemon p1, Pokemon p2) {
        Pokemon[] order = new Pokemon[2];

        Move m1 = p1.moveSelected();
        Move m2 = p2.moveSelected();

        // Paralyzed Pokemons' speed is reduced by half
        int speed1 = (int) (p1.hasPrimaryCondition(StatusCondition.PARALYSIS) ? p1.speed().power() * 0.5 : p1.speed().power());
        int speed2 = (int) (p2.hasPrimaryCondition(StatusCondition.PARALYSIS) ? p2.speed().power() * 0.5 : p2.speed().power());

        // Handles null moves (pokemon may not always have selected a move)
        if (m2 == null) {
            order[0] = p1;
            order[1] = p2;
            return order;
        }
        else if (m1 == null) {
            order[0] = p2;
            order[1] = p1;
            return order;
        }

        // Higher Priority Moves act first
        if (m1.priority() > m2.priority()) {
            order[0] = p1; 
            order[1] = p2;
        }
        else if (m1.priority() < m2.priority()) {
            order[0] = p2; 
            order[1] = p1;
        }
        // Pokemon with higher speed acts firsts
        else if (speed1 > speed2) {
            order[0] = p1;
            order[1] = p2;
        }
        else if (speed1 < speed2) {
            order[0] = p2;
            order[1] = p1;
        }
        // Speed Tie, move order is random
        else {
            if (new Random().nextDouble() < 0.5){
                order[0] = p1;
                order[1] = p2;
            }
            else {
                order[0] = p2;
                order[1] = p1;
            }
        }

        return order;
    }
    
    // Pokemon uses a turn, nothing happens if the Pokemon did not select a move
    public static void pokemonTurn(Pokemon a, Pokemon b) {
        if (a.moveSelected() == null) return;

        BattleLog.addLine();
        a.useTurn(a.moveSelected(), b);
    }

    /**
     * Players select one of two options
     * 1) Pokemon uses a move
     * 2) Switch Pokemon in battle
     * 
     * After choosing, the round will play out
     * @param pt1 Player 1
     * @param pt2 Player 2
     */
    public static void moveSelection(PokemonTrainer pt1, PokemonTrainer pt2) {
        try {
            // Player choose their moves
            chooseMove(pt1);
            chooseMove(pt2);

            // Order of Pokemon
            Pokemon[] order = turnOrder(pt1.pokemonInBattle(), pt2.pokemonInBattle());

            Pokemon p1 = order[0];
            Pokemon p2 = order[1];

            // Pokemon use their moves, interrupted if one of them faints
            pokemonTurn(p1, p2);
            pokemonTurn(p2, p1);
        } catch (PokemonFaintedException e) {
            BattleLog.add(e.getMessage());
        } 

        /**
         * Updates Battlefield attributes
         * Applies an after effects to each Pokemon
         * Resets any other necessary parameters
         */
        BattleField.endOfRound(pt1.pokemonInBattle(), pt2.pokemonInBattle());

        // Plays out the round messages
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

        // Game ends when one trainer is out of Pokemon
        while (!player1.outOfPokemon() && !player2.outOfPokemon()) {
            while (!player1.pokemonInBattle().fainted() && !player2.pokemonInBattle().fainted()) 
                moveSelection(player1, player2);
            
            if (player1.pokemonInBattle().fainted()) choosePokemon(player1);
            if (player2.pokemonInBattle().fainted()) choosePokemon(player2);
        }

        // Displays the winner
        if (player1.outOfPokemon()) {
            BattleLog.add(String.format("%n%s is out of Pokemon!", player1));
            BattleLog.add(String.format("%s wins the battle!", player2));
        }
        else {
            BattleLog.add(String.format("%n%s is out of Pokemon!", player2));
            BattleLog.add(String.format("%s wins the battle!", player1));
        }

        BattleLog.out();
    }

}
