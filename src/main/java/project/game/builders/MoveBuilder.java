package project.game.builders;

import project.data.AdditonalEffects;
import project.game.move.Move;
import project.game.move.Move.MoveCategory;
import project.game.move.Move.MoveTarget;
import project.game.move.MoveStat;
import project.game.move.PowerPoints;

public class MoveBuilder implements Builder {
    
    // Must Set
    private int id = 0;
    private String name = null;
    private String type = null;
    private MoveCategory category = null;   
    private PowerPoints pp = null;
    
    // Optional (pow if an only if status moves)
    
    private MoveStat pow = null;
    private MoveStat acc = new MoveStat(100);
    private MoveTarget target = MoveTarget.Single_Adjacent;
    private int prot = 0;
    private double crit = Move.UNIVERSAL_CRIT_RATE;
    private boolean contact = false;
    private boolean multiHit = false;
    private AdditonalEffects effects = null;

    @Override
    public void validateBuild() {
        if (this.id == 0) throw new IllegalStateException("Id not initialized");
        if (this.name == null) throw new IllegalStateException("Name not initialized");
        if (this.type == null) throw new IllegalStateException("Type not initialized");
        if (this.category == null) throw new IllegalStateException("Category not initialized");
        if (this.pp == null) throw new IllegalStateException("PP not initialized");
        if (!(this.category == Move.MoveCategory.Status) && this.pow == null) throw new IllegalStateException("Power not initialized for physical/special move");
    }

    /**
     * Converts into a Move
     * @throws IllegalStateException if required variables are not set
     * @return a new Move object
     */
    @Override
    public Move build() {
        validateBuild();
        
        return new Move(
            this.id, 
            this.name, 
            this.type, 
            this.category, 
            this.pp, 
            this.pow, 
            this.acc, 
            this.target, 
            this.crit, 
            this.prot, 
            this.contact, 
            this.multiHit, 
            this.effects
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

    public MoveBuilder setCategory(MoveCategory c) {
        this.category = c;

        // Physical Moves usually make contact, while Special and Status Moves don't
        if (this.category == MoveCategory.Physical) { 
            this.contact = true;
        }

        return this;
    }

    public MoveBuilder setPP(int p) {
        this.pp = new PowerPoints(p);
        return this;
    }

    public MoveBuilder setPower(int p) {
        this.pow = new MoveStat(p);
        return this;
    }

    public MoveBuilder setAccuracy(int a) {
        this.acc = new MoveStat(a);
        return this;
    }

    public MoveBuilder setTarget(MoveTarget t) {
        this.target = t;
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

    public MoveBuilder setMultiHit(boolean m) {
        this.multiHit = m;
        return this;
    }

    public MoveBuilder setAdditionEffects(AdditonalEffects e) {
        this.effects = e;
        return this;
    }

}
