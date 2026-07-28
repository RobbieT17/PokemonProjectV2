package project.game.move.moveactions;

import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusCondition.StatusConditionID;
import project.game.pokemon.effects.StatusContext;
import project.game.utility.RandomValues;

public class MoveActionChangeCondition {

    private final EventManager eventManager;

    public MoveActionChangeCondition(EventManager e) {
        this.eventManager = e;
    }
    
// Status Conditions Functions
    public void applyCondition(StatusConditionID id, double chance) {
        EventData data  = this.eventManager.data;
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

        this.eventManager.notifyUserPokemon(EventID.ATK_STATUS_CONDITION_CHANGE); 
        this.eventManager.notifyEffectTargetPokemon(EventID.DEF_STATUS_CONDITION_CHANGE);
            
        data.statusFailed = !c.applyEffectToTarget();
    }

}
