package project.game.pokemon.effects;

import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Effect;

public class StatusCondition extends Effect {
// Error Messages
    public static final String ID_ERR = "Invalid status condition ID";


    public StatusCondition(Pokemon p, String name, String[] flags) {
        super(p, name, flags);
    }

    @Override
    public void removeEffect() {
        this.getBearer().getEvents().removeEventListener(this.getFlags(), this.getEffectName());
    }
        
}
