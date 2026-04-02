package project.battle;

import java.util.Random;

import project.event.EventManager;
import project.event.GameEvents;
import project.move.Move;
import project.pokemon.Pokemon;
import project.pokemon.PokemonActions;

public class BattleActions {

    private final Pokemon pokemon1;
    private final Pokemon pokemon2;

    private Pokemon[] turnOrder;

    public BattleActions(Pokemon p1, Pokemon p2) {
        this.pokemon1 = p1;
        this.pokemon2 = p2;
    }

    // Finds the order which the Pokemon in battle will move
    public void constructTurnOrder() {
        EventManager eventManager = new EventManager(this.pokemon1, this.pokemon2);
        eventManager.notifyAllPokemon(GameEvents.FIND_MOVE_ORDER);

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
     */
    public void processPokemonActions() {
        Pokemon p1 = turnOrder[0];
        Pokemon p2 = turnOrder[1];

        // Faster pokemon acts first, followed by the second
        PokemonActions firstPokemonAction = new PokemonActions(p1, p2, p1.getMoveSelected());
        firstPokemonAction.useTurn();

        PokemonActions secondPokemonAction = new PokemonActions(p2, p1, p2.getMoveSelected());
        secondPokemonAction.useTurn();
    }

    /**
     * Checks if a Pokemon has fainted or the battle has been won (player is out of Pokemon)
     */
    public void checkWinConditions() {

    }

    /**
     * Process the after effects of a round.
     * Updates specific events and also
     * reset any Pokemon params needed.
     */
    public void processRoundEnd() {
        this.pokemon1.endOfRoundReset();
        this.pokemon2.endOfRoundReset();

        EventManager eventManager = new EventManager(this.pokemon1, this.pokemon2);
        eventManager.notifyAllPokemon(GameEvents.WEATHER_EFFECT);
        eventManager.notifyAllPokemon(GameEvents.END_OF_ROUND);
    }
}
