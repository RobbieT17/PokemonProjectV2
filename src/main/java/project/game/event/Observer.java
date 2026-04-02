package project.game.event;

@FunctionalInterface
public interface Observer {
    void act(EventData data);
}
