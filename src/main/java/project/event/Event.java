package project.event;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Event implements Subscriber {
    
    private final String eventId;
    private final HashMap<String, Observer> eventListeners;
    private final ArrayList<Map.Entry<String, Observer>> toAdd;
    private final ArrayList<String> toRemove;

    public Event(String id) {
        this.eventId = id;
        this.eventListeners = new HashMap<>();
        this.toAdd = new ArrayList<>();
        this.toRemove = new ArrayList<>();
    }

    // Methods
    public void updateMap() { // Puts and removes elements from the hashmap (I do this to avoid concurrent error)
        for (Entry<String, Observer> entry : this.toAdd) {
            this.eventListeners.put(entry.getKey(), entry.getValue());
        }

        for (String s : this.toRemove) {
            this.eventListeners.remove(s);
        }

        this.toAdd.clear();
        this.toRemove.clear();
    }

    @Override
    public void addListener(String id, Observer o) {
      this.toAdd.add(new AbstractMap.SimpleEntry<>(id, o));
    }

    @Override
    public void removeListener(String id) {
       this.toRemove.add(id);
    }

    @Override
    public void update() {
        for (Observer listener : this.eventListeners.values()) listener.act(null);
        updateMap();

    }

    @Override
    public void update(EventData e) {
        for (Observer listener : this.eventListeners.values()) listener.act(e);
        updateMap();
    }


    // Getters
    public String eventId() {return this.eventId;}
    public HashMap<String, Observer> eventListeners() {return this.eventListeners;}
}
