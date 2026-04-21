package project.game.move.moveactions;

import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusCondition;
import project.game.pokemon.effects.StatusConditionManager;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.effects.StatusContext;
import project.game.pokemon.stats.Type;
import project.game.utility.RandomValues;

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

    private static void applyGrounded(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.grounded(c));
    }

    private static void applyForcedMove(StatusContext c) {
        c.target.getConditions().addCondition(StatusConditionManager.forcedMove(c));
    }

    private static boolean cannotApplyCondition(Pokemon p, StatusConditionID id) {
        return switch (id) {
            case StatusConditionID.Burn -> p.isType(Type.Fire) || p.getConditions().hasPrimary();
            case StatusConditionID.Freeze -> p.isType(Type.Ice) || p.getConditions().hasPrimary();
            case StatusConditionID.Infect -> p.isType(Type.Zombie) || p.getConditions().hasPrimary();
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
        
        // Checks if
        if (cannotApplyCondition(p, id)) {
            data.statusFailed = true;
            data.failMessage = Move.FAILED;
            return;
        }

        // Rolls for hit-chance
        if (!RandomValues.chance(chance)) { // Bad Roll, condition not applied
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
            case StatusConditionID.Grounded -> applyGrounded(c);
            case StatusConditionID.Forced_Move -> applyForcedMove(c);
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR + id);
        }  
    }

    public static void applyCondition(EventManager eventManager, StatusConditionID id) {
        applyCondition(eventManager, id, 100);
    }

}
