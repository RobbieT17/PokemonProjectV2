package project.game.event;

import java.util.HashMap;

public class GameEvents {

    public static final String MOVE_HITS = "Move-Hits";
    public static final String STAT_CHANGE = "Stat-Change";
    public static final String STATUS_CONDITION_CHANGE = "Status-Condition-Change";
    public static final String SWITCH_IN = "Switch-In";
    public static final String MOVE_MAKES_CONTACT = "Move-Makes-Contact";
    public static final String END_OF_TURN = "End-Of-Turn";
    public static final String END_OF_ROUND = "End-Of-Round";
    public static final String DAMAGE_MULTIPLIER = "Damage-Multiplier";
    public static final String MOVE_EFFECTIVENESS = "Move-Effectiveness";
    public static final String FIND_MOVE_ORDER = "Find-Move-Order";
    public static final String WEATHER_EFFECT = "Weather-Effect";
    public static final String MOVE_ACCURACY = "Move-Accuracy";
    public static final String PRIMARY_STATUS_BEFORE = "Primary-Status-Effect-Before";
    public static final String STATUS_BEFORE = "Status-Effect-Before";
    public static final String BEFORE_MOVE = "Before-Move";
    public static final String SWITCH_OUT = "Switch-Out";
    public static final String MOVE_SELECTION = "Move-Selection";
    public static final String USE_MOVE = "Use-Move";
    public static final String MOVE_INTERRUPTED = "Move-Interrupted";

    private final HashMap<String, Event> eventMap;
   
    public GameEvents() {
        this.eventMap = new HashMap<>();

        this.addEventMapEntry(GameEvents.MOVE_HITS);
        this.addEventMapEntry(GameEvents.STATUS_CONDITION_CHANGE);
        this.addEventMapEntry(GameEvents.SWITCH_IN);
        this.addEventMapEntry(GameEvents.MOVE_MAKES_CONTACT);
        this.addEventMapEntry(GameEvents.END_OF_TURN);
        this.addEventMapEntry(GameEvents.END_OF_ROUND);
        this.addEventMapEntry(GameEvents.DAMAGE_MULTIPLIER);
        this.addEventMapEntry(GameEvents.MOVE_EFFECTIVENESS);
        this.addEventMapEntry(GameEvents.FIND_MOVE_ORDER);
        this.addEventMapEntry(GameEvents.WEATHER_EFFECT);
        this.addEventMapEntry(GameEvents.MOVE_ACCURACY);
        this.addEventMapEntry(GameEvents.PRIMARY_STATUS_BEFORE);
        this.addEventMapEntry(GameEvents.STATUS_BEFORE);
        this.addEventMapEntry(GameEvents.BEFORE_MOVE);
        this.addEventMapEntry(GameEvents.SWITCH_OUT);
        this.addEventMapEntry(GameEvents.MOVE_SELECTION);
        this.addEventMapEntry(GameEvents.USE_MOVE);
        this.addEventMapEntry(GameEvents.MOVE_INTERRUPTED);
    }

    // Methods
    private void addEventMapEntry(String eventName) {
        this.eventMap.put(eventName, new Event(eventName));
    }

    public void updateEvent(String eventName, EventData data) {
        this.eventMap.get(eventName).update(data);
    }

    public void addEventListener(String eventName, String id, Observer e) {
        this.eventMap.get(eventName).addListener(id, e);
    }

    public void removeEventListener(String eventName, String id) {
        this.eventMap.get(eventName).removeListener(id);
    }

    public void removeEventListener(String[] eventNames, String id) {
        for (String name : eventNames) {
            this.removeEventListener(name, id);
        }
    }

    public void updateEventMaps() {
        for (Event e : this.eventMap.values()) {
            e.updateMap();
        }
    }
}
