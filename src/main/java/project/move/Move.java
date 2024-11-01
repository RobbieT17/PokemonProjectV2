package project.move;

import project.pokemon.Pokemon;
import project.pokemon.PokemonType;

public class Move {
// Class Variables
    // Message when a move fails
    public static final String FAILED = "But it failed!";

    // Critical Hit Ratios
    public static final double UNIVERSAL_CRIT_RATE = 0.04;
    public static final double HIGH_CRIT = 0.125;
    public static final double ALWAYS_CRIT = 1.0;
    
    // Moves that always hit
    public static final int ALWAYS_HITS = 101; 
    
    // Move Categories
    public static final String PHYSICAL = "Physical";
    public static final String SPECIAL = "Special";
    public static final String STATUS = "Status";

// Object Variables
    private final int moveID; // Unique identifier stored as int
    private final String moveName; // Name of the move
    private final String moveType; // Type of move
    private final String category; // Three Categories (Physical, Special, Status)

    private final double critRate; // Chance of a critical hit

    private final PowerPoints pp; // Power Points (The number of uses a move has)
	private final MoveStat power; // Strength of the move
	private final MoveStat accuracy; // Hit rate of the move
	private final int priority; // Moves with higher priority always move first
    private final boolean makesContact;
    
    private final MoveAction action; // What the move does (ex. Deal damage, apply status effect, etc.)
    
    private boolean disabled; // Move is currently unusable

// Constructor
    // A move that is used by a Pokemon in battle
    public Move(
        int id, 
        String name,
        String type,
        String category,
        double crit,
        PowerPoints pp,
        MoveStat pow, MoveStat acc, 
        int prot,
        boolean contact,
        MoveAction action
        ) {
        this.moveID = id;
        this.moveName = name;
        this.moveType = type;
        this.category = category;
        this.critRate = crit;
        this.pp = pp;
        this.power = pow;
        this.accuracy = acc;
        this.priority = prot;
        this.makesContact = contact;
        this.action = action;
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
        ? this.moveType.equals(t.primaryType().typeName())  || this.moveType.equals(t.secondaryType().typeName()) 
        : this.moveType.equals(t.primaryType().typeName());
    }

    /**
     * @param t a type name
     * @return true if the move matches the type
     */
    public boolean isType(String t) {
        return this.moveType.equals(t);
    }

    public boolean isCategory(String c) {
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
        return this.moveName;
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
    public int moveID() {return this.moveID;}
    public String moveName() {return this.moveName;}
    public String moveType() {return this.moveType;}
    public String category() {return this.category;}
    public double critRate() {return this.critRate;}
    public PowerPoints pp() {return this.pp;}
    public int power() {return this.power != null ? this.power.power(): 0;}
    public int accuracy() {return this.accuracy.power();}
    public int priority() {return this.priority;}
    public boolean makesContact() {return this.makesContact;}
    public MoveAction action() {return this.action;}
    public boolean disabled() {return this.disabled;}
}
