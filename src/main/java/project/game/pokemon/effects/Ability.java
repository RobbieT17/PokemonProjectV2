package project.game.pokemon.effects;

import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;

public class Ability extends Effect {
 
    public Ability(Pokemon p, String name, EventID[] flags) {
        super(p, name, flags);
    }

    @Override
    public void removeEffect() {
        this.getBearer().getEvents().removeEventListener(this.getFlags(), this.getEffectName());
        this.getBearer().setAbility(null);
    }
    
}
