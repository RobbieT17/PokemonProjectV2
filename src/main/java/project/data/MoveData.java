package project.data;

import java.util.ArrayList;

import project.game.move.Move;

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
    private String target = "Single-Adjacent";
    private ArrayList<Double> multiHit = null;
    private AdditonalEffects effects = null;

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name.replaceAll("[ -]", "_");}
    public void setType(String type) {this.type = type;}
    public void setCategory(String category) {this.category = category; this.contact = category.equals(Move.PHYSICAL);}
    public void setPp(int pp) {this.pp = pp;}
    public void setPow(int pow) {this.pow = pow;}
    public void setAcc(int acc) {this.acc = acc;}
    public void setPriority(int priority) {this.priority = priority;}
    public void setCrit(double crit) {this.crit = crit;}
    public void setContact(boolean contact) {this.contact = contact;}
    public void setTarget(String target) {this.target = target;}
    public void setMultiHit(ArrayList<Double> multiHit) {this.multiHit = multiHit;}
    public void setEffects(AdditonalEffects effects) {this.effects = effects;}
    
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
    public ArrayList<Double> getMultiHit() {return this.multiHit;}
    public AdditonalEffects getEffects(){return this.effects;}         
}
