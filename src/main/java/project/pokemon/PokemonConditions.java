package project.pokemon;

import java.util.HashMap;

import project.stats.StatusCondition;
import project.utility.Protection;

public class PokemonConditions {
    
// Object Variables 

    // TODO: Put in alphabetical order
    private boolean fainted; // Pokemon is unable to battle
    private boolean immobilized; // Pokemon cannot act or dodge attacks
    private boolean switchedIn; // Pokemon first enters the field
    private boolean hasMoved; // Pokemon has moved during the round
    private boolean interrupted; // Pokemon's last move was interrupted
    private boolean recharge; // Pokemon has to recharge before moving again
    private boolean tookDamage; // Pokemon took damage during the round

    private Protection protect; 
    private Protection endure;

    private StatusCondition primaryCondition; // Non-Volatile Condition (Burn, Freeze, Paralysis, Poison, Sleep)
    private final HashMap<String, StatusCondition> conditions; // All other conditions


// Constructor
    public PokemonConditions() {
        this.conditions = new HashMap<>();
        this.protect = new Protection();
        this.endure = new Protection();
    }

// Methods
    public boolean hasPrimary() {
        return this.primaryCondition != null;
    }

    public boolean inImmuneState() {
        return 
        this.conditions.containsKey(StatusCondition.FLY_ID) ||
        this.conditions.containsKey(StatusCondition.DIG_ID) ||
        this.conditions.containsKey(StatusCondition.DIVE_ID);
    }

    public boolean hasKey(String key) {
        return this.conditions.containsKey(key);
    }


// Methods
    public void clearPrimary() {
        if (this.primaryCondition == null) return;
        this.primaryCondition.removeEffect();
        this.primaryCondition = null;
    }

    public void addCondition(StatusCondition condition) {
        this.conditions.put(condition.effectName(), condition);
    }

    public void removeCondition(String key) {
        if (!this.conditions.containsKey(key)) return;
        this.conditions.get(key).removeEffect();
        this.conditions.remove(key);
    }

    public void clearVolatileConditions() {
        for (StatusCondition c : this.conditions.values()) c.removeEffect();
        this.conditions.clear();
    }

    public void clearAtReturn() {
        this.setSwitchedIn(false);
        this.clearVolatileConditions();
    }

// Setters
    public void setFainted(boolean f) {this.fainted = f;}
    public void setImmobilized(boolean i) {this.immobilized = i;}
    public void setSwitchedIn(boolean s) {this.switchedIn = s;}
    public void setHasMoved(boolean h) {this.hasMoved = h;}
    public void setInterrupted(boolean i) {this.interrupted = i;}
    public void setRecharge(boolean r) {this.recharge = r;}
    public void setTookDamage(boolean d) {this.tookDamage = d;}
    public void setProtect(Protection p) {this.protect = p;}
    public void setEndure(Protection p) {this.endure = p;}
    public void setPrimaryCondition(StatusCondition c) {this.primaryCondition = c;}

// Getters
    public boolean immobilized() {return this.immobilized;}
    public boolean fainted() {return this.fainted;}
    public boolean switchedIn() {return this.switchedIn;}
    public boolean hasMoved() {return this.hasMoved;}
    public boolean interrupted() {return this.interrupted;}
    public boolean recharge() {return this.recharge;}
    public boolean tookDamage() {return this.tookDamage;}

    public Protection protect() {return this.protect;}
    public Protection endure() {return this.endure;}

    public StatusCondition primaryCondition() {return this.primaryCondition;}
    public HashMap<String, StatusCondition> volatileConditions() {return this.conditions;}

}
