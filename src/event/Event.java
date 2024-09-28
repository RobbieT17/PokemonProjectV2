package event;

import java.util.HashMap;

public class Event implements Subscriber {
    
    private final String eventId;
    private final HashMap<String, Observer> eventListeners;

    public Event(String id) {
        this.eventId = id;
        this.eventListeners = new HashMap<>();
    }

    // Methods
    @Override
    public void addListener(String id, Observer o) {
        eventListeners.put(id, o);
    }

    @Override
    public void removeListener(String id) {
        eventListeners.remove(id);
    }

    @Override
    public void update() {
        for (Observer listener : this.eventListeners.values()) listener.act(null);
    }

    @Override
    public void update(EventData e) {
        for (Observer listener : this.eventListeners.values()) listener.act(e);
    }

    // Getters
    public String eventId() {return this.eventId;}
    public HashMap<String, Observer> eventListeners() {return this.eventListeners;}
}
