package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.effects.StatusContext;

public interface MoveActionCharge extends MoveAction{
     /*
     * Charges move first round, then unleashes it on the second.
     * Can be interrupted by status effects
     */
    private static void chargeMove(EventManager eventManager) {
        EventData data  = eventManager.data;
        Pokemon attacker = data.user;

        if (!attacker.getConditions().hasKey(StatusConditionID.Charge)) {
            StatusContext c = new StatusContext(attacker);
            c.move = data.moveUsed;

            attacker.getConditions().addCondition(StatusConditionManager.chargeMove(c));    
            BattleLog.add("%s begins charging!", attacker);
            eventManager.data.moveEndedEarly = true;
        }
        else {
            attacker.getConditions().removeCondition(StatusConditionID.Charge);
            eventManager.data.moveEndedEarly = false;
        }
        
    }

    private static void focusMove(EventManager eventManager) {
        BattleLog.add("<NOT IMPLEMENTED: Focus Move>");
    }

    private static void rechargeMove(EventManager eventManager) {
        EventData data  = eventManager.data;
        data.user.getConditions().setRecharge(true);
    }

    /**
     * Forces Pokemon to use the same move for 2-3 turns.
     * Rampage is disrupted if the move misses or the Pokemon
     * cannot act due to a status condition. If the move
     * ends uninterrupted, the user becomes confused.
     */
    private static void rampageMove(EventManager eventManager) {
        EventData data  = eventManager.data;
        Pokemon attacker = data.user;

        // Starts rampage
        if (!attacker.getConditions().hasKey(StatusConditionID.Rampage)) {
            StatusContext c = new StatusContext(attacker);
            c.move = data.moveUsed;

            attacker.getConditions().addCondition(StatusConditionManager.rampage(c));
        }
             
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
