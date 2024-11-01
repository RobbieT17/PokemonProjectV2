package project.utility;

public class Protection {
    
// Variables
    private int count; // Consecutive usage
    private boolean active; // Condition is active


// Setters
    public void set() {
        if (RandomValues.chance(100 - (this.count * 33.3))) {
            this.active = true;
            this.count++;
            return;
        }
        this.reset();
    }

    public void reset() {
        this.active = false;
        this.count = 0;
    }

    public void setActive(boolean a) {
        this.active = a;
    }

// Getters
    public boolean active() {
        return this.active;
    }

}
