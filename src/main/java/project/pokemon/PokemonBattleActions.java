package project.pokemon;

import java.util.Random;

import project.battle.BattleLog;
import project.event.EventData;
import project.event.EventManager;
import project.event.GameEvents;
import project.exceptions.MoveEndedEarlyException;
import project.exceptions.MoveInterruptedException;
import project.exceptions.PokemonCannotActException;
import project.move.Move;
import project.stats.StatusCondition;

public class PokemonBattleActions {

    private final EventData eventData;
    private final EventManager eventManager;

    public PokemonBattleActions(Pokemon attacker, Pokemon defender, Move move) {
        this.eventData = new EventData(attacker, defender, move);
        this.eventManager = new EventManager(this.eventData);
    }

    private void updateBeforeMoveEvents() {
        this.eventManager.notifyUserPokemon(GameEvents.BEFORE_MOVE);
        this.eventManager.notifyUserPokemon(GameEvents.PRIMARY_STATUS_BEFORE);
        this.eventManager.notifyUserPokemon(GameEvents.STATUS_BEFORE);
    }

    private void updateInterruptedMoveEvents() {
        this.eventManager.notifyUserPokemon(GameEvents.MOVE_INTERRUPTED);
    }

    private void updateAfterMoveEvents() {
        this.eventManager.notifyUserPokemon(GameEvents.END_OF_TURN);
    }

    private void stopOnGoingMoves(Pokemon p) {
        p.getConditions().removeCondition(StatusCondition.FOCUSED_ID);
        p.getConditions().removeCondition(StatusCondition.FORCED_MOVE_ID);
        p.getConditions().removeCondition(StatusCondition.RAMPAGE_ID);
        p.getConditions().setInterrupted(true);
    }

    /**
     * Uses a move on a target
     * Decrements that moves PP
     * @param move the Move chosen
     * @param defender the target Pokemon
     */
    private void useMove() {
        Pokemon user = this.eventData.user;
        Move move = this.eventData.moveUsed;

        BattleLog.add("%s used %s!", user, move);
        try {
            this.eventManager.notifyUserPokemon(GameEvents.USE_MOVE);
            move.getPp().decrement(user.getConditions().hasKey(StatusCondition.FORCED_MOVE_ID));
            move.getAction().act(this.eventManager);
        } catch (MoveEndedEarlyException e) {
            BattleLog.add(e.getMessage());
        }
    }

    /**
     * Checks any condition, then
     * Pokemon uses their move if actionable
     * Pokemon is considered to have moved (even if they cannot act)
     * 
     * @param move the Move chosen
     * @param defender the target Pokemon
     */
    public void useTurn(){
        Pokemon user = this.eventData.user;

        try {
            this.updateBeforeMoveEvents();
            this.useMove();
            user.getConditions().setInterrupted(false); // Successful Move

        } catch (MoveInterruptedException | PokemonCannotActException e) {
            BattleLog.add(e.getMessage());

            this.updateInterruptedMoveEvents();
            this.stopOnGoingMoves(user);
                  
        } 
        user.getConditions().setHasMoved(true); 
        this.updateAfterMoveEvents();

        eventManager.updateEventMaps();
    }

    // Finds the order which the Pokemon in battle will move
    public static Pokemon[] turnOrder(Pokemon p1, Pokemon p2) {
        p1.getEvents().updateEvent(GameEvents.FIND_MOVE_ORDER, null);
        p2.getEvents().updateEvent(GameEvents.FIND_MOVE_ORDER, null);

        Pokemon[] order = new Pokemon[2];

        Move m1 = p1.getMoveSelected();
        Move m2 = p2.getMoveSelected();

        int speed1 = p1.getSpeed().getPower();
        int speed2 = p2.getSpeed().getPower();

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
        if (m1.getPriority() > m2.getPriority()) {
            order[0] = p1; 
            order[1] = p2;
        }
        else if (m1.getPriority() < m2.getPriority()) {
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
}
