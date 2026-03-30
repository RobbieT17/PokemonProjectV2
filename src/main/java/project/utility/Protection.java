package project.utility;

public class Protection {
    
// Variables
    private int count; // Consecutive usage
    private boolean isActive; // Condition is active


// Setters
    public void set() {
        if (RandomValues.chance(100 - (this.count * 33.3))) {
            this.isActive = true;
            this.count++;
            return;
        }
        this.reset();
    }

    public void reset() {
        this.isActive = false;
        this.count = 0;
    }

    public void setActive(boolean a) {
        this.isActive = a;
    }

// Getters
    public boolean isActive() {
        return this.isActive;
    }

}
