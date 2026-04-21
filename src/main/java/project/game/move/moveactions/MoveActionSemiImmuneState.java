package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.exceptions.MoveEndedEarlyException;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.effects.StatusContext;

public interface MoveActionSemiImmuneState {

    private static void flyState(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.fly(c));
    }

    private static void digState(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.dig(c));
    }

    private static void diveState(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.dive(c));
    }

    private static void enterState(EventManager eventManager, StatusConditionID state) {
        Pokemon p = eventManager.data.effectTarget;
        StatusContext c = new StatusContext(p);
        c.move = eventManager.data.moveUsed;

        switch (state) {
            case StatusConditionID.Fly_State -> MoveActionSemiImmuneState.flyState(c);
            case StatusConditionID.Dig_State -> MoveActionSemiImmuneState.digState(c);
            case StatusConditionID.Dive_State -> MoveActionSemiImmuneState.diveState(c);
            default -> throw new IllegalArgumentException("Unexpected value: " + state);
        }
    }

     // Semi-Immune State Function

    /*
     * Pokemon enters a semi-invulnerable state the first turn
     * Pokemon leaves the state and attacks on the second turn
     */
    public static void enterImmuneState(EventManager eventManager, StatusConditionID state) {
        EventData data  = eventManager.data;
        Pokemon attacker = data.user;
        data.immuneStateChange = state;

        // Enters semi-immune state if in it already (1nd part of the move)
        if (!attacker.getConditions().inImmuneState()) {
            MoveActionSemiImmuneState.enterState(eventManager, data.immuneStateChange);
            throw new MoveEndedEarlyException();
        } 
        else {  // Leaves the state if in it already (2nd part of the move)
            attacker.getConditions().removeCondition(data.immuneStateChange);
        }
    
    }

    /*
     * Pokemon is knocked out of their semi-invulnerable state, interrupted
     */
    public static void leaveImmuneState(EventManager eventManager, StatusConditionID state, String message) {
        EventData data  = eventManager.data;
        Pokemon p = data.attackTarget;
        data.immuneStateChange = StatusConditionID.No_Invul;
        data.message = message;
    
        if (p.getConditions().isFainted() || !p.getConditions().hasKey(data.immuneStateChange)) {
            return;
        }

        p.getConditions().removeCondition(data.immuneStateChange);
        p.getConditions().setInterrupted(true);
        p.resetMove();
        BattleLog.add(data.message);
    }
    
} 
