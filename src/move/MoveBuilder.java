package move;

// Class designed to create Move objects efficiently
public class MoveBuilder {
    
    // Must Set
    private int id = 0;
    private String name = null;
    private String type = null;
    private String category = null;   
    private PowerPoints pp = null;
    private MoveAction action = null;

    // Optional (pow only if status moves)
    private int pow = 0;
    private int acc = 100;
    private int prot = 0;
    private double crit = Move.UNIVERSAL_CRIT_RATE;
    private boolean contact = false;

    /**
     * Converts into a Move
     * @throws IllegalStateException if required variables are not set
     * @return a new Move object
     */
    public Move buildMove() {
        if (this.id == 0) throw new IllegalStateException("Id not initialized");
        if (this.name == null) throw new IllegalStateException("Name not initialized");
        if (this.type == null) throw new IllegalStateException("Type not initialized");
        if (this.category == null) throw new IllegalStateException("Category not initialized");
        if (this.pp == null) throw new IllegalStateException("PP not initialized");
        if (this.action == null) throw new IllegalStateException("Action not initialized");
        if (!this.category.equals(Move.STATUS) && this.pow == 0) throw new IllegalStateException("Power not initialized for physical/special move");
        
        return new Move(
            this.id, 
            this.name, 
            this.type, 
            this.category, 
            this.crit, 
            this.pp, 
            this.pow, 
            this.acc, 
            this.prot, 
            this.contact, 
            this.action
            );
    }

// Setters
    public MoveBuilder setId(int i) {
        this.id = i;
        return this;
    }

    public MoveBuilder setName(String n) {
        this.name = n;
        return this;
    }

    public MoveBuilder setType(String t) {
        this.type = t;
        return this;
    }

    public MoveBuilder setCategory(String c) {
        this.category = c;

        // Physical Moves usually make contact, while Special and Status Moves don't
        if (c.equals(Move.PHYSICAL)) this.contact = true;

        return this;
    }

    public MoveBuilder setPP(int p) {
        this.pp = new PowerPoints(p);
        return this;
    }

    public MoveBuilder setPower(int p) {
        this.pow = p;
        return this;
    }

    public MoveBuilder setAccuracy(int a) {
        this.acc = a;
        return this;
    }

    public MoveBuilder setPriority(int p) {
        this.prot = p;
        return this;
    }

    public MoveBuilder setCritRatio(double c) {
        this.crit = c;
        return this;
    }

    public MoveBuilder setContact(boolean c) {
        this.contact = c;
        return this;
    }

    public MoveBuilder setAction(MoveAction a) {
        this.action = a;
        return this;
    }
}
