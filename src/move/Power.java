package move;

public class Power {
    
    private final int basePower;
    private int modified;
    private boolean doubled;

    public Power(int base) {
        this.basePower = base;
    }

    // Gets power and resets any modifications
    public int value() {
        int pow = this.modified != 0 
        ?  this.modified
        :  this.basePower;

        if (this.doubled) pow *= 2;

        this.doubled = false;
        this.modified = 0;
        return pow;
    }

    public void set(int i) {
        this.modified = i; 
    }

    public void doubled() {
        this.doubled = true;
    }
}
