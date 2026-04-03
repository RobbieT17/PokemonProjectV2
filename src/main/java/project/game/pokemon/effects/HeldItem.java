package project.game.pokemon.effects;

import project.game.battle.BattleField;
import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.GameEvents;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Effect;
import project.game.utility.Counter;

public class HeldItem extends Effect {

// Public Class Variables
    public static final String ASSAULT_VEST_ID = "Assault-Vest";
    public static final String BLACK_SLUDGE_ID = "Black-Sludge";
    public static final String BOMB_SURPRISE_ID = "Bomb-Surprise";
    public static final String CHOICE_BAND_ID = "Choice-Band";
    public static final String CHOICE_SCARF_ID = "Choice-Band";
    public static final String CHOICE_SPECS_ID = "Choice-Band";
    public static final String LEFTOVERS_ID = "Leftovers";
    public static final String MUSCLE_BAND_ID = "Muscle-Band";
    public static final String ROCKY_HELMET_ID = "Rocky-Helmet";
    public static final String WISE_GLASSES_ID = "Wise-Glasses";
    
 
// Object
    public HeldItem(Pokemon p, String name, String[] flags) {
        super(p, name, flags);
    }

    @Override
    public void removeEffect() {
        this.getBearer().getEvents().removeEventListener(this.getFlags(), this.getEffectName());
        this.getBearer().setItem(null);
    }

}
