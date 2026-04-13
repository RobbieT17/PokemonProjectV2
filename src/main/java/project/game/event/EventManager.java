package project.game.event;

import project.game.battle.BattleData;
import project.game.event.GameEvents.EventID;
import project.game.move.Move;
import project.game.pokemon.Pokemon;

public class EventManager {
    
    public final BattleData battleData;
    public final EventData eventData;
  
    public EventManager(BattleData data, Pokemon p1, Pokemon p2) {
        this.battleData = data;
        this.eventData = new EventData(data, p1, p2, null);
    }

    public EventManager(BattleData data, Pokemon p1, Pokemon p2, Move m) {
        this.battleData = data;
        this.eventData = new EventData(data, p1, p2, m);
    }

    public void notifyUserPokemon(EventID id) {
        this.eventData.user.getEvents().updateEvent(id, this.eventData);
    }

    public void notifyAttackTargetPokemon(EventID id) {
        this.eventData.attackTarget.getEvents().updateEvent(id, this.eventData);
    }

    public void notifyEffectTargetPokemon(EventID id) {
        this.eventData.effectTarget.getEvents().updateEvent(id, this.eventData);
    }

    public void notifyAllPokemon(EventID eventName) {
        for (Pokemon p : this.eventData.pokemonList) {
            p.getEvents().updateEvent(eventName, this.eventData);
        }
    }

    public void updateEventMaps() {
        for (Pokemon p : this.eventData.pokemonList) {
            p.getEvents().updateEventMaps();
        }
    }

}
