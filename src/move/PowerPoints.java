package move;
public class PowerPoints {

// Object Variables
    private final int maxPowerPoints; // max PP
    private int powerPoints; // current PP

// Constructor
    /**
     * Creates new PowerPoint object
     * @param pp max PP
     */
    public PowerPoints(int pp) {
        this.maxPowerPoints = pp;
        this.powerPoints = pp;
    }

// Methods
    // Increments PP (cannot exceed max)
    public void increment() {
        this.powerPoints++;
        if (this.powerPoints > this.maxPowerPoints) this.powerPoints = this.maxPowerPoints;
    }

    // Decrements PP (cannot go below 0)
    public void decrement() {
        this.powerPoints--;
        if (this.powerPoints < 0) this.powerPoints = 0;
    }

    // Out of PP when it reaches 0
    public boolean depleted() {
        return this.powerPoints == 0;
    }

    @Override
    public String toString() {
        return String.format("(%d/%d)", this.powerPoints, this.maxPowerPoints);
    }
}
