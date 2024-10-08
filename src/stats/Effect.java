package stats;

import pokemon.Pokemon;

public abstract class Effect {
    
    private final String effectName; // Name of the effect
    private final String[] flags; // Events the effect triggers on
    private final Pokemon bearer; // Pokemon with this effect

    public Effect(Pokemon p, String name, String[] flag) {
        this.effectName = name;
        this.flags = flag;
        this.bearer = p;
    }

    @Override
    public String toString() {
        return this.effectName;
    }

    public void removeEffect() {
        this.bearer.events().removeEventListener(this.flags, this.effectName);
    }


    public String effectName() {return this.effectName;}
    public String[] flags() {return this.flags;}
    public Pokemon bearer() {return this.bearer;}
}
