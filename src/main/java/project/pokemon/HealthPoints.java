package project.pokemon;

public class HealthPoints {
// Object Variables
    private final int maxHealthPoints; // Max HP
    private int currentHealthPoints; // Current HP

 // Constructor
    /**
     * Creates new HealthPoint object
     * @param health max HP
     */
    public HealthPoints(int health) {
        this.maxHealthPoints = health;
        this.currentHealthPoints = health;
    }

// Methods

    // Pokemon's current HP is at the max
    public boolean atFullHP() {
        return this.currentHealthPoints == this.maxHealthPoints;
    }

    // Change current HP by some value. Cannot exceed max or drop below 0
    public void change(int value) {
        this.currentHealthPoints += value;

        if (this.currentHealthPoints > this.maxHealthPoints) this.currentHealthPoints = this.maxHealthPoints;
        if (this.currentHealthPoints < 0) this.currentHealthPoints = 0;
    }

    // Out of HP when it reaches 0
    public boolean depleted() {
        return this.currentHealthPoints == 0;
    }

    @Override
    public String toString() {
        return String.format("%d / %d", this.currentHealthPoints, this.maxHealthPoints);
    }

    // Getters
    public int getMaxHealthPoints() {
        return this.maxHealthPoints;
    }

    public int getCurrentHealthPoints() {
        return this.currentHealthPoints;
    }
}