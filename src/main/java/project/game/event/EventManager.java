package project.game.event;

import project.game.battle.BattleData;
import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;

public class EventManager {
    
    public final EventData data;
  
    public EventManager(BattleData data, Pokemon user) {
        this.data = new EventData(
            data, 
            user, 
            user.getTargetSelected() != null ? user.getTargetSelected().getCurrentPokemon() : null, 
            user.getMoveSelected());
    }

    /**
     * Notifies the user Pokemon (the move user) of the specified event 
     * @param id Event ID
     */
    public void notifyUserPokemon(EventID id) {
        this.data.user.getEvents().updateEvent(id, this.data);
    }

    /**
     * Notifies the attack target Pokemon of the specified event
     * @param id Event ID
     */
    public void notifyAttackTargetPokemon(EventID id) {
        this.data.attackTarget.getEvents().updateEvent(id, this.data);
    }

    /**
     * Notifes the effect target Pokemon of the specified event.
     * @param id Event ID
     */
    public void notifyEffectTargetPokemon(EventID id) {
        this.data.effectTarget.getEvents().updateEvent(id, this.data);   
    }

    public void updateEventMaps() {
        this.data.user.getEvents().updateEventMaps();
    }

}
