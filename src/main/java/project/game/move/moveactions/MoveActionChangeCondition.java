package project.game.move.moveactions;

import java.util.Random;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusCondition;
import project.game.pokemon.effects.StatusConditionManager;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.effects.StatusContext;
import project.game.pokemon.stats.Type;

public interface MoveActionChangeCondition extends MoveAction {
    
// Status Conditions Functions

    // Applies Burn Condition
    private static void applyBurn(StatusContext c) { 
        c.target.getConditions().setPrimaryCondition(StatusConditionManager.burn(c));
    }

    // Applies Freeze Condition
    private static void applyFreeze(StatusContext c) {
        c.target.getConditions().setPrimaryCondition(StatusConditionManager.freeze(c));
    }

    // Applies Infect Condition
    private static void applyInfect(StatusContext c) {
        c.target.getConditions().setPrimaryCondition(StatusConditionManager.infect(c));
    }

    // Applies Paralysis Condition
    private static void applyParalysis(StatusContext c) {
        c.target.getConditions().setPrimaryCondition(StatusConditionManager.paralysis(c));
    }

    // Applies Poison Condition
    private static void applyPoison(StatusContext c) {
        c.target.getConditions().setPrimaryCondition(StatusConditionManager.poisoned(c));
    }

    // Applies Badly Poison Condition
    private static void applyBadlyPoison(StatusContext c) {
        c.target.getConditions().setPrimaryCondition(StatusConditionManager.badlyPoisoned(c));
    }

    // Applies Sleep Condition
    private static void applySleep(StatusContext c) {
        c.target.getConditions().setPrimaryCondition(StatusConditionManager.sleep(c));
    }

    private static void flyState(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.fly(c));
    }

    private static void digState(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.dig(c));
    }

    private static void diveState(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.dive(c));
    }

    private static void applyFlinch(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.flinch(c));
    }

    // Applies Confusion Condition
    private static void applyBound(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.bound(c));
    }

    private static void applyConfusion(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.confusion(c));
    }

    private static void applySeeded(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.seeded(c));
    }

    private static boolean cannotApplyCondition(Pokemon p, StatusConditionID id) {
        return switch (id) {
            case StatusConditionID.Burn -> p.isType(Type.Fire) || p.getConditions().hasPrimary();
            case StatusConditionID.Freeze -> p.isType(Type.Ice) || p.getConditions().hasPrimary();
            case StatusConditionID.Infect -> p.getConditions().hasPrimary();
            case StatusConditionID.Paralysis -> p.isType(Type.Electric) || p.getConditions().hasPrimary();
            case StatusConditionID.Poison, StatusConditionID.Bad_Poison -> p.isType(Type.Poison) || p.isType(Type.Steel) || p.getConditions().hasPrimary();
            case StatusConditionID.Sleep -> p.isType(Type.Digital) || p.getConditions().hasPrimary();
            case StatusConditionID.Flinch -> p.getConditions().hasMoved();
            case StatusConditionID.Bound, StatusConditionID.Confusion -> p.getConditions().hasKey(id);
            case StatusConditionID.Seeded -> p.isType(Type.Grass) || p.getConditions().hasKey(id);
            default -> false;
        };
    }

    public static void applyCondition(EventManager eventManager, StatusConditionID id, double chance) {
        EventData data  = eventManager.data;
        Pokemon p = data.effectTarget;
        data.statusChange = id;
        data.statusProb = chance;
        
        if (cannotApplyCondition(p, id) || p.getConditions().isFainted()) {
            data.statusFailed = true;
            data.message = Move.FAILED;
            return;
        }

        if (new Random().nextDouble() > chance * 0.01) {
            data.statusFailed = true;
            return;
        }

        StatusContext c = new StatusContext(p);
        c.source = data.user;
            
        switch (id) {
            case StatusConditionID.Burn -> applyBurn(c);
            case StatusConditionID.Freeze -> applyFreeze(c);
            case StatusConditionID.Infect -> applyInfect(c);
            case StatusConditionID.Paralysis -> applyParalysis(c);
            case StatusConditionID.Poison -> applyPoison(c);
            case StatusConditionID.Bad_Poison -> applyBadlyPoison(c);
            case StatusConditionID.Sleep -> applySleep(c);
            case StatusConditionID.Flinch -> applyFlinch(c);
            case StatusConditionID.Bound -> applyBound(c);
            case StatusConditionID.Confusion -> applyConfusion(c);
            case StatusConditionID.Seeded -> applySeeded(c);
            case StatusConditionID.Fly_State -> {enterImmuneState(eventManager, id); flyState(c);}
            case StatusConditionID.Dig_State -> {enterImmuneState(eventManager, id); digState(c);}
            case StatusConditionID.Dive_State -> {enterImmuneState(eventManager, id); diveState(c);}
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        }  
    }

    public static void applyCondition(EventManager eventManager, StatusConditionID id) {
        applyCondition(eventManager, id, 100);
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

        // Leave states if in it already (2nd part of the move)
        if (attacker.getConditions().inImmuneState()) {
            attacker.getConditions().removeCondition(data.immuneStateChange);
            MoveActionAttack.attackTarget(eventManager);
        } 
    
    }

    /*
     * Pokemon is knocked out of their semi-invulnerable state, interrupted
     */
    public static void leaveImmuneState(EventManager eventManager, StatusConditionID state, String message) {
        EventData data  = eventManager.data;
        Pokemon p = data.attackTarget;
        data.immuneStateChange = StatusConditionID.No_Invul;
    
        if (p.getConditions().isFainted() || !p.getConditions().hasKey(state)) {
            return;
        }

        p.getConditions().removeCondition(state);
        p.getConditions().setInterrupted(true);
        p.resetMove();
        BattleLog.add(message);
    }

}
