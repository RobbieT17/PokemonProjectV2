package project.game.pokemon;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents;
import project.game.exceptions.MoveEndedEarlyException;
import project.game.exceptions.MoveInterruptedException;
import project.game.exceptions.PokemonCannotActException;
import project.game.move.Move;
import project.game.pokemon.stats.StatusCondition;

public class PokemonActions {

    private final EventData eventData;
    private final EventManager eventManager;

    public PokemonActions(Pokemon attacker, Pokemon defender, Move move) {
        this.eventManager = new EventManager(attacker, defender, move);
        this.eventData = this.eventManager.eventData;
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

    public void processEndOfRound() {
        
    }

   
}
