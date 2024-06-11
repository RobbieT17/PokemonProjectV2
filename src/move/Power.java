package move;

public class Power {
    
    private final int basePower;
    private boolean doubled;

    public Power(int base) {
        this.basePower = base;
    }

    // Gets power, if doubled, returns that value and sets doubled to false
    public int value() {
        int pow = this.doubled ? this.basePower * 2 : this.basePower;
        this.doubled = false;
        return pow;
    }

    public void doubled() {
        this.doubled = true;
    }
}
