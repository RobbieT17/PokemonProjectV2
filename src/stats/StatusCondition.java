package stats;

public class StatusCondition {
    
    // Status Conditions IDs
    public static final int BURN = 0;
    public static final int FREEZE = 1;
    public static final int PARALYSIS = 2;
    public static final int POISON = 3;
    public static final int SLEEP = 4;

    // Object Method
    private final Effect[] nonVolatileConditions;
    private final Effect[] beforeMoveEffects;
    private final Effect[] afterMoveEffects;
    private final Effect[] otherEffects;

    public StatusCondition() {
        this.nonVolatileConditions = new Effect[] {
            new Effect(BURN),
            new Effect(FREEZE), 
            new Effect(PARALYSIS),
            new Effect(POISON),
            new Effect(SLEEP)
        };
        this.beforeMoveEffects = null;
        this.afterMoveEffects = null;
        this.otherEffects = null;
    }
}
