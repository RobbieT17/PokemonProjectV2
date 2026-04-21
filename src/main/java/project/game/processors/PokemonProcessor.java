package project.game.processors;

import project.game.battle.BattleData;
import project.game.battle.BattleLog;
import project.game.battle.BattlePosition;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.MoveEndedEarlyException;
import project.game.exceptions.MoveInterruptedException;
import project.game.exceptions.PokemonCannotActException;
import project.game.pokemon.Pokemon;

public class PokemonProcessor implements Processor {

    private final BattleData battleData;
    private final Pokemon user;

    public PokemonProcessor(BattleData data, Pokemon p) {
        this.battleData = data;
        this.user = p;
    }

    private void updateBeforeMoveEvents() {
        EventManager eventManager = new EventManager(this.battleData, this.user);
        eventManager.notifyUserPokemon(EventID.BEFORE_MOVE);
        eventManager.notifyUserPokemon(EventID.PRIMARY_STATUS_BEFORE);
        eventManager.notifyUserPokemon(EventID.STATUS_BEFORE);
    }

    private void updateInterruptedMoveEvents() {
        EventManager eventManager = new EventManager(this.battleData, this.user);
        eventManager.notifyUserPokemon(EventID.MOVE_INTERRUPTED);
    }

    private void updateAfterMoveEvents() {
        EventManager eventManager = new EventManager(this.battleData, this.user);
        eventManager.notifyUserPokemon(EventID.END_OF_TURN);
    }

    /**
     * Uses a move on a target
     * Decrements that moves PP
     * @param target the target Pokemon
     */
    private void useMove(Pokemon target) {
        EventManager eventManager = new EventManager(this.battleData, this.user, target);
        eventManager.notifyUserPokemon(EventID.USE_MOVE);
        new MoveProcessor(eventManager).process();        
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
                BattleLog.add("%s used %s!", this.user, this.user.getMoveSelected());
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
     * Validiates if the Pokemon can act
     * @return
     */
    public boolean validPokemon() {
        // Use turn to switch in new Pokemon
        if (this.user.getConditions().isSwitchedIn()) {
            this.user.getPosition().sendOutPokemon();
            return false;
        }
       
        // No targets selected
        if (this.user.getTargetPositions() == null) {
            return false;
        }
 
        // Pokemon will not act if any of these conditions are met.
        if (this.user.getConditions().isFainted()) {
            return false;
        }

        return true;
    }

    /**
     * Checks any condition, then
     * Pokemon uses their move if actionable
     * Pokemon is considered to have moved (even if they cannot act)
     */
    @Override
    public void process() {
        this.user.getConditions().setHasMoved(true); 

        if (!this.validPokemon()) {
            return;
        }

        BattleLog.add("-----------------------------------------------");
        this.user.getEvents().updateEventMaps();
        
        try {
            this.updateBeforeMoveEvents(); // Status condition checks
            this.useTurn();
            this.user.getConditions().setInterrupted(false); // Successful Move
        } 
        catch (MoveEndedEarlyException e) { // Move ended early, but considered successful
            BattleLog.add(e.getMessage());
            this.user.getConditions().setInterrupted(false);
        }
        catch (MoveInterruptedException | PokemonCannotActException e) { // Move was interrupt and unsuccessful
            BattleLog.add(e.getMessage());

            this.updateInterruptedMoveEvents();
            
            // Sets interrupt value to true, resets phase to 0
            this.user.getMoveSelected().getPhase().reset();
            this.user.getConditions().setInterrupted(true);   
        }

        this.updateAfterMoveEvents();
        this.user.getEvents().updateEventMaps();
    }

}
