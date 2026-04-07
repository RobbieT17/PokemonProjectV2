package project.game.builders;

public interface Builder {
    void validateBuild(); // Confirms the builder has all required features before building
    Object build(); // Builds the object
}
