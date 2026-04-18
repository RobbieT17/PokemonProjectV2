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

    private final Pokemon pokemon1;
    private final Pokemon pokemon2;

    private Pokemon[] turnOrder;

    public BattleProcessor(BattleData data) {
        this.battleData = data;
        this.pokemon1 = data.getPlayer1().getPokemonInBattle();
        this.pokemon2 = data.getPlayer2().getPokemonInBattle();
    }

    // Finds the order which the Pokemon in battle will move
    public void constructTurnOrder() {
        EventManager eventManager1 = new EventManager(this.battleData, this.pokemon1);
        EventManager eventManager2 = new EventManager(this.battleData, this.pokemon2);

        eventManager1.notifyUserPokemon(EventID.FIND_MOVE_ORDER);
        eventManager2.notifyUserPokemon(EventID.FIND_MOVE_ORDER);

        Pokemon[] order = new Pokemon[2];

        Move m1 = this.pokemon1.getMoveSelected();
        Move m2 = this.pokemon2.getMoveSelected();

        int speed1 = this.pokemon1.getSpeed().getPower();
        int speed2 = this.pokemon2.getSpeed().getPower();

        // Handles null moves (pokemon may not always have selected a move)
        if (m2 == null) {
            order[0] = this.pokemon1;
            order[1] = this.pokemon2;
        }
        else if (m1 == null) {
            order[0] = this.pokemon2;
            order[1] = this.pokemon1;
        }
        // Higher Priority Moves act first
        else if (m1.getPriority() > m2.getPriority()) {
            order[0] = this.pokemon1; 
            order[1] = this.pokemon2;
        }
        else if (m1.getPriority() < m2.getPriority()) {
            order[0] = this.pokemon2; 
            order[1] = this.pokemon1;
        }
        // Pokemon with higher speed acts firsts
        else if (speed1 > speed2) {
            order[0] = this.pokemon1;
            order[1] = this.pokemon2;
        }
        else if (speed1 < speed2) {
            order[0] = this.pokemon2;
            order[1] = this.pokemon1;
        }
        // Speed Tie, move order is random
        else {
            if (new Random().nextDouble() < 0.5){
                order[0] = this.pokemon1;
                order[1] = this.pokemon2;
            }
            else {
                order[0] = this.pokemon2;
                order[1] = this.pokemon1;
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
        try {
            for (Pokemon p : this.turnOrder) {
            PokemonProcessor pokemonProcessor = new PokemonProcessor(this.battleData, p);
            pokemonProcessor.useTurn();
        }
        } catch (PokemonFaintedException e) {
            return;
        }
        
    }

    /**
     * Checks if a Pokemon has fainted or the battle has been won (player is out of Pokemon)
     */
    public void checkWinConditions() {
        PokemonTrainer t1 = this.pokemon1.getOwner();
        PokemonTrainer t2 = this.pokemon2.getOwner();
        
        if (t1.outOfPokemon() && t2.outOfPokemon()) {
            throw new BattleEndedException("Both players are out of Pokemon, it's a tie!");
        }
        else if (t1.outOfPokemon()) {
            throw new BattleEndedException(t2, t1);
        }
        else if (t2.outOfPokemon()){
            throw new BattleEndedException(t1, t2);
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

            try {
                // TODO: Tick weather

                EventManager eventManager = new EventManager(this.battleData, p);
                eventManager.updateEventMaps();
                eventManager.notifyUserPokemon(EventID.WEATHER_EFFECT);
                // eventManager.notifyUserPokemon(EventID.PRIMARY_STATUS_AFTER);
                // eventManager.notifyUserPokemon(EventID.STATUS_AFTER);
                eventManager.notifyUserPokemon(EventID.END_OF_ROUND);
            } catch (PokemonFaintedException e) { 
                // Some effects might cause the Pokemon to faint, skip to next Pokemon if so
                continue;
            }
            
        }

    }

}
