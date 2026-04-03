package project.game.pokemon.effects;

import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;

public abstract class Effect {
    
    private final String effectName; // Name of the effect
    private final EventID[] flags; // Events the effect triggers on
    private final Pokemon bearer; // Pokemon with this effect

    public Effect(Pokemon p, String name, EventID[] flag) {
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
    public EventID[] getFlags() {return this.flags;}
    public Pokemon getBearer() {return this.bearer;}
}
