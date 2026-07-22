package project.game.move.moveactions;

import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusCondition.StatusConditionID;
import project.game.pokemon.effects.StatusContext;
import project.game.utility.RandomValues;

public interface MoveActionChangeCondition extends MoveAction {
    
// Status Conditions Functions
    public static void applyCondition(EventManager eventManager, StatusConditionID id, double chance) {
        EventData data  = eventManager.data;
        Pokemon p = data.effectTarget;

        data.statusProb = chance;
        
        // Rolls for hit-chance
        if (!RandomValues.chance(chance)) { // Bad Roll, condition not applied
            data.statusFailed = true;
            return;
        }

        StatusContext c = new StatusContext(id, p);
        c.source = data.attackUser.getPosition();
        c.target2 = data.attackUser;
        c.isSlave = false;

        data.statusContext = c;

        eventManager.notifyUserPokemon(EventID.ATK_STATUS_CONDITION_CHANGE); 
        eventManager.notifyEffectTargetPokemon(EventID.DEF_STATUS_CONDITION_CHANGE);
            
        data.statFailed = !c.applyEffectToTarget();
    }

    public static void applyCondition(EventManager eventManager, StatusConditionID id) {
        applyCondition(eventManager, id, 100);
    }

    public static void applyCondition(EventManager eventManager, String name) {
        applyCondition(eventManager, StatusConditionID.valueOf(name));
    }

}
