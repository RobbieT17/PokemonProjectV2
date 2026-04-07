package project.data;


import project.game.move.Move;
import project.game.move.Move.MoveCategory;
import project.game.move.Move.MoveTarget;
import project.game.move.MoveStat;
import project.game.move.PowerPoints;

public class MoveData {
    
    private int id;
    private String name;
    private String type;
    private String category;
    private int pp;
    private int pow = -1;
    private int acc = 100;
    private int priority;
    private double crit = Move.UNIVERSAL_CRIT_RATE;
    private boolean contact = false;
    private String target = Move.MoveTarget.Single_Adjacent.name();
    private boolean multiHit = false;
    private AdditonalEffects addEffects = null;

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name.replaceAll("[ -]", "_");}
    public void setType(String type) {this.type = type;}
    public void setCategory(String category) {this.category = category; this.contact = category.equals(Move.MoveCategory.Physical.name());}
    public void setPp(int pp) {this.pp = pp;}
    public void setPow(int pow) {this.pow = pow;}
    public void setAcc(int acc) {this.acc = acc;}
    public void setPriority(int priority) {this.priority = priority;}
    public void setCrit(double crit) {this.crit = crit;}
    public void setContact(boolean contact) {this.contact = contact;}
    public void setTarget(String target) {this.target = target.replaceAll("[ -]", "_");}
    public void setMultiHit(boolean multiHit) {this.multiHit = multiHit;}
    public void setAddEffects(AdditonalEffects addEffects) {this.addEffects = addEffects;}
    
    public int getId() {return this.id;}
    public String getName() {return this.name;}
    public String getType() {return this.type;}
    public String getCategory() {return this.category;}
    public int getPp() {return this.pp;}
    public int getPow() {return this.pow;}
    public int getAcc() {return this.acc;}
    public int getPriority() {return this.priority;}
    public double getCrit() {return this.crit;}
    public boolean getContact() {return this.contact;}
    public String getTarget() {return this.target;}
    public boolean getMultiHit() {return this.multiHit;}
    public AdditonalEffects getAddEffects(){return this.addEffects;}      
    
    // Wrapper Functions
    public MoveCategory getMoveCategory() {return MoveCategory.valueOf(this.category);}
    public MoveTarget getMoveTarget() {return MoveTarget.valueOf(this.target);}
    public PowerPoints getPowerPoints() {return new PowerPoints(this.pp);}
    public MoveStat getPower() {return new MoveStat(this.pow);}
    public MoveStat getAccuracy() {return new MoveStat(this.acc);}
}
