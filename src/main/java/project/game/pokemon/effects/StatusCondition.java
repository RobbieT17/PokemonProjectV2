package project.game.pokemon.effects;

import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Effect;

public class StatusCondition extends Effect {
// Error Messages
    public static final String ID_ERR = "Invalid status condition ID";

// Public Class Methods

    // Non-Volatile
    public static final String BURN_ID = "BURNED";
    public static final String FREEZE_ID = "FROZEN";
    public static final String INFECT_ID = "INFECTED";
    public static final String PARALYSIS_ID = "PARALYZED";
    public static final String POISON_ID = "POISONED";
    public static final String BAD_POISON_ID = "BADLY POISONED";
    public static final String SLEEP_ID = "ASLEEP";

    // Volatile
    public static final String FLINCH_ID = "Flinched";
    public static final String BOUND_ID = "Trapped";
    public static final String CONFUSION_ID = "Confused";
    public static final String SEEDED_ID = "Seeded";
    public static final String FORCED_MOVE_ID = "Forced Move";
    public static final String FOCUSED_ID = "Focused";
    public static final String RAMPAGE_ID = "Rampage";
    public static final String RECHARGE_ID = "Recharge";
    public static final String GROUNDED_ID = "Grounded";
    public static final String CHARGE_MOVE = "Charge Move";

    // Semi-Invulnerable State
    public static final String NO_INVUL_ID = "Normal State";
    public static final String FLY_ID = "Flying State";
    public static final String DIG_ID = "Underground State";
    public static final String DIVE_ID = "Underwater State";

    // Protection
    public static final String PROTECT_ID = "Protect";
    
// Object
    public StatusCondition(Pokemon p, String name, String[] flags) {
        super(p, name, flags);
    }

    @Override
    public void removeEffect() {
        this.getBearer().getEvents().removeEventListener(this.getFlags(), this.getEffectName());
    }
        
}
