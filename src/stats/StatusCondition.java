package stats;


public class StatusCondition {

    // Status Conditions IDs
    public static final int BURN = 0;
    public static final int FREEZE = 1;
    public static final int PARALYSIS = 2;
    public static final int POISON = 3;
    public static final int SLEEP = 4;

    public static final int FLINCH = 5;

    // Object Variables
    private final int id;
    private final StatusAction action;
    private final boolean beforeMove;

    // Constructor
    public StatusCondition(int id, StatusAction action, boolean before) {
        this.id = id;
        this.beforeMove = before;
        this.action = action;
    }

    // Methods
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
