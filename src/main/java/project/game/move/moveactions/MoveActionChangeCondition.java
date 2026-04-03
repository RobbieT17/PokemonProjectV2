package project.game.move.moveactions;

import java.util.Random;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusCondition;
import project.game.pokemon.effects.StatusConditionManager;
import project.game.pokemon.effects.StatusContext;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionIDs;
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

    private static boolean cannotApplyCondition(Pokemon p, StatusConditionIDs id) {
        return switch (id) {
            case StatusConditionIDs.BURN_ID -> p.isType(Type.FIRE) || p.getConditions().hasPrimary();
            case StatusConditionIDs.FREEZE_ID -> p.isType(Type.ICE) || p.getConditions().hasPrimary();
            case StatusConditionIDs.INFECT_ID -> p.getConditions().hasPrimary();
            case StatusConditionIDs.PARALYSIS_ID -> p.isType(Type.ELECTRIC) || p.getConditions().hasPrimary();
            case StatusConditionIDs.POISON_ID, StatusConditionIDs.BAD_POISON_ID -> p.isType(Type.POISON) || p.isType(Type.STEEL) || p.getConditions().hasPrimary();
            case StatusConditionIDs.SLEEP_ID -> p.isType(Type.DIGITAL) || p.getConditions().hasPrimary();
            case StatusConditionIDs.FLINCH_ID -> p.getConditions().hasMoved();
            case StatusConditionIDs.BOUND_ID, StatusConditionIDs.CONFUSION_ID -> p.getConditions().hasKey(id);
            case StatusConditionIDs.SEEDED_ID -> p.isType(Type.GRASS) || p.getConditions().hasKey(id);
            default -> false;
        };
    }

    public static void applyCondition(EventManager eventManager, StatusConditionIDs id, double chance) {
        EventData data  = eventManager.eventData;
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
            case StatusConditionIDs.BURN_ID -> applyBurn(c);
            case StatusConditionIDs.FREEZE_ID -> applyFreeze(c);
            case StatusConditionIDs.INFECT_ID -> applyInfect(c);
            case StatusConditionIDs.PARALYSIS_ID -> applyParalysis(c);
            case StatusConditionIDs.POISON_ID -> applyPoison(c);
            case StatusConditionIDs.BAD_POISON_ID -> applyBadlyPoison(c);
            case StatusConditionIDs.SLEEP_ID -> applySleep(c);
            case StatusConditionIDs.FLINCH_ID -> applyFlinch(c);
            case StatusConditionIDs.BOUND_ID -> applyBound(c);
            case StatusConditionIDs.CONFUSION_ID -> applyConfusion(c);
            case StatusConditionIDs.SEEDED_ID -> applySeeded(c);
            case StatusConditionIDs.FLY_ID -> flyState(c);
            case StatusConditionIDs.DIG_ID -> digState(c);
            case StatusConditionIDs.DIVE_ID -> diveState(c);
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        }  
    }

    public static void applyCondition(EventManager eventManager, StatusConditionIDs id) {
        applyCondition(eventManager, id, 100);
    }

    // Semi-Immune State Function

    /*
     * Pokemon enters a semi-invulnerable state the first turn
     * Pokemon leaves the state and attacks on the second turn
     */
    public static void enterImmuneState(EventManager eventManager, StatusConditionIDs state) {
        EventData data  = eventManager.eventData;
        Pokemon attacker = data.user;
        data.immuneStateChange = state;

        // Enters state
        if (!attacker.getConditions().inImmuneState()) {
            applyCondition(eventManager, state, 100);
            return;
        }

        attacker.getConditions().removeCondition(data.immuneStateChange);
        MoveActionAttackDamage.dealDamage(eventManager);
    }

    /*
     * Pokemon is knocked out of their semi-invulnerable state, interrupted
     */
    public static void leaveImmuneState(EventManager eventManager, StatusConditionIDs state, String message) {
        EventData data  = eventManager.eventData;
        Pokemon p = data.attackTarget;
        data.immuneStateChange = StatusConditionIDs.NO_INVUL_ID;
    
        if (p.getConditions().isFainted() || !p.getConditions().hasKey(state)) {
            return;
        }

        p.getConditions().removeCondition(state);
        p.getConditions().setInterrupted(true);
        p.resetMove();
        BattleLog.add(message);
    }

}
