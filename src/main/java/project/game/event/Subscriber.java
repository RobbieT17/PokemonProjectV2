package project.game.event;

public interface Subscriber {
    void addListener(String id, Observer o);
    void removeListener(String id);
    void update(EventData e);
    void update(); 
}
