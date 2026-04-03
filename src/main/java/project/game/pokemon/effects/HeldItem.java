package project.game.pokemon.effects;

import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Effect;

public class HeldItem extends Effect {
    
    public HeldItem(Pokemon p, String name, String[] flags) {
        super(p, name, flags);
    }

    @Override
    public void removeEffect() {
        this.getBearer().getEvents().removeEventListener(this.getFlags(), this.getEffectName());
        this.getBearer().setItem(null);
    }

}
