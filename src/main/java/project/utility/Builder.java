package project.utility;

public interface Builder {
    void validBuild(); // Confirms the builder has all required features before building
    Object build(); // Builds the object
}
