package project.game.processors;

import project.game.battle.BattleData;
import project.game.battle.BattleLog;
import project.game.battle.BattlePosition;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.MoveEndedEarlyException;
import project.game.exceptions.MoveInterruptedException;
import project.game.exceptions.PokemonCannotActException;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;;

public class PokemonProcessor implements Processor {

    private final BattleData battleData;
    private final Pokemon user;

    public PokemonProcessor(BattleData data, Pokemon p) {
        this.battleData = data;
        this.user = p;
    }

    private void updateBeforeMoveEvents() {
        EventManager eventManager = new EventManager(battleData, user);
        eventManager.notifyUserPokemon(EventID.BEFORE_MOVE);
        eventManager.notifyUserPokemon(EventID.PRIMARY_STATUS_BEFORE);
        eventManager.notifyUserPokemon(EventID.STATUS_BEFORE);
    }

    private void updateInterruptedMoveEvents() {
        EventManager eventManager = new EventManager(battleData, user);
        eventManager.notifyUserPokemon(EventID.MOVE_INTERRUPTED);
    }

    private void updateAfterMoveEvents() {
        EventManager eventManager = new EventManager(battleData, user);
        eventManager.notifyUserPokemon(EventID.END_OF_TURN);
    }

    private void stopOnGoingMoves(Pokemon p) {
        p.getConditions().removeCondition(StatusConditionID.Focused);
        p.getConditions().removeCondition(StatusConditionID.Forced_Move);
        p.getConditions().removeCondition(StatusConditionID.Rampage);
    }

    /**
     * Uses a move on a target
     * Decrements that moves PP
     * @param target the target Pokemon
     */
    private void useMove(Pokemon target) {
        EventManager eventManager = new EventManager(battleData, user, target);

        try {
            eventManager.notifyUserPokemon(EventID.USE_MOVE);
            new MoveProcessor(eventManager).process();

        } catch (MoveEndedEarlyException e) { // TODO: Entering immune state should throw this exception
            eventManager.data.message = e.getMessage();
        }
    }
    
    /**
     * Uses the move selected on each target.
     */
    private void useTurn(){
        boolean logMessage = true;
        int interruptCount = 0; 

        for (BattlePosition target : this.user.getTargetPositions()) {
            if (target.getCurrentPokemon() == null) {
                continue;
            }
            if (logMessage) { // Only execute during first loop iteration
                BattleLog.add("%s used %s!", user, user.getMoveSelected());
                logMessage = false;
            }
            
            try { // Move might be interrupted, move onto the next target
                this.useMove(target.getCurrentPokemon());  
            } catch (MoveInterruptedException e) {
                BattleLog.add(e.getMessage());
                interruptCount++;
            }
           
        }

        // Move interrupt by all move targets
        if (interruptCount >= this.user.getTargetPositions().length) {
            throw new MoveInterruptedException();
        }
    }

    /**
     * Checks any condition, then
     * Pokemon uses their move if actionable
     * Pokemon is considered to have moved (even if they cannot act)
     */
    @Override
    public void process() {
        user.getConditions().setHasMoved(true); 

        // Use turn to switch in new Pokemon
        if (user.getConditions().isSwitchedIn()) {
            user.getPosition().sendOutPokemon();
            return;
        }
       
        // No targets selected
        if (this.user.getTargetPositions() == null) {
            return;
        }
 
        // Pokemon will not act if any of these conditions are met.
        if (this.user.getConditions().isFainted()) {
            return;
        }

        BattleLog.add("-----------------------------------------------");
        this.user.getEvents().updateEventMaps();
        
        try {
            this.updateBeforeMoveEvents(); // Status condition checks
            this.useTurn();
            user.getConditions().setInterrupted(false); // Successful Move

        } catch (MoveInterruptedException | PokemonCannotActException e) {
            BattleLog.add(e.getMessage());

            this.updateInterruptedMoveEvents();
            this.stopOnGoingMoves(user);
            user.getConditions().setInterrupted(true);
                  
        } 

        this.updateAfterMoveEvents();
        this.user.getEvents().updateEventMaps();
    }

}
