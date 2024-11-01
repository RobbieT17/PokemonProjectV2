package project.move;

public class MoveStat {

// Object
    private final int base;
    private int power;
    private double mod;

    public MoveStat(int base){
        this.base = base;
        this.power = base;
        this.mod = 1.0;
    }

// Methods
    public void reset() {
        this.power = this.base;
        this.mod = 1.0;
    }

// Setters
    public void setPower(int p) {this.power = p;}
    public void setMod(double m) {this.mod = 0.01 * m;}

// Getters
    public int base() {return this.base;}
    public int power() {return (int) (this.power * this.mod);}   
}
