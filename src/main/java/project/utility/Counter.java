package project.utility;

public class Counter {

// Variables
    private final int duration; // Number of rounds counter last
    private int count; // Current count

// Constructor
    public Counter() {
        this.duration = -1;
    }

    public Counter(int duration) {
        if (duration <= 0) throw new IllegalArgumentException("Duration must be a positive number");
        this.duration = duration;
    }

// Methods
    // Increments the counter, returns true if terminated
    public boolean inc() { 
        this.count++;
        return this.count > this.duration;
    }

    // Resets counter to 0
    public void reset() {
        this.count = 0;
    }

// Getters
    public int getCount() {
        return this.count;
    }

}
