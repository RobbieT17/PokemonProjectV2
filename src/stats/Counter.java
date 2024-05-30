package stats;

public class Counter {

    // Variables
    private final int duration;
    private int count;
    private boolean terminated;

    // Constructor
    public Counter(int duration) {
        this.duration = duration;
        this.count = 0;
        this.terminated = false;
    }

    // Methods
    public void inc() {
        this.count++;
        if (this.duration == -1) return;
        if (this.count > this.duration) this.terminated = true;
    }

    // Getters
    public int count() {
        return this.count;
    }
    
    public boolean terminated() {
        return this.terminated;
    }
}
