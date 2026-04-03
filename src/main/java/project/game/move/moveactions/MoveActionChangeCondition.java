package project.game.move.moveactions;

import java.util.Random;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusCondition;
import project.game.pokemon.effects.StatusConditionManager;
import project.game.pokemon.stats.Type;

public interface MoveActionChangeCondition extends MoveAction {
    
// Status Conditions Functions

    // Applies Burn Condition
    private static void applyBurn(Pokemon p) { 
        p.getConditions().setPrimaryCondition(StatusConditionManager.burn(p));
    }

    // Applies Freeze Condition
    private static void applyFreeze(Pokemon p) {
        p.getConditions().setPrimaryCondition(StatusConditionManager.freeze(p));
    }

    // Applies Infect Condition
    private static void applyInfect(Pokemon p) {
        p.getConditions().setPrimaryCondition(StatusConditionManager.infect(p));
    }

    // Applies Paralysis Condition
    private static void applyParalysis(Pokemon p) {
        p.getConditions().setPrimaryCondition(StatusConditionManager.paralysis(p));
    }

    // Applies Poison Condition
    private static void applyPoison(Pokemon p) {
        p.getConditions().setPrimaryCondition(StatusConditionManager.poisoned(p));
    }

    // Applies Badly Poison Condition
    private static void applyBadlyPoison(Pokemon p) {
        p.getConditions().setPrimaryCondition(StatusConditionManager.badlyPoisoned(p));
    }

    // Applies Sleep Condition
    private static void applySleep(Pokemon p) {
        p.getConditions().setPrimaryCondition(StatusConditionManager.sleep(p));
    }

    private static void flyState(EventManager eventManager) {
        EventData data  = eventManager.eventData;
        Pokemon p = data.user;
        p.getConditions().addCondition(StatusConditionManager.fly(p, data.moveUsed));
    }

    private static void digState(EventManager eventManager) {
        EventData data  = eventManager.eventData;
        Pokemon p = data.user;
        p.getConditions().addCondition(StatusConditionManager.dig(p, data.moveUsed));
    }

    private static void diveState(EventManager eventManager) {
        EventData data  = eventManager.eventData;
        Pokemon p = data.user;
        p.getConditions().addCondition(StatusConditionManager.dive(p, data.moveUsed));
    }

    private static void applyFlinch(Pokemon p) {
        p.getConditions().addCondition(StatusConditionManager.flinch(p));
    }

    // Applies Confusion Condition
    private static void applyBound(Pokemon p) {
        p.getConditions().addCondition(StatusConditionManager.bound(p));
    }

    private static void applyConfusion(Pokemon p) {
        p.getConditions().addCondition(StatusConditionManager.confusion(p));
    }

    private static void applySeeded(Pokemon p, Pokemon r) {
        p.getConditions().addCondition(StatusConditionManager.seeded(p, r));
    }

    private static boolean cannotApplyCondition(Pokemon p, String id) {
        return switch (id) {
            case StatusCondition.BURN_ID -> p.isType(Type.FIRE) || p.getConditions().hasPrimary();
            case StatusCondition.FREEZE_ID -> p.isType(Type.ICE) || p.getConditions().hasPrimary();
            case StatusCondition.INFECT_ID -> p.getConditions().hasPrimary();
            case StatusCondition.PARALYSIS_ID -> p.isType(Type.ELECTRIC) || p.getConditions().hasPrimary();
            case StatusCondition.POISON_ID, StatusCondition.BAD_POISON_ID -> p.isType(Type.POISON) || p.isType(Type.STEEL) || p.getConditions().hasPrimary();
            case StatusCondition.SLEEP_ID -> p.isType(Type.DIGITAL) || p.getConditions().hasPrimary();
            case StatusCondition.FLINCH_ID -> p.getConditions().hasMoved();
            case StatusCondition.BOUND_ID, StatusCondition.CONFUSION_ID -> p.getConditions().hasKey(id);
            case StatusCondition.SEEDED_ID -> p.isType(Type.GRASS) || p.getConditions().hasKey(id);
            default -> false;
        };
    }

    public static void applyCondition(EventManager eventManager, String id, double chance) {
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
            
        switch (id) {
            case StatusCondition.BURN_ID -> applyBurn(p);
            case StatusCondition.FREEZE_ID -> applyFreeze(p);
            case StatusCondition.INFECT_ID -> applyInfect(p);
            case StatusCondition.PARALYSIS_ID -> applyParalysis(p);
            case StatusCondition.POISON_ID -> applyPoison(p);
            case StatusCondition.BAD_POISON_ID -> applyBadlyPoison(p);
            case StatusCondition.SLEEP_ID -> applySleep(p);
            case StatusCondition.FLINCH_ID -> applyFlinch(p);
            case StatusCondition.BOUND_ID -> applyBound(p);
            case StatusCondition.CONFUSION_ID -> applyConfusion(p);
            case StatusCondition.SEEDED_ID -> applySeeded(data.attackTarget, data.user);
            case StatusCondition.FLY_ID -> flyState(eventManager);
            case StatusCondition.DIG_ID -> digState(eventManager);
            case StatusCondition.DIVE_ID -> diveState(eventManager);
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        }  
    }

    public static void applyCondition(EventManager eventManager, String id) {
        applyCondition(eventManager, id, 100);
    }

    // Semi-Immune State Function

    /*
     * Pokemon enters a semi-invulnerable state the first turn
     * Pokemon leaves the state and attacks on the second turn
     */
    public static void enterImmuneState(EventManager eventManager, String state) {
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
    public static void leaveImmuneState(EventManager eventManager, String state, String message) {
        EventData data  = eventManager.eventData;
        Pokemon p = data.attackTarget;
        data.immuneStateChange = StatusCondition.NO_INVUL_ID;
    
        if (p.getConditions().isFainted() || !p.getConditions().hasKey(state)) {
            return;
        }

        p.getConditions().removeCondition(state);
        p.getConditions().setInterrupted(true);
        p.resetMove();
        BattleLog.add(message);
    }

}
