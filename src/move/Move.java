package move;

import pokemon.PokemonType;

public class Move {
// Class Variables
    // Message when a move fails
    public static final String FAILED = "But it failed!";

    // Critical Hit Ratios
    public static final double UNIVERSAL_CRIT_RATE = 0.04;
    public static final double HIGH_CRIT = 0.125;
    public static final double ALWAYS_CRIT = 1.0;
    public static final int INF = 101; // Moves that always hit
    
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
	private final Power power; // Strength of the move
	private final int accuracy; // Hit rate of the move
	private final int priority; // Moves with higher priority always move first
    private final boolean makesContact;
    
    private final MoveAction action; // What the move does (ex. Deal damage, apply status effect, etc.)

// Constructor
    // A move that is used by a Pokemon in battle
    public Move(
        int id, 
        String name,
        String type,
        String category,
        double crit,
        PowerPoints pp,
        Power pow, 
        int acc, int prot,
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
    // Doubles current power
    public void doublePower() {
        this.power.doubled();
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

    // Displays the move's name, type, and PP available
    public String moveStats() {
        return new StringBuilder()
        .append(String.format("%s <%s>: %s%n", this.moveName, this.moveType.toUpperCase(), this.pp))
        .toString();
    }

    @Override
    public String toString() {
        return this.moveName;
    }

// Getters
    public int moveID() {
        return this.moveID;
    }

    public String moveName() {
        return this.moveName;
    }

    public String moveType() {
        return this.moveType;
    }

    public String category() {
        return this.category;
    }

    public double critRate() {
        return this.critRate;
    }

    public PowerPoints pp() {
        return this.pp;
    }

    public int power() {
        return this.power.value();
    }

    public int accuracy() {
        return this.accuracy;
    }

    public int priority() {
        return this.priority;
    }

    public boolean makesContact() {
        return this.makesContact;
    }

    public MoveAction action() {
        return this.action;
    }
}
