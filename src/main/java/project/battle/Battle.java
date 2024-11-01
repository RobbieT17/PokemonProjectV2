package project.battle;

import project.event.EventData;
import project.event.GameEvent;
import project.exceptions.BattleEndedException;
import project.move.Move;
import project.move.MoveList;
import project.player.*;
import project.pokemon.Pokemon;
import project.pokemon.PokemonList;
import project.stats.Ability;
import project.stats.HeldItem;
import project.stats.StatDisplay;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;


public class Battle {

    public static boolean skipRound; // Switched in Pokemon after a faint don't get attacked for the round

    // Can't switch pokemon if and only if the Pokemon has fainted or is the current one out
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
                if (!done) BattleLog.addPrint("%s is already on the field!%n", pokemon);
                
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
        p.events().onEvent(GameEvent.MOVE_SELECTION, null);

        /*
         * Unable to choose a move if just switched in
         * or needs to recharge 
         */
        if (p.conditions().switchedIn() | p.conditions().recharge()) {
            p.conditions().setRecharge(false);
            return p; 
        }
        // Default to struggle if all the Pokemon's move has no more PP
        if (p.hasNoMoves()) {
            BattleLog.add("%s has no moves!", p);
            p.setMove(MoveList.struggle());
        }

        Move move = p.moveSelected();

        if (move != null) {
            p.setMove(move);

            // if (p.conditions().hasKey(StatusCondition.FORCED_MOVE_ID) && pt.pokemonAvailable() > 1){
            //     BattleLog.addPrint("%s is going to use %s%n", p, p.moveSelected());
            //     BattleLog.addPrintln("[0] Continue%n[9] Switch Pokemon");

            //     Scanner scanner = new Scanner(System.in);
         
            //     while (true) {
            //         try {
            //             int input = scanner.nextInt();

            //             switch (input) {
            //                 case 0 -> {
            //                     return p; 
            //                 }
            //                 case 9 -> {
            //                     p = choosePokemon(pt);
            //                     return p;
            //                 }
            //                 default -> BattleLog.addPrint("Invalid input, try again");
            //             }


            //         } catch (InputMismatchException e) {
            //             BattleLog.addPrintln("Invalid input try again");
            //             scanner.next();
            //         }

            //     }
            // }

            return p;

        }

        BattleLog.addPrintln("=====================================");
        BattleLog.addPrintln(StatDisplay.showSomeStats(p));
        BattleLog.addPrintln("[9] Switch Pokemon");

        Scanner scanner = new Scanner(System.in);
        boolean done = false;

        while (!done) {
            try {
                BattleLog.addPrint("What should %s do? ", p); 
                int input = scanner.nextInt();

                /*
                 * Switches Pokemon out, not possible if trainer has one pokemon
                 * or Pokemon is unable to switch
                 */
                if (input == 9 && pt.pokemonAvailable() > 1){ 
                    p = choosePokemon(pt);
                    return p;
                }

                // Selects one of the Pokemon's move pool to use
                move = p.moves()[input];
                // Cannot use move if disabled or out of powerpoints
                done = !(move.pp().depleted() || move.disabled());
                
            
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

        // Updates an event listener for added status conditions
        a.events().updateEventMaps();
        b.events().updateEventMaps();
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
    public static void moveSelection() {
        PokemonTrainer pt1 = BattleField.player1;
        PokemonTrainer pt2 = BattleField.player2;

        if (Battle.skipRound) return;
        
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
       
    }

    public static void main(String[] args) {
        
        Pokemon p1 = PokemonList.venusaur("Bobby");
        p1.setAbility(Ability.chlorophyll(p1));
        // p1.setItem(HeldItem.choiceScarf(p1));

        Pokemon p2 = PokemonList.charizard("Charlie");
        p2.setAbility(Ability.blaze(p2));
        p2.setItem(HeldItem.rockyHelmet(p2));

        Pokemon p3 = PokemonList.blastoise("Squirt");
        p3.setAbility(Ability.torrent(p3));
        // p3.setItem(HeldItem.choiceSpecs(p3));

        Pokemon p4 = PokemonList.venusaur("Bub");
        p4.setAbility(Ability.waterAbsorb(p4));
        p4.setItem(HeldItem.blackSludge(p4));
 
        Pokemon p5 = PokemonList.charizard("Chandler");
        p5.setAbility(Ability.solarPower(p5));
        p5.setItem(HeldItem.muscleBand(p5));

        Pokemon p6 = PokemonList.blastoise("Tim");
        p6.setAbility(Ability.rainDish(p6));
        p6.setItem(HeldItem.leftovers(p6));

        BattleField.player1 = new PokemonTrainerBuilder()
        .setName("Robbie")
        .addPokemon(p1)
        .addPokemon(p2)
        .addPokemon(p3)
        .build();

        BattleField.player2 = new PokemonTrainerBuilder()
        .setName("Sammi")
        .addPokemon(p4)
        .addPokemon(p5)
        .addPokemon(p6)
        .build();

        PokemonTrainer pt1 = BattleField.player1;
        PokemonTrainer pt2 = BattleField.player2;

        choosePokemon(pt1);
        choosePokemon(pt2);
        
        BattleLog.out();
        Battle.skipRound = true;

        // Game ends when one trainer is out of Pokemon
        try {
            while (true) { // Continues forever
                while (!pt1.pokemonInBattle().conditions().fainted() && !pt2.pokemonInBattle().conditions().fainted()) {     
                    moveSelection();    
                    BattleField.endOfRound();      
                    BattleLog.out();  // Plays out the round messages
                }
                
                Battle.skipRound = true;
                if (pt1.pokemonInBattle().conditions().fainted()) choosePokemon(pt1);
                else if (pt2.pokemonInBattle().conditions().fainted()) choosePokemon(pt2);
                else throw new IllegalStateException("Pokemon must've fainted, what happened???");
            }
        } catch (BattleEndedException e) {
            BattleLog.add(e.getMessage());
        }

        BattleLog.out();
    }

}