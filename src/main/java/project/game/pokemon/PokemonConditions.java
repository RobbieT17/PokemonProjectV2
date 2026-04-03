package project.game.pokemon;

import java.util.HashMap;

import project.game.pokemon.effects.StatusCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.utility.Protection;

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
        this.hasKey(StatusConditionID.FLY_ID) ||
        this.hasKey(StatusConditionID.DIG_ID) ||
        this.hasKey(StatusConditionID.DIVE_ID);
    }

    public boolean hasKey(StatusConditionID key) {
        return this.conditions.containsKey(key.name());
    }

// Methods
    public void clearPrimary() {
        if (this.primaryCondition == null) return;
        this.primaryCondition.removeEffect();
        this.primaryCondition = null;
    }

    public void addCondition(StatusCondition condition) {
        this.conditions.put(condition.getEffectName(), condition);
    }

    public void removeCondition(StatusConditionID key) {
        if (!this.conditions.containsKey(key.name())) return;
        this.conditions.get(key.name()).removeEffect();
        this.conditions.remove(key.name());
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
    public boolean isImmobilized() {return this.immobilized;}
    public boolean isFainted() {return this.fainted;}
    public boolean isSwitchedIn() {return this.switchedIn;}
    public boolean hasMoved() {return this.hasMoved;}
    public boolean isInterrupted() {return this.interrupted;}
    public boolean isRecharge() {return this.recharge;}
    public boolean tookDamage() {return this.tookDamage;}

    public Protection getProtect() {return this.protect;}
    public Protection getEndure() {return this.endure;}

    public StatusCondition getPrimaryCondition() {return this.primaryCondition;}
    public HashMap<String, StatusCondition> getVolatileConditions() {return this.conditions;}

}
