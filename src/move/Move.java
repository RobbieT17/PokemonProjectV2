package move;

import pokemon.PokemonType;

public class Move {
    // Class Variables
    // Message when a move fails
    public static final String FAILED = "But it failed!";

    public static final double UNIVERSAL_CRIT_RATE = 0.04;
    public static final double HIGH_CRIT = 0.125;
    public static final double ALWAYS_CRIT = 1.0;
    public static final int INF = 101; // Moves that always hit
    
    // Move Categories
    public static final String PHYSICAL = "Physical";
    public static final String SPECIAL = "Special";
    public static final String STATUS = "Status";

    // Object Variables
    private final int moveID;
    private final String moveName;
    private final String moveType;
    private final String category;

    private final double critRate;

    private final PowerPoints pp;
	private final int power;
	private final int accuracy;
	private final int priority; // Moves with higher priority always move first
    private final boolean makesContact;
    
    private final MoveAction action;

    public Move(
        int id, 
        String name,
        String type,
        String category,
        double crit,
        PowerPoints pp,
        int pow, int acc, int prot,
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
    public boolean isType(PokemonType t) {
        return (t.hasSecondaryType()) 
        ? this.moveType.equals(t.primaryType().typeName())  || this.moveType.equals(t.secondaryType().typeName()) 
        : this.moveType.equals(t.primaryType().typeName());
    }

    public boolean isType(String t) {
        return this.moveType.equals(t);
    }

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
        return this.power;
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
