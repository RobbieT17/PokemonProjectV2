package project.stats;

import project.pokemon.Pokemon;

public abstract class Effect {
    
    private final String effectName; // Name of the effect
    private final String[] flags; // Events the effect triggers on
    private final Pokemon bearer; // Pokemon with this effect

    public Effect(Pokemon p, String name, String[] flag) {
        this.effectName = name;
        this.flags = flag;
        this.bearer = p;
    }

    public abstract void removeEffect();

    @Override
    public String toString() {
        return this.effectName;
    }

// Getters
    public String getEffectName() {return this.effectName;}
    public String[] getFlags() {return this.flags;}
    public Pokemon getBearer() {return this.bearer;}
}
