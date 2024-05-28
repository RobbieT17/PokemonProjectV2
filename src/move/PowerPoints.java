package move;
public class PowerPoints {

    private final int maxPowerPoints;
    private int powerPoints;
    private boolean hasNoPowerPoints;

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
        if (this.powerPoints == 0) this.hasNoPowerPoints = true;
    }

    // Getters
    public int maxValue() {
        return this.maxPowerPoints;
    }

    public int value(){
        return this.powerPoints;
    }

    public boolean depleted() {
        return this.hasNoPowerPoints;
    }
}
