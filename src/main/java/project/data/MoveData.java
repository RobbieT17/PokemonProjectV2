package project.data;

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

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setType(String type) {this.type = type;}
    public void setCategory(String category) {this.category = category; this.contact = category.equals(Move.PHYSICAL);}
    public void setPp(int pp) {this.pp = pp;}
    public void setPow(int pow) {this.pow = pow;}
    public void setAcc(int acc) {this.acc = acc;}
    public void setPriority(int priority) {this.priority = priority;}
    public void setCrit(double crit) {this.crit = crit;}
    public void setContact(boolean contact) {this.contact = contact;}
    
    public int getId() {return id;}
    public String getName() {return name;}
    public String getType() {return type;}
    public String getCategory() {return category;}
    public int getPp() {return pp;}
    public int getPow() {return pow;}
    public int getAcc() {return acc;}
    public int getPriority() {return priority;}
    public double getCrit() {return crit;}
    public boolean isContact() {return contact;}
        
}
