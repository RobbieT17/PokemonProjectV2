package project.event;

import project.move.Move;
import project.pokemon.Pokemon;

public class EventManager {
    
    public final EventData eventData;
  
    public EventManager(Pokemon p1, Pokemon p2) {
        this.eventData = new EventData(p1, p2, null);
    }

    public EventManager(Pokemon p1, Pokemon p2, Move m) {
        this.eventData = new EventData(p1, p2, m);
    }

    public void notifyUserPokemon(String eventName) {
        this.eventData.user.getEvents().updateEvent(eventName, this.eventData);
    }

    public void notifyAttackTargetPokemon(String eventName) {
        this.eventData.attackTarget.getEvents().updateEvent(eventName, this.eventData);
    }

    public void notifyEffectTargetPokemon(String eventName) {
        this.eventData.effectTarget.getEvents().updateEvent(eventName, this.eventData);
    }

    public void notifyAllPokemon(String eventName) {
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
