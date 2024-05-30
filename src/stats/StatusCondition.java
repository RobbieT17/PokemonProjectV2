package stats;


public class StatusCondition {

    // Status Conditions IDs
    public static final int BURN = 0;
    public static final int FREEZE = 1;
    public static final int PARALYSIS = 2;
    public static final int POISON = 3;
    public static final int SLEEP = 4;

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
