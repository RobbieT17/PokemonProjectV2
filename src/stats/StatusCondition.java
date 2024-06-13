package stats;


public class StatusCondition {

// Public Class Variables
    // Error Messages
    public static final String ID_ERR = "Invalid status condition ID";

    // Status Conditions IDs
    public static final int BURN = 0;
    public static final int FREEZE = 1;
    public static final int PARALYSIS = 2;
    public static final int POISON = 3;
    public static final int SLEEP = 4;

    public static final int FLINCH = 5;
    public static final int CONFUSION = 6;
    public static final int SEEDED = 7;


    // Semi-Invulnerable IDs
    public static final int NO_INVUL = -1;
    public static final int FLY = -2;
    public static final int DIG = -3;

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
            case StatusCondition.BURN -> "burned";
            case StatusCondition.FREEZE -> "frozen";
            case StatusCondition.PARALYSIS -> "paralyzed";
            case StatusCondition.POISON -> "poisoned";
            case StatusCondition.SLEEP -> "asleep";
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        };
    }

    // Message displayed when the condition expires
    public static String expireMessage(int id) {
        return switch (id) {
            case StatusCondition.FREEZE -> " thawed!";
            case StatusCondition.SLEEP -> " woke up!";
            case StatusCondition.CONFUSION -> " snapped out of confusion!";
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        };
    }

    @Override
    public String toString() {
        return switch (this.id){
            case StatusCondition.BURN -> "BURNED";
            case StatusCondition.FREEZE -> "FROZEN";
            case StatusCondition.PARALYSIS -> "PARALYZED";
            case StatusCondition.POISON -> "POISONED";
            case StatusCondition.SLEEP -> "ASLEEP";
            case StatusCondition.CONFUSION -> "Confused";
            case StatusCondition.SEEDED -> "Seeded";
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
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
