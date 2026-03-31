package project.pokemon;

import project.battle.BattleLog;
import project.event.EventData;
import project.event.EventManager;
import project.event.GameEvent;
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
        this.eventManager.notifyUserPokemon(GameEvent.BEFORE_MOVE);
        this.eventManager.notifyUserPokemon(GameEvent.PRIMARY_STATUS_BEFORE);
        this.eventManager.notifyUserPokemon(GameEvent.STATUS_BEFORE);
    }

    private void updateInterruptedMoveEvents() {
        this.eventManager.notifyUserPokemon(GameEvent.MOVE_INTERRUPTED);
    }

    private void updateAfterMoveEvents() {
        this.eventManager.notifyUserPokemon(GameEvent.END_OF_TURN);
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
    public void useMove() {
        Pokemon user = this.eventData.user;
        Move move = this.eventData.moveUsed;

        BattleLog.add("%s used %s!", user, move);
        try {
            this.eventManager.notifyUserPokemon(GameEvent.USE_MOVE);
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
    }
}
