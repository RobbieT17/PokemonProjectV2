package project.pokemon;

public class HealthPoints {
// Object Variables
    private final int maxHealthPoints; // Max HP
    private int healthPoints; // Current HP

 // Constructor
    /**
     * Creates new HealthPoint object
     * @param health max HP
     */
    public HealthPoints(int health) {
        this.maxHealthPoints = health;
        this.healthPoints = health;
    }

// Methods

    // Pokemon's current HP is at the max
    public boolean atFullHP() {
        return this.healthPoints == this.maxHealthPoints;
    }

    // Change current HP by some value. Cannot exceed max or drop below 0
    public void change(int value) {
        this.healthPoints += value;

        if (this.healthPoints > this.maxHealthPoints) this.healthPoints = this.maxHealthPoints;
        if (this.healthPoints < 0) this.healthPoints = 0;
    }

    // Out of HP when it reaches 0
    public boolean depleted() {
        return this.healthPoints == 0;
    }

    @Override
    public String toString() {
        return String.format("%d / %d", this.healthPoints, this.maxHealthPoints);
    }

    // Getters
    public int max() {
        return this.maxHealthPoints;
    }

    public int value() {
        return this.healthPoints;
    }
}