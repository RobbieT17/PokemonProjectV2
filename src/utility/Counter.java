package utility;

public class Counter {

// Variables
    private final int duration; // Number of rounds counter last
    private int count; // Current count
    private boolean terminated; // Once count exceeds the duration

// Constructor
    public Counter(int duration) {
        this.duration = duration;
        this.count = 0;
        this.terminated = false;
    }

// Methods
    // Increments the counter
    public void inc() {
        this.count++;
        if (this.duration == -1) return;
        if (this.count > this.duration) this.terminated = true;
    }

    // Resets counter to 0
    public void reset() {
        this.count = 0;
    }

// Getters
    public int count() {
        return this.count;
    }
    
    public boolean terminated() {
        return this.terminated;
    }
}
