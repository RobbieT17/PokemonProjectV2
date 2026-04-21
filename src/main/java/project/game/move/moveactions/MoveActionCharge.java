package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventManager;
import project.game.exceptions.MoveEndedEarlyException;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.MovePhase;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.utility.RandomValues;

public interface MoveActionCharge extends MoveAction{
     /*
     * Charges move first round, then unleashes it on the second.
     * Can be interrupted.
     */
    private static void chargeMove(EventManager eventManager) {
        MovePhase phase = eventManager.data.moveUsed.getPhase();

        if (phase.equals(0)) { // Sets up the charge (lasts 2 phases)
            phase.set(2);
            throw new MoveEndedEarlyException("%s begins charging!", eventManager.data.user);
        }
        else if (phase.equals(1)){ // Attacks the target
            return;
        } 
        
        // Move is in some unknown state
        throw new IllegalStateException(String.format("Charge move in illegal phase: %s ", phase));
   
    }

    private static void focusMove(EventManager eventManager) {
        Pokemon p = eventManager.data.user;
        MovePhase phase = eventManager.data.moveUsed.getPhase();

        if (phase.equals(0)) { // Begins focus move (lasts 2 phases)
            phase.set(2);
            throw new MoveEndedEarlyException("%s is tightening its focus!", p);
        }
        else if (phase.equals(1)){ // Attacks the target
            if (p.getConditions().tookDamage()) {
                throw new MoveInterruptedException("%s lost its focus and couldn't move!", p);
            }
            return;
        } 
        
        // Move is in some unknown state
        throw new IllegalStateException(String.format("Charge move in illegal phase: %s ", phase));
    }

    private static void rechargeMove(EventManager eventManager) {
        Pokemon p  = eventManager.data.effectTarget;
        p.getConditions().setRecharge(true);
    }

    /**
     * Forces Pokemon to use the same move for 2-3 turns.
     * Rampage is disrupted if the move misses or the Pokemon
     * cannot act due to a status condition. If the move
     * ends uninterrupted, the user becomes confused.
     */
    private static void rampageMove(EventManager eventManager) {
        MovePhase phase = eventManager.data.moveUsed.getPhase();

        if (phase.equals(0)) { // Phase 0: Starts rampage
            // Rampage lasts 2 to 3 turns
            phase.set(RandomValues.generateInt(2, 3));
        }
        
        if (phase.moreThan(1)) { // Phases >1: Attack
            return;
        }
        else if (phase.equals(1)) { // Phase 1: Become confused and then attack once more
            MoveActionChangeCondition.applyCondition(eventManager, StatusConditionID.Confusion);
            return;
        }

        // Move is in some unknown state
        throw new IllegalStateException(String.format("Charge move in illegal phase: %s", phase)); 
             
    }

    public static void enterChargeState(EventManager eventManager, StatusConditionID id) {
        switch (id) {
            case StatusConditionID.Charge -> MoveActionCharge.chargeMove(eventManager);
            case StatusConditionID.Recharge -> MoveActionCharge.rechargeMove(eventManager);
            case StatusConditionID.Rampage -> MoveActionCharge.rampageMove(eventManager);
            case StatusConditionID.Focused -> MoveActionCharge.focusMove(eventManager);
            default -> throw new IllegalArgumentException("Unexpected value: " + id);
        }
    }

}
