package project.game.pokemon.effects;

import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;

public class HeldItem extends Effect {
    
    public HeldItem(Pokemon p, String name, EventID[] flags) {
        super(p, name, flags);
    }

    @Override
    public void removeEffect() {
        this.getBearer().getEvents().removeEventListener(this.getFlags(), this.getEffectName());
        this.getBearer().setItem(null);
    }

}
