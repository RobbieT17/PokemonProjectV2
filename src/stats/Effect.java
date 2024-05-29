package stats;

public class Effect {
    
    private final int effectId;
    private final boolean hasCounter;
    private final EffectAction action;

    private boolean active;
    private Counter counter;
   
    // Constructors
    public Effect(int id) {
        this.effectId = id;
        this.hasCounter = false;
        this.action = EffectAction.getAction(id);
    }

    public Effect(int id, boolean count) {
        this.effectId = id;
        this.hasCounter = count;
        this.action = EffectAction.getAction(id);
    }

    // Methods
    public void activate() {
        this.active = true;
        if (this.hasCounter) this.counter = new Counter();
    }

    public void activate(int c) {
        this.active = true;
        if (this.hasCounter) this.counter = new Counter(c);
    }

    // Getters
    public int effectId() {
        return this.effectId;
    }

    public boolean hasCounter() {
        return this.hasCounter;
    }

    public EffectAction action(){
        return this.action;
    }

    public boolean active() {
        return this.active;
    }

    public Counter counter() {
        return this.counter;
    }

}
