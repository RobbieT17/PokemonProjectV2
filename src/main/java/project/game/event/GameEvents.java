package project.game.event;

import java.util.HashMap;

public class GameEvents {

    public enum EventID {
        MOVE_SELECTION,
        FIND_MOVE_ORDER,
        USE_MOVE,
        MOVE_INTERRUPTED,  
        PRIMARY_STATUS_BEFORE,
        STATUS_BEFORE,
        BEFORE_MOVE,
        PRIMARY_STATUS_AFTER,
        STATUS_AFTER,
        END_OF_TURN,
        END_OF_ROUND,
        WEATHER_EFFECT,
        SWITCH_IN,
        SWITCH_OUT,
        ATK_MOVE_HITS,
        ATK_STAT_CHANGE,
        ATK_STATUS_CONDITION_CHANGE,
        ATK_MOVE_MAKES_CONTACT,
        ATK_DAMAGE_MULTIPLIER,
        ATK_MOVE_EFFECTIVENESS,
        ATK_MOVE_ACCURACY,
        ATK_MOVE_DEALS_DAMAGE,
        DEF_MOVE_HITS,
        DEF_STAT_CHANGE,
        DEF_STATUS_CONDITION_CHANGE,
        DEF_MOVE_MAKES_CONTACT,
        DEF_DAMAGE_MULTIPLIER,
        DEF_MOVE_EFFECTIVENESS,
        DEF_MOVE_ACCURACY,
        DEF_MOVE_DEALS_DAMAGE,
    }

    private final HashMap<String, Event> eventMap;
   
    public GameEvents() {
        this.eventMap = addEventMapEntries();
    }

    // Methods
    private HashMap<String, Event> addEventMapEntries() {
        HashMap<String, Event> map = new HashMap<>();
    
        for (EventID event : EventID.values()) {
            String name = event.name();
            map.put(name, new Event(name));
        }  

        return map;     
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
