package project.move.moveactions;

import project.battle.BattleLog;
import project.event.EventData;
import project.event.EventManager;
import project.pokemon.Pokemon;
import project.stats.StatusCondition;

public interface MoveActionCharge extends MoveAction{
     /*
     * Charges move first round, then unleashes it on the second
     * Can be interrupted by status effects
     */
    public static void chargeMove(EventManager eventManager) {
        EventData data  = eventManager.eventData;
        Pokemon attacker = data.user;

        if (!attacker.getConditions().hasKey(StatusCondition.CHARGE_MOVE)) {
            attacker.getConditions().addCondition(StatusCondition.chargeMove(attacker, data.moveUsed));    
            BattleLog.add("%s begins charging!", attacker);
            return;
        }
        
        attacker.getConditions().removeCondition(StatusCondition.CHARGE_MOVE);
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
        if (!attacker.getConditions().hasKey(StatusCondition.RAMPAGE_ID)) {
            attacker.getConditions().addCondition(StatusCondition.rampage(attacker, data.moveUsed));
        }
       
        MoveActionAttackDamage.dealDamage(eventManager);       
    }

}
