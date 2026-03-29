package project.move.moveactions;

import java.util.Random;

import project.battle.BattleLog;
import project.event.EventData;
import project.move.Move;
import project.pokemon.Pokemon;
import project.stats.StatusCondition;
import project.stats.Type;

public interface MoveActionChangeCondition extends MoveAction {
    
// Status Conditions Functions

    // Applies Burn Condition
    private static void applyBurn(Pokemon p) { 
        p.conditions().setPrimaryCondition(StatusCondition.burn(p));
    }

    // Applies Freeze Condition
    private static void applyFreeze(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.freeze(p));
    }

    // Applies Infect Condition
    private static void applyInfect(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.infect(p));
    }

    // Applies Paralysis Condition
    private static void applyParalysis(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.paralysis(p));
    }

    // Applies Poison Condition
    private static void applyPoison(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.poisoned(p));
    }

    // Applies Badly Poison Condition
    private static void applyBadlyPoison(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.badlyPoisoned(p));
    }

    // Applies Sleep Condition
    private static void applySleep(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.sleep(p));
    }

    private static void flyState(EventData data) {
        Pokemon p = data.user;
        p.conditions().addCondition(StatusCondition.fly(p, data.moveUsed));
    }

    private static void digState(EventData data) {
        Pokemon p = data.user;
        p.conditions().addCondition(StatusCondition.dig(p, data.moveUsed));
    }

    private static void diveState(EventData data) {
        Pokemon p = data.user;
        p.conditions().addCondition(StatusCondition.dive(p, data.moveUsed));
    }

    private static void applyFlinch(Pokemon p) {
        p.conditions().addCondition(StatusCondition.flinch(p));
    }

    // Applies Confusion Condition
    private static void applyBound(Pokemon p) {
        p.conditions().addCondition(StatusCondition.bound(p));
    }

    private static void applyConfusion(Pokemon p) {
        p.conditions().addCondition(StatusCondition.confusion(p));
    }

    private static void applySeeded(Pokemon p, Pokemon r) {
        p.conditions().addCondition(StatusCondition.seeded(p, r));
    }

    private static boolean cannotApplyCondition(Pokemon p, String id) {
        return switch (id) {
            case StatusCondition.BURN_ID -> p.isType(Type.FIRE) || p.conditions().hasPrimary();
            case StatusCondition.FREEZE_ID -> p.isType(Type.ICE) || p.conditions().hasPrimary();
            case StatusCondition.INFECT_ID -> p.conditions().hasPrimary();
            case StatusCondition.PARALYSIS_ID -> p.isType(Type.ELECTRIC) || p.conditions().hasPrimary();
            case StatusCondition.POISON_ID, StatusCondition.BAD_POISON_ID -> p.isType(Type.POISON) || p.isType(Type.STEEL) || p.conditions().hasPrimary();
            case StatusCondition.SLEEP_ID -> p.isType(Type.DIGITAL) || p.conditions().hasPrimary();
            case StatusCondition.FLINCH_ID -> p.conditions().hasMoved();
            case StatusCondition.BOUND_ID, StatusCondition.CONFUSION_ID -> p.conditions().hasKey(id);
            case StatusCondition.SEEDED_ID -> p.isType(Type.GRASS) || p.conditions().hasKey(id);
            default -> false;
        };
    }

    public static void applyCondition(EventData data, String id, double chance) {
        Pokemon p = data.effectTarget;
        data.statusChange = id;
        data.statusProb = chance;
        
        if (cannotApplyCondition(p, id) || p.conditions().fainted()) {
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
            case StatusCondition.FLY_ID -> flyState(data);
            case StatusCondition.DIG_ID -> digState(data);
            case StatusCondition.DIVE_ID -> diveState(data);
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        }  
    }

    public static void applyCondition(EventData data, String id) {
        applyCondition(data, id, 100);
    }

    // Semi-Immune State Function

    /*
     * Pokemon enters a semi-invulnerable state the first turn
     * Pokemon leaves the state and attacks on the second turn
     */
    public static void enterImmuneState(EventData data, String state) {
        Pokemon attacker = data.user;
        data.immuneStateChange = state;

        // Enters state
        if (!attacker.conditions().inImmuneState()) {
            applyCondition(data, state, 100);
            return;
        }

        attacker.conditions().removeCondition(data.immuneStateChange);
        MoveActionAttackDamage.dealDamage(data);
    }

    /*
     * Pokemon is knocked out of their semi-invulnerable state, interrupted
     */
    public static void leaveImmuneState(EventData data, String state, String message) {
        Pokemon p = data.attackTarget;
        data.immuneStateChange = StatusCondition.NO_INVUL_ID;
    
        if (p.conditions().fainted() || !p.conditions().hasKey(state)) {
            return;
        }

        p.conditions().removeCondition(state);
        p.conditions().setInterrupted(true);
        p.resetMove();
        BattleLog.add(message);
    }

}
