package project.game.move;

public class MovePhase {
    
    private int state; // Current phase of a move, cannot go below 0

    public MovePhase() {}

    /**
     * Returns true if in phase i
     * @param i phase
     */
    public boolean inPhase(int i) {
        return this.state == i;
    }

    /**
     * Advance move to its next phase. Once
     * the internal counter reaches zero,
     */
    public int nextPhase() {
        if (this.state < 0) {
            throw new IllegalStateException("Phase cannot be below 0");
        }
        if (this.state > 0) { // Decrease state by one
            this.state--;
        }
        return this.state;
    }

    public void setPhase(int i) {this.state = i;}
    public int getPhase() {return this.state;}
}
