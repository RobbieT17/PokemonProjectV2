package stats;


public class StatusCondition {

// Public Class Variables
    // Status Conditions IDs
    public static final int BURN = 0;
    public static final int FREEZE = 1;
    public static final int PARALYSIS = 2;
    public static final int POISON = 3;
    public static final int SLEEP = 4;

    public static final int FLINCH = 5;
    public static final int CONFUSION = 6;
    public static final int SEEDED = 7;

// Object Variables
    private final int id; // Unique Identifier 
    private final StatusAction action; // The effect
    private final boolean beforeMove; // If the effect is applied before the Pokemon moves

// Constructor
    public StatusCondition(int id, StatusAction action, boolean beforeMove) {
        this.id = id;
        this.beforeMove = beforeMove;
        this.action = action;
    }

// Methods
    // Message displayed if the Pokemon already has a condition
    public static String failMessage(int id) {
        return switch (id) {
            case BURN -> "burned";
            case FREEZE -> "frozen";
            case PARALYSIS -> "paralyzed";
            case POISON -> "poisoned";
            case SLEEP -> "asleep";
            default -> throw new IllegalArgumentException("Invalid status condition id");
        };
    }

    @Override
    public String toString() {
        return switch (this.id){
            case BURN -> "BURNED";
            case FREEZE -> "FROZEN";
            case PARALYSIS -> "PARALYZED";
            case POISON -> "POISONED";
            case SLEEP -> "ASLEEP";
            case CONFUSION -> "Confused";
            case SEEDED -> "Seeded";
            default -> throw new IllegalArgumentException("Invalid status condition id");
        };
    }

// Getters
    public int id() {
        return this.id;
    }

    public StatusAction action() {
        return this.action;
    }

    public boolean beforeMove() {
        return this.beforeMove;
    }

}
