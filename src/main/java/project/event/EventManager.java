package project.event;

import project.pokemon.Pokemon;

public class EventManager {
    
    public final EventData eventData;

    public EventManager(EventData e) {
        this.eventData = e;
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

    // Notifies user and attack target of an event
    public void notifyPokemon(String eventName) {
        this.notifyAttackTargetPokemon(eventName);
        this.notifyUserPokemon(eventName);
    }

    public void updateEventMaps() {
        eventData.user.getEvents().updateEventMaps();
        eventData.attackTarget.getEvents().updateEventMaps();
    }

}
