package project.game.event;

import java.util.HashMap;

public class GameEvents {

    public enum EventID {
        MOVE_HITS,
        STAT_CHANGE,
        STATUS_CONDITION_CHANGE,
        SWITCH_IN,
        MOVE_MAKES_CONTACT,
        END_OF_TURN,
        END_OF_ROUND,
        DAMAGE_MULTIPLIER,
        MOVE_EFFECTIVENESS,
        FIND_MOVE_ORDER,
        WEATHER_EFFECT,
        MOVE_ACCURACY,
        PRIMARY_STATUS_BEFORE,
        STATUS_BEFORE,
        BEFORE_MOVE,
        SWITCH_OUT,
        MOVE_SELECTION,
        USE_MOVE,
        MOVE_INTERRUPTED,
        MOVE_DEALS_DAMAGE;
    }

    private final HashMap<String, Event> eventMap;
   
    public GameEvents() {
        this.eventMap = new HashMap<>();

        this.addEventMapEntry(EventID.MOVE_HITS);
        this.addEventMapEntry(EventID.STATUS_CONDITION_CHANGE);
        this.addEventMapEntry(EventID.SWITCH_IN);
        this.addEventMapEntry(EventID.MOVE_MAKES_CONTACT);
        this.addEventMapEntry(EventID.END_OF_TURN);
        this.addEventMapEntry(EventID.END_OF_ROUND);
        this.addEventMapEntry(EventID.DAMAGE_MULTIPLIER);
        this.addEventMapEntry(EventID.MOVE_EFFECTIVENESS);
        this.addEventMapEntry(EventID.FIND_MOVE_ORDER);
        this.addEventMapEntry(EventID.WEATHER_EFFECT);
        this.addEventMapEntry(EventID.MOVE_ACCURACY);
        this.addEventMapEntry(EventID.PRIMARY_STATUS_BEFORE);
        this.addEventMapEntry(EventID.STATUS_BEFORE);
        this.addEventMapEntry(EventID.BEFORE_MOVE);
        this.addEventMapEntry(EventID.SWITCH_OUT);
        this.addEventMapEntry(EventID.MOVE_SELECTION);
        this.addEventMapEntry(EventID.USE_MOVE);
        this.addEventMapEntry(EventID.MOVE_INTERRUPTED);
        this.addEventMapEntry(EventID.MOVE_DEALS_DAMAGE);
    }

    // Methods
    private void addEventMapEntry(EventID eventId) {
        String name = eventId.name();
        this.eventMap.put(name, new Event(name));
    }

    public void updateEvent(EventID eventId, EventData data) {
        this.eventMap.get(eventId.name()).update(data);
    }

    public void addEventListener(EventID eventId, String effectId, Observer e) {
        this.eventMap.get(eventId.name()).addListener(effectId, e);
    }

    public void removeEventListener(EventID eventId, String effectId) {
        this.eventMap.get(eventId.name()).removeListener(effectId);
    }

    public void removeEventListener(EventID[] eventIds, String effectId) {
        for (EventID eventId : eventIds) {
            this.removeEventListener(eventId, effectId);
        }
    }

    public void updateEventMaps() {
        for (Event e : this.eventMap.values()) {
            e.updateMap();
        }
    }
}
