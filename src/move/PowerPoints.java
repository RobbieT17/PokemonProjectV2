package move;
public class PowerPoints {

    private final int maxPowerPoints;
    private int powerPoints;

    public PowerPoints(int pp) {
        this.maxPowerPoints = pp;
        this.powerPoints = pp;
    }

    // Methods
    public void increment() {
        this.powerPoints++;
        if (this.powerPoints > this.maxPowerPoints) this.powerPoints = this.maxPowerPoints;
    }

    public void decrement() {
        this.powerPoints--;
        if (this.powerPoints < 0) this.powerPoints = 0;
    }

    @Override
    public String toString() {
        return String.format("(%d/%d)", this.powerPoints, this.maxPowerPoints);
    }

    // Getters
    public boolean depleted() {
        return this.powerPoints == 0;
    }
}
