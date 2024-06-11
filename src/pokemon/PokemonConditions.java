package pokemon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;
import stats.StatusCondition;
import utility.Counter;

public class PokemonConditions {
    
// Object Variables
    private boolean fainted; // When the Pokemon is unable to battle
    private boolean immobilized; // When the pokemon cannot act or dodge attacks
    private boolean forcedMove; // When the Pokemon charges up a move
    private boolean switchedIn; // Set to true when the pokemon first enters the field;
    private boolean hasMoved; // When the Pokemon has moved during the round
    private boolean flinched; // When the Pokemon cannot act for the turn

    private Counter rampageCount; // Count for rampage

    private StatusCondition primaryCondition; // Non-Volatile Condition (Burn, Freeze, Paralysis, Poison, Sleep)
    private final HashMap<Integer, StatusCondition> volatileConditions;

// Constructor
    public PokemonConditions() {
        this.volatileConditions = new HashMap<>();
    }

// Methods
    public boolean hasKey(int key) {
        return this.volatileConditions.containsKey(key);
    }

    public StatusCondition filterPrimaryCondition(boolean b) {
        return this.primaryCondition != null && this.primaryCondition.beforeMove() == b
        ? this.primaryCondition
        : null;
    }

    public Stream<StatusCondition> filterVolatileConditions(boolean b) {
        return Arrays.stream(this.volatileConditions.values().toArray(StatusCondition[]::new))
        .filter(c -> c.beforeMove() == b);
    }

    public boolean onRampage() {
        return this.rampageCount != null;
    }

// Setters
    public void setFainted(boolean f) {
        this.fainted = f;
    }
    public void setImmobilized(boolean i) {
        this.immobilized = i;
    }

    public void setForcedMove(boolean c) {
        this.forcedMove = c;
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

    public void startRampage(int duration) {
        this.rampageCount = new Counter(duration);
    }

    public void stopRampage() {
        this.rampageCount = null;
    }

    public void setPrimaryCondition(StatusCondition c) {
        this.primaryCondition = c;
    }

    public void clearPrimaryCondition() {
        this.primaryCondition = null;
    }

    public void add(StatusCondition condition) {
        this.volatileConditions.put(condition.id(), condition);
    }

    public void remove(int key) {
        this.volatileConditions.remove(key);
    }

    public void clearVolatileConditions() {
        this.volatileConditions.clear();
    }

// Getters
    public boolean immobilized() {
        return this.immobilized;
    }

    public boolean fainted() {
        return this.fainted;
    }

    public boolean forcedMove() {
        return this.forcedMove;
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

    public Counter rampage() {
        return this.rampageCount;
    }
   
    public StatusCondition primaryCondition() {
        return this.primaryCondition;
    }

    public HashMap<Integer, StatusCondition> volatileConditions() {
        return this.volatileConditions;
    }

}
