package project.game.event;

import project.game.battle.BattleData;
import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;

public class EventManager {
    
    public final EventData data;
  
    public EventManager(BattleData data, Pokemon user) {
        this.data = new EventData(data, user, user.getTargetSelected(), user.getMoveSelected());
    }

    public void notifyUserPokemon(EventID id) {
        this.data.user.getEvents().updateEvent(id, this.data);
    }

    public void notifyAttackTargetPokemon(EventID id) {
        this.data.attackTarget.getEvents().updateEvent(id, this.data);
    }

    public void notifyEffectTargetPokemon(EventID id) {
        this.data.effectTarget.getEvents().updateEvent(id, this.data);
    }

    public void updateEventMaps() {
        this.data.user.getEvents().updateEventMaps();
    }

}
