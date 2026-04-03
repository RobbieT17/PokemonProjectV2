package project.data;

public class MoveData {
    
    private int id;
    private String name;
    private String type;
    private String category;
    private int pp;
    private int pow;
    private int acc;
    private int priority;
    private int crit;
    private boolean contact;

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setType(String type) {this.type = type;}
    public void setCategory(String category) {this.category = category;}
    public void setPp(int pp) {this.pp = pp;}
    public void setPow(int pow) {this.pow = pow;}
    public void setAcc(int acc) {this.acc = acc;}
    public void setPriority(int priority) {this.priority = priority;}
    public void setCrit(int crit) {this.crit = crit;}
    public void setContact(boolean contact) {this.contact = contact;}
    
    public int getId() {return id;}
    public String getName() {return name;}
    public String getType() {return type;}
    public String getCategory() {return category;}
    public int getPp() {return pp;}
    public int getPow() {return pow;}
    public int getAcc() {return acc;}
    public int getPriority() {return priority;}
    public int getCrit() {return crit;}
    public boolean isContact() {return contact;}
        
}
