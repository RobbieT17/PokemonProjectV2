package pokemon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;
import stats.StatusCondition;
import utility.Counter;
import utility.Protection;

public class PokemonConditions {
    
// Object Variables 

    // TODO: Put in alphabetical order
    private boolean fainted; // Pokemon is unable to battle
    private boolean immobilized; // Pokemon cannot act or dodge attacks
    private boolean forcedMove; // Pokemon is forced to use their last move
    private boolean focused; // Pokemon concentrates, resets if disrupted
    private boolean recharging; // Pokemon waits a turn before acting again
    private boolean switchedIn; // Pokemon first enters the field
    private boolean hasMoved; // Pokemon has moved during the round
    private boolean interrupted; // Pokemon's last move was interrupted
    private boolean grounded; // Pokemon is grounded, vulnerable to Ground-Type moves 
    private boolean flinched; // Pokemon cannot act for the turn

    private int immuneState; // Pokemon is immune from most attacks for a turn
  
    // Likely fails when used consecutively
    private final Protection protect; // Protects Pokemon from incoming attacks
    private final Protection endured; // Prevents knocking Pokemon's HP to 0

    private Counter rampage; // Count for rampage

    private StatusCondition primaryCondition; // Non-Volatile Condition (Burn, Freeze, Paralysis, Poison, Sleep)
    private final HashMap<Integer, StatusCondition> volatileConditions;


// Constructor
    public PokemonConditions() {
        this.volatileConditions = new HashMap<>();
        this.protect = new Protection();
        this.endured = new Protection();
    }

// Methods
    public boolean inImmuneState() {
        return this.immuneState != StatusCondition.NO_INVUL;
    }

    public boolean hasImmuneState(int s) {
        return this.immuneState == s;
    }

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
        return this.rampage != null;
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

    public void setFocused(boolean f) {
        this.focused = f;
    }

    public void setRecharging(boolean r) {
        this.recharging = r;
    }

    public void setSwitchedIn(boolean s) {
        this.switchedIn = s;
    }

    public void setHasMoved(boolean h) {
        this.hasMoved = h;
    }

    public void setInterrupted(boolean i) {
        this.interrupted = i;
    }

    public void setGrounded(boolean g) {
        this.grounded = g;
    }

    public void setFlinched(boolean f) {
        this.flinched = f;
    } 

    public void setImmuneState(int s) {
        this.immuneState = s;
    }

    public void startRampage(int duration) {
        this.rampage = new Counter(duration);
    }

    public void stopRampage() {
        this.rampage = null;
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

    public void clearAtEndRound() {
        this.hasMoved = false;
        this.flinched = false;
    }

    public void clearAtReturn() {
        this.setForcedMove(false);
        this.setSwitchedIn(false);
        this.protect.reset();
        this.endured.reset();
        this.clearVolatileConditions();
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

    public boolean focused() {
        return this.focused;
    }

    public boolean recharging() {
        return this.recharging;
    }

    public boolean switchedIn() {
        return this.switchedIn;
    }

    public boolean hasMoved() {
        return this.hasMoved;
    }

    public boolean interrupted() {
        return this.interrupted;
    }

    public boolean grounded() {
        return this.grounded;
    }

    public boolean flinched() {
        return this.flinched;
    }

    public int immuneState() {
        return this.immuneState;
    }

    public Protection protect() {
        return this.protect;
    }

    public Protection endured() {
        return this.endured;
    }

    public Counter rampage() {
        return this.rampage;
    }
   
    public StatusCondition primaryCondition() {
        return this.primaryCondition;
    }

    public HashMap<Integer, StatusCondition> volatileConditions() {
        return this.volatileConditions;
    }

}
