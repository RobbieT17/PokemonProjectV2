package event;

import java.util.ArrayList;

public class Event implements Subscriber {
    
    private final String eventId;
    private final ArrayList<Observer> eventListeners;

    public Event(String id) {
        this.eventId = id;
        this.eventListeners = new ArrayList<>();
    }

    // Methods
    @Override
    public void addListener(Observer o) {
        eventListeners.add(o);
    }

    @Override
    public void removeListener(Observer o) {
        eventListeners.remove(o);
    }

    @Override
    public void update() {
        for (Observer listener : this.eventListeners) listener.act(null);
    }

    @Override
    public void update(EventData e) {
        for (Observer listener : this.eventListeners) listener.act(e);
    }

    // Getters
    public String eventId() {return this.eventId;}
    public ArrayList<Observer> eventListeners() {return this.eventListeners;}
}
