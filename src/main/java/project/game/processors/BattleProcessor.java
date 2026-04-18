package project.game.processors;

import java.util.Random;

import project.game.battle.BattleData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.BattleEndedException;
import project.game.exceptions.PokemonFaintedException;
import project.game.move.Move;
import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokemon;

public class BattleProcessor {

    private final BattleData battleData;

    private final PokemonTrainer player1;
    private final PokemonTrainer player2;

    private Pokemon[] turnOrder;

    public BattleProcessor(BattleData data) {
        this.battleData = data;
        this.player1 = data.getPlayer1();
        this.player2 = data.getPlayer2();
    }

    // Finds the order which the Pokemon in battle will act
    public void constructTurnOrder() {
        Pokemon pokemon1 = this.player1.getPokemonInBattle();
        Pokemon pokemon2 = this.player2.getPokemonInBattle();

        EventManager eventManager1 = new EventManager(this.battleData, pokemon1);
        EventManager eventManager2 = new EventManager(this.battleData, pokemon2);

        eventManager1.notifyUserPokemon(EventID.FIND_MOVE_ORDER);
        eventManager2.notifyUserPokemon(EventID.FIND_MOVE_ORDER);

        Pokemon[] order = new Pokemon[2];

        Move m1 = pokemon1.getMoveSelected();
        Move m2 = pokemon2.getMoveSelected();

        int speed1 = pokemon1.getSpeed().getPower();
        int speed2 = pokemon2.getSpeed().getPower();

        // Higher Priority Moves act first
        if (m1.getPriority() > m2.getPriority()) {
            order[0] = pokemon1; 
            order[1] = pokemon2;
        }
        else if (m2.getPriority() > m1.getPriority()) {
            order[0] = pokemon2; 
            order[1] = pokemon1;
        }
        // Pokemon with higher speed acts firsts
        else if (speed1 > speed2) {
            order[0] = pokemon1;
            order[1] = pokemon2;
        }
        else if (speed2 > speed1) {
            order[0] = pokemon2;
            order[1] = pokemon1;
        }
        // Speed Tie, move order is random
        else {
            if (new Random(System.currentTimeMillis()).nextDouble() < 0.5){
                order[0] = pokemon1;
                order[1] = pokemon2;
            }
            else {
                order[0] = pokemon2;
                order[1] = pokemon1;
            }
        }

        this.turnOrder = order;
    }

    /**
     * Each pokemon acts and targets their opponent with the move chosen
     * Function terminates early if a <b>PokemonFaintedException</b> is caught.
     */
    public void processPokemonActions() {
        // Faster pokemon acts first, followed by the second
        for (Pokemon p : this.turnOrder) {
            new PokemonProcessor(this.battleData, p).useTurn();
        }
        
    }

    /**
     * Checks if a Pokemon has fainted or the battle has been won (player is out of Pokemon)
     */
    public void checkWinConditions() {        
        if (this.player1.outOfPokemon() && this.player2.outOfPokemon()) {
            throw new BattleEndedException("Both players are out of Pokemon, it's a tie!");
        }
        else if (this.player1.outOfPokemon()) {
            throw new BattleEndedException(this.player2, this.player1);
        }
        else if (this.player2.outOfPokemon()){
            throw new BattleEndedException(this.player1, this.player2);
        }
    }

    /**
     * Process the after effects of a round.
     * Updates specific events and also
     * reset any Pokemon params needed.
     * 
     */
    public void processRoundEnd() {
        for (Pokemon p : this.turnOrder) {
            p.endOfRoundReset();

            if (p.getConditions().isFainted()) { // Skips Pokemon if fainted
                continue;
            }

            try {
                // TODO: Tick weather

                EventManager eventManager = new EventManager(this.battleData, p);
                eventManager.updateEventMaps();
                eventManager.notifyUserPokemon(EventID.WEATHER_EFFECT);
                eventManager.notifyUserPokemon(EventID.PRIMARY_STATUS_AFTER);
                eventManager.notifyUserPokemon(EventID.STATUS_AFTER);
                eventManager.notifyUserPokemon(EventID.END_OF_ROUND);
            } catch (PokemonFaintedException e) { 
                // Some effects might cause the Pokemon to faint, skip to next Pokemon if so
                continue;
            }
            
        }

    }

}
