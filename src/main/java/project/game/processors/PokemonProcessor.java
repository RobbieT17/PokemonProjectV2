package project.game.processors;

import project.game.battle.BattleData;
import project.game.battle.BattleLog;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.MoveEndedEarlyException;
import project.game.exceptions.MoveInterruptedException;
import project.game.exceptions.PokemonCannotActException;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;;

public class PokemonProcessor {

    private final EventManager eventManager;
    
    public PokemonProcessor(BattleData data, Pokemon p) {
        this.eventManager = new EventManager(data, p);
    }

    private void updateBeforeMoveEvents() {
        this.eventManager.notifyUserPokemon(EventID.BEFORE_MOVE);
        this.eventManager.notifyUserPokemon(EventID.PRIMARY_STATUS_BEFORE);
        this.eventManager.notifyUserPokemon(EventID.STATUS_BEFORE);
    }

    private void updateInterruptedMoveEvents() {
        this.eventManager.notifyUserPokemon(EventID.MOVE_INTERRUPTED);
    }

    private void updateAfterMoveEvents() {
        this.eventManager.notifyUserPokemon(EventID.END_OF_TURN);
    }

    private void stopOnGoingMoves(Pokemon p) {
        p.getConditions().removeCondition(StatusConditionID.Focused);
        p.getConditions().removeCondition(StatusConditionID.Forced_Move);
        p.getConditions().removeCondition(StatusConditionID.Rampage);
    }

    /**
     * Uses a move on a target
     * Decrements that moves PP
     * @param move the Move chosen
     * @param defender the target Pokemon
     */
    private void useMove() {
        Pokemon user = this.eventManager.data.user;
        Move move = this.eventManager.data.moveUsed;

        BattleLog.add("%s used %s!", user, move);
        try {
            this.eventManager.notifyUserPokemon(EventID.USE_MOVE);
            move.getPp().decrement(user.getConditions().hasKey(StatusConditionID.Forced_Move));

            MoveProcessor moveProcessor = new MoveProcessor(eventManager);
            moveProcessor.processMove();

        } catch (MoveEndedEarlyException e) { // TODO: Entering immune state should throw this exception
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
        Pokemon user = this.eventManager.data.user;
        eventManager.updateEventMaps();
        
        try {
            this.updateBeforeMoveEvents();
            this.useMove();
            user.getConditions().setInterrupted(false); // Successful Move

        } catch (MoveInterruptedException | PokemonCannotActException e) {
            BattleLog.add(e.getMessage());

            this.updateInterruptedMoveEvents();
            this.stopOnGoingMoves(user);
            user.getConditions().setInterrupted(true);
                  
        } 
        user.getConditions().setHasMoved(true); 
        this.updateAfterMoveEvents();

        eventManager.updateEventMaps();
    }

    public void processEndOfRound() {
        
    }

   
}
