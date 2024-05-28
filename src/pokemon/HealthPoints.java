package pokemon;

public class HealthPoints {
    
    private final int maxHealthPoints;
    private int healthPoints;

    public HealthPoints(int health) {
        this.maxHealthPoints = health;
        this.healthPoints = health;
    }

    // Methods
    public void change(int value) {
        this.healthPoints += value;

        if (this.healthPoints > this.maxHealthPoints) this.healthPoints = this.maxHealthPoints;
        if (this.healthPoints < 0) this.healthPoints = 0;
    }

    @Override
    public String toString() {
        return String.format("%d / %d", this.healthPoints, this.maxHealthPoints);
    }

    // Getters
    public int maxValue() {
        return this.maxHealthPoints;
    }

    public int value() {
        return this.healthPoints;
    }
}