package project.game.event;

import project.game.battle.BattleData;
import project.game.battle.BattleLog;
import project.game.event.GameEvents.EventID;
import project.game.move.Move.MoveCategory;
import project.game.pokemon.Pokemon;

public class EventManager {
    
    public final EventData data;

    public EventManager(BattleData data, Pokemon user) {
        this.data = new EventData(data, user, null, user.getMoveSelected());
    }
  
    public EventManager(BattleData data, Pokemon user, Pokemon target) {
        this.data = new EventData(data, user, target, user.getMoveSelected());
    }

    /**
     * Logs failed Status moves
     */
    public void logFailMessage() {
        if (this.data.moveUsed.isCategory(MoveCategory.Status)) {
            BattleLog.add(this.data.failMessage);
        }
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
