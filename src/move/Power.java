package move;

public class Power {
    
    private final int base;
    private int modified;
    private double ratio;

    public Power(int base) {
        this.base = base;
        this.ratio = 1.0;
    }

    // Gets power and resets any modifications
    public int value() {
        int pow = this.modified != 0 
        ?  this.modified
        :  this.base;

        pow *= this.ratio;

        this.modified = 0;
        this.ratio = 1.0;
        return pow;
    }

    public void set(int i) {
        this.modified = i; 
    }

    public void ratio(double r) {
        this.ratio = 0.01 * r;
    }
}
