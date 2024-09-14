package event;

public interface Subscriber {
    void addListener(Observer o);
    void removeListener(Observer o);
    void update(EventData e);
}
