package pokemon;

import stats.StatusCondition;

public class PokemonConditions {
    
// Object Variables
    private boolean fainted; // When the Pokemon is unable to 
    private boolean immobilized; // When the pokemon cannot act or dodge attacks
    private boolean charged; // When the Pokemon charges up a move
    private boolean switchedIn; // Set to true when the pokemon first enters the field;
    private boolean hasMoved; // When the Pokemon has moved during the round
    private boolean flinched; // When the Pokemon cannot act for the turn

    private StatusCondition primaryCondition; // Non-Volatile Condition (Does not clear when the Pokemon switches)

// Setters
    public void setFainted(boolean f) {
        this.fainted = f;
    }
    public void setImmobilized(boolean i) {
        this.immobilized = i;
    }

    public void setCharge(boolean c) {
        this.charged = c;
    }

    public void setSwitchedIn(boolean s) {
        this.switchedIn = s;
    }

    public void setHasMoved(boolean h) {
        this.hasMoved = h;
    }

    public void setFlinched(boolean f) {
        this.flinched = f;
    }

    public void setPrimaryCondition(StatusCondition c) {
        this.primaryCondition = c;
    }

    public void clearPrimaryCondition() {
        this.primaryCondition = null;
    }

// Getters
    public boolean immobilized() {
        return this.immobilized;
    }

    public boolean fainted() {
        return this.fainted;
    }

    public boolean charged() {
        return this.charged;
    }

    public boolean switchedIn() {
        return this.switchedIn;
    }

    public boolean hasMoved() {
        return this.hasMoved;
    }

    public boolean flinched() {
        return this.flinched;
    }
   
    public StatusCondition primaryCondition() {
        return this.primaryCondition;
    }
}
