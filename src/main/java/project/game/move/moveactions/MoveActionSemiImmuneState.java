package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.exceptions.MoveEndedEarlyException;
import project.game.move.MovePhase;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.effects.StatusContext;

public interface MoveActionSemiImmuneState {

    private static void flyState(StatusContext c) {
        c.exceptions = new int[] {479, 542}; // Smack Down, Hurricane
        c.message = String.format("But %s is high in the sky!", c.target);
        BattleLog.add("%s flew into sky!", c.target); 
        
    }

    private static void digState(StatusContext c) {
        c.exceptions = new int[] {89}; // Earthquake
        c.message = String.format("But %s is underground!", c.target);
        BattleLog.add("%s dug underground!", c.target); 
    }

    private static void diveState(StatusContext c) {
        c.exceptions = new int[] {57, 250}; // Surf, Whirlpool
        c.message = String.format("But %s is underwater!", c.target);
        BattleLog.add("%s dove underwater!", c.target); 
    }

    private static void enterState(EventManager eventManager, StatusConditionID state) {
        EventData data = eventManager.data;
        Pokemon p = data.effectTarget;
        data.immuneStateChange = state;

        StatusContext c = new StatusContext(p);
        c.move = eventManager.data.moveUsed;

        switch (state) {
            case StatusConditionID.Fly_State -> MoveActionSemiImmuneState.flyState(c);
            case StatusConditionID.Dig_State -> MoveActionSemiImmuneState.digState(c);
            case StatusConditionID.Dive_State -> MoveActionSemiImmuneState.diveState(c);
            default -> throw new IllegalArgumentException("Unexpected value: " + state);
        }

        c.target.getConditions().addCondition(StatusConditionManager.semiImmune(c));
    }

     // Semi-Immune State Function

    /*
     * Pokemon enters a semi-invulnerable state the first turn
     * Pokemon leaves the state and attacks on the second turn
     */
    public static void enterImmuneState(EventManager eventManager, StatusConditionID state) {
        MovePhase phase = eventManager.data.moveUsed.getPhase();
 
        if (phase.equals(0)) { // Enters immune state
            phase.set(2);
            enterState(eventManager, state);
            throw new MoveEndedEarlyException();
        }
        else if (phase.equals(1)){ // Attacks the target
            // Removes immune state
            eventManager.data.user.getConditions().removeCondition(StatusConditionID.Semi_Immune);
            return;
        } 
        
        // Move is in some unknown state
        throw new IllegalStateException(String.format("Charge move in illegal phase: %s ", phase));
    
    }

    /*
     * Pokemon is knocked out of their semi-invulnerable state, interrupted
     */
    public static void leaveImmuneState(EventManager eventManager, StatusConditionID state, String message) {
        EventData data  = eventManager.data;
        Pokemon p = data.attackTarget;
        
        data.immuneStateChange = StatusConditionID.No_Invul;
        data.failMessage = message;
    
        if (p.getConditions().isFainted() || !p.getConditions().hasKey(data.immuneStateChange)) {
            return;
        }

        p.getMoveSelected().getPhase().reset();
        p.getConditions().removeCondition(data.immuneStateChange);
        p.getConditions().setInterrupted(true);
        p.resetMove();
    }
    
} 
