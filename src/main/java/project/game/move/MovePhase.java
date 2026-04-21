package project.game.move;

public class MovePhase {
    
    private int state; // Current phase of a move, cannot go below 0

    public MovePhase() {}

    /**
     * Returns true if in phase i
     * @param i phase
     */
    public boolean equals(int i) {
        return this.state == i;
    }

    /**
     * Returns true if phase is less than i
     * @param i phase
     */
    public boolean lessThan(int i) {
        return this.state < i;
    }

    /**
     * Returns true if phase is more than i
     * @param i phase
     */
    public boolean moreThan(int i) {
        return this.state > i;
    }

    /**
     * Advance move to its next phase. Once
     * the internal counter reaches zero,
     */
    public int next() {
        if (this.state < 0) {
            throw new IllegalStateException("Phase cannot be below 0");
        }
        if (this.state > 0) { // Decrease state by one
            this.state--;
        }
        return this.state;
    }

    @Override
    public String toString() {
        return String.format("Phase: %d", this.state);
    }

    public void reset() {this.state = 0;}
    public void set(int i) {this.state = i;}
    public int get() {return this.state;}
}
