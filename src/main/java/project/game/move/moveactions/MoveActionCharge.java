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
     * Charges move first round, then unleashes it on the second
     * Can be interrupted by status effects
     */
    public static void chargeMove(EventManager eventManager) {
        EventData data  = eventManager.eventData;
        Pokemon attacker = data.user;

        if (!attacker.getConditions().hasKey(StatusConditionID.Charge)) {
            StatusContext c = new StatusContext(attacker);
            c.move = data.moveUsed;

            attacker.getConditions().addCondition(StatusConditionManager.chargeMove(c));    
            BattleLog.add("%s begins charging!", attacker);
            return;
        }
        
        attacker.getConditions().removeCondition(StatusConditionID.Charge);
        MoveActionAttackDamage.dealDamage(eventManager); 
    }


    public static void focusMove(EventManager eventManager) {
        BattleLog.add("<NOT IMPLEMENTED: Focus Move>");
    }

    public static void rechargeMove(EventManager eventManager) {
        EventData data  = eventManager.eventData;
        data.user.getConditions().setRecharge(true);
    }

    /**
     * Forces Pokemon to use the same move for 2-3 turns
     * Rampage is disrupt if the move misses or the Pokemon
     * cannot act due to a status condition
     */
    public static void rampageMove(EventManager eventManager) {
        EventData data  = eventManager.eventData;
        Pokemon attacker = data.user;

        // Starts rampage
        if (!attacker.getConditions().hasKey(StatusConditionID.Rampage)) {
            StatusContext c = new StatusContext(attacker);
            c.move = data.moveUsed;

            attacker.getConditions().addCondition(StatusConditionManager.rampage(c));
        }
       
        MoveActionAttackDamage.dealDamage(eventManager);       
    }

}
