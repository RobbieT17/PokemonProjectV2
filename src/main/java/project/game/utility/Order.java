package project.game.utility;

public class Order {
    
    private final int priority;
    private final int value;
    private final double tiebreaker;

    private final Object ref;

    public Order(int priority, int value) {
        this.priority = priority;
        this.value = value;
        this.tiebreaker = RandomValues.randomDouble();
        this.ref = null;
    }

    public Order(int priority, int value, Object ref) {
        this.priority = priority;
        this.value = value;
        this.tiebreaker = RandomValues.randomDouble();
        this.ref = ref;
    }

    public int getPriority() {return this.priority;}
    public int getValue() {return this.value;}
    public double getTiebreaker() {return this.tiebreaker;}
    public Object getRef() {return this.ref;}

}
