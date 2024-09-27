package battle;

import event.EventData;
import event.GameEvent;
import exceptions.BattleEndedException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import move.Move;
import move.MoveList;
import player.*;
import pokemon.Pokemon;
import pokemon.PokemonList;
import stats.Ability;
import utility.Input;

public class Battle {

    public static boolean skipRound; // Switched in Pokemon after a faint don't get attacked for the round

    // Can switch pokemon if and only if the Pokemon hasn't fainted and isn't the current one out
    public static boolean validPokemonChoice(PokemonTrainer pt, Pokemon p) {
        return !p.conditions().fainted() && (pt.pokemonInBattle() != null) ? !pt.pokemonInBattle().equals(p) : true;
    }

    // Trainer chooses a Pokemon to send out to battle
    public static Pokemon choosePokemon(PokemonTrainer trainer) {
        if (trainer.outOfPokemon()) return null;

        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        Pokemon pokemon = trainer.pokemonInBattle();

        BattleLog.addPrintln("\n" + trainer.showPokemon());

        while (!done) {
            try {
                BattleLog.addPrint("%s, choose a Pokemon: ", trainer);
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
        return pokemon;
    }


    // Pokemon chooses a move
    public static Pokemon chooseMove(PokemonTrainer pt) {
        Pokemon p = pt.pokemonInBattle();

        /*
         * Unable to choose a move if just switched in
         * or needs to recharge 
         */
        if (p.conditions().switchedIn() | p.conditions().recharging()) {
            p.conditions().setRecharging(false);
            return p; 
        }
        // Default to struggle if all the Pokemon's move has no more PP
        if (p.hasNoMoves()) {
            BattleLog.add("%s has no moves!", p);
            p.setMove(MoveList.struggle());
        }

        // Uses last move used if forced to
        Move move = p.conditions().forcedMove() ? p.lastMove() : null;

        if (move != null) {
            p.setMove(move);
            return p;
        }

        BattleLog.addPrintln("=====================================");
        BattleLog.addPrintln(p.showSomeStats());
        BattleLog.addPrintln("[S] Switch Pokemon");

        Scanner scanner = new Scanner(System.in);
        boolean done = false;

        while (!done) {
            try {
                BattleLog.addPrint("What should %s do? ", p); 
                String input = scanner.nextLine();

                /*
                 * Switches Pokemon out, not possible if trainer has one pokemon
                 * or Pokemon is unable to switch
                 */
                if (Input.isChar(input, 's') && pt.pokemonAvailable() > 1){ 
                    p = choosePokemon(pt);
                    return p;
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
        return p;
    }

    // Finds the order which the Pokemon in battle will move
    public static Pokemon[] turnOrder(Pokemon p1, Pokemon p2) {
        p1.events().onEvent(GameEvent.FIND_MOVE_ORDER, null);
        p2.events().onEvent(GameEvent.FIND_MOVE_ORDER, null);

        Pokemon[] order = new Pokemon[2];

        Move m1 = p1.moveSelected();
        Move m2 = p2.moveSelected();

        int speed1 = p1.speed().power();
        int speed2 = p2.speed().power();

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
    
    // Pokemon uses a turn, nothing happens if the Pokemon did not select a move or one fainted
    public static void pokemonTurn(Pokemon a, Pokemon b) {
        if (a.conditions().fainted() || b.conditions().fainted() || a.moveSelected() == null) return;

        BattleLog.addLine();
        a.useTurn(new EventData(a, b, a.moveSelected()));
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
        if (Battle.skipRound) return;
        
        // Player choose their moves, returns the Pokemon (in case of a switch out)
        BattleField.pokemon1 = chooseMove(pt1);
        BattleField.pokemon2 = chooseMove(pt2);

        // Order of Pokemon
        Pokemon[] order = turnOrder(pt1.pokemonInBattle(), pt2.pokemonInBattle());

        Pokemon p1 = order[0];
        Pokemon p2 = order[1];

        // Pokemon use their moves, interrupted if one of them faints
        pokemonTurn(p1, p2);
        pokemonTurn(p2, p1);
       
    }

    public static void main(String[] args) {

        /**
         * TODO: Implement an algorithm to remove
         * all listeners when a Pokemon switches out.
         * Must re-add listener when Pokemon switches back
         * in
         */

        Pokemon p1 = PokemonList.venusaur("Bobby");
        p1.setAbility(Ability.chlorophyll(p1));

        Pokemon p2 = PokemonList.charizard("Charlie");
        p2.setAbility(Ability.blaze(p2));

        Pokemon p3 = PokemonList.blastoise("Squirt");
        p3.setAbility(Ability.torrent(p3));

        Pokemon p4 = PokemonList.venusaur("Bub");
        p4.setAbility(Ability.waterAbsorb(p4));

        Pokemon p5 = PokemonList.charizard("Chandler");
        p5.setAbility(Ability.solarPower(p5));

        Pokemon p6 = PokemonList.blastoise("Tim");
        p6.setAbility(Ability.rainDish(p6));

        PokemonTrainer player1 = new PokemonTrainerBuilder()
        .setName("Robbie")
        .addPokemon(p1)
        .addPokemon(p2)
        .addPokemon(p3)
        .build();

        PokemonTrainer player2 = new PokemonTrainerBuilder()
        .setName("Sammi")
        .addPokemon(p4)
        .addPokemon(p5)
        .addPokemon(p6)
        .build();

        BattleField.pokemon1 = choosePokemon(player1);
        BattleField.pokemon2 = choosePokemon(player2);

        BattleLog.out();
        Battle.skipRound = true;

        // Game ends when one trainer is out of Pokemon
        try {
            while (true) { // Continues forever
                while (!player1.pokemonInBattle().conditions().fainted() && !player2.pokemonInBattle().conditions().fainted()) {     
                    moveSelection(player1, player2);    
                    BattleField.endOfRound(player1, player2);      
                    BattleLog.out();  // Plays out the round messages
                }
                
                Battle.skipRound = true;
                if (player1.pokemonInBattle().conditions().fainted()) choosePokemon(player1);
                else if (player2.pokemonInBattle().conditions().fainted()) choosePokemon(player2);
                else throw new IllegalStateException("Pokemon must've fainted, what happened???");
            }
        } catch (BattleEndedException e) {
            BattleLog.add(e.getMessage());
        }

        BattleLog.out();
    }

}
