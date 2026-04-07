package project.game.move;

import project.data.AdditonalEffects;
import project.game.pokemon.Pokemon;
import project.game.pokemon.PokemonType;

public class Move {

// Class Variables
public enum MoveTarget {Single_Adjacent, Single_Ally, Single_Foe, All_Adjacent, All_Allies, All_Foes, All};
public enum MoveCategory {Physical, Special, Status};

    // Message when a move fails
    public static final String FAILED = "But it failed!";

    // Critical Hit Ratios
    public static final double UNIVERSAL_CRIT_RATE = 0.04;
    public static final double HIGH_CRIT = 0.125;
    public static final double ALWAYS_CRIT = 1.0;
    
    // Moves that always hit
    public static final int ALWAYS_HITS = 101; 
    
// Object Variables
    private final int moveID; // Unique identifier stored as int
    private final String moveName; // Name of the move
    private final String moveType; // Type of move
    private final MoveCategory category; // Three Categories (Physical, Special, Status)
    private final PowerPoints pp; // Power Points (The number of uses a move has)
	private final MoveStat power; // Strength of the move
	private final MoveStat accuracy; // Hit rate of the move
    private final MoveTarget moveTarget; // The target(s) of the move used

    private final double critRate; // Chance of a critical hit
    private final int priority; // Moves with higher priority always move first
    private final boolean makesContact; // True if the move makes contact with the defender
    private final boolean multiHit; // True if the move hits multiple times
    private final AdditonalEffects additonEffects; // Additional Effect such as stat/condition changes
        
    private boolean disabled; // Move is currently unusable

// Constructor
    // A move that is used by a Pokemon in battle
    public Move(
        int id, 
        String name,
        String type,
        MoveCategory category,
        PowerPoints pp,
        MoveStat pow, MoveStat acc, 
        MoveTarget target,
        double crit,
        int prot,
        boolean contact,
        boolean multiHit,
        AdditonalEffects additionalEffects
        ) {
        this.moveID = id;
        this.moveName = name;
        this.moveType = type;
        this.category = category;
        this.pp = pp;
        this.power = pow;
        this.accuracy = acc;
        this.moveTarget = target;
        this.critRate = crit; 
        this.priority = prot;
        this.makesContact = contact;
        this.multiHit = multiHit;
        this.additonEffects = additionalEffects;
    }

// Methods
    public static String noEffectOn(Pokemon p) {
        return String.format("But it doesn't affect %s...", p);
    }

    /**
     * @param t A Pokemon's typing
     * @return true if the move type is one of the Pokemon's types
     */
    public boolean isType(PokemonType t) {
        return (t.hasSecondaryType()) 
        ? this.moveType.equals(t.getPrimaryType().typeName())  || this.moveType.equals(t.getSecondaryType().typeName()) 
        : this.moveType.equals(t.getPrimaryType().typeName());
    }

    /**
     * @param t a type name
     * @return true if the move matches the type
     */
    public boolean isType(String t) {
        return this.moveType.equals(t);
    }

    public boolean isCategory(MoveCategory c) {
        return this.category.equals(c);
    }

    // Displays the move's name, type, and PP available
    public String moveStats() {
        return new StringBuilder()
        .append(String.format("%s <%s>: %s%n", this.moveName, this.moveType.toUpperCase(), this.pp))
        .toString();
    }

    public boolean equals(Move m) {
        return m != null 
        ? this.moveID == m.moveID
        : false;
    }

    @Override
    public String toString() {
        return this.moveName.replaceAll("[_-]", " ");
    }

    public void resetStats() {
        if (this.power != null) this.power.reset();
        this.accuracy.reset();
        this.disabled = false;
    }

// Setters
    public void setPower(int pow) {this.power.setPower(pow);}
    public void doublePower() {this.power.setMod(200);}
    public void changePowerByPercent(double pow) {this.power.setMod(pow);}
    public void setAccuracy(int acc) {this.accuracy.setPower(acc);}
    public void perfectAccuracy() {this.accuracy.setPower(Move.ALWAYS_HITS);}
    public void enable() {this.disabled = false;}
    public void disable() {this.disabled = true;}
    
// Getters
    public int getMoveID() {return this.moveID;}
    public String getMoveName() {return this.moveName;}
    public String getMoveType() {return this.moveType;}
    public MoveCategory getCategory() {return this.category;}
    public PowerPoints getPp() {return this.pp;}
    public int getPower() {return this.power != null ? this.power.power(): 0;}
    public int getAccuracy() {return this.accuracy.power();}
    public MoveTarget getMoveTarget() {return this.moveTarget;}
    public double getCritRate() {return this.critRate;}
    public int getPriority() {return this.priority;}
    public boolean getMakesContact() {return this.makesContact;}
    public boolean getMultiHit() {return this.multiHit;}
    public AdditonalEffects getAdditionalEffects() {return this.additonEffects;}
    public boolean isDisabled() {return this.disabled;}
}
