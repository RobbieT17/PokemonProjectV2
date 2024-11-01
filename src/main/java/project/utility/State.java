package project.utility;

public class State {

// Class Variables
    public static final String INVALID = "In invalid State";
    
// Object
    private int num;
    private boolean bool;

    public State() {
        // Num = 0, Bool = false
    }

    public State(int n) {
        this.num = n;
    }

    public State(boolean b) {
        this.bool = b;
    }

    public State(int n, boolean b) {
        this.num = n;
        this.bool = b;
    }
 
// Methods
    public void set(int n) {this.num = n;}
    public void set(boolean b) {this.bool = b;}
    public int getInt() {return this.num;};
    public boolean getBool() {return this.bool;}
}
