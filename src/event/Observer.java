package event;

@FunctionalInterface
public interface Observer {
    void act(EventData data);
}

