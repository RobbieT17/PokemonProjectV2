package move;

public class Accuracy {
    
    private final int base;
    private int modified;
    private boolean perfect;

    public Accuracy(int base) {
        this.base = base;
    }

    public int value() {
        int acc = this.perfect 
        ? Move.ALWAYS_HITS
        : this.modified != 0 ? this.modified : this.base;

        this.modified = 0;
        this.perfect = false;
        return acc;
    }

    public void set(int i) {
        this.modified = i;
    }

    public void perfect() {
        this.perfect = true;
    }
}
