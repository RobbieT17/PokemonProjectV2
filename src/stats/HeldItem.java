package stats;

import event.EventData;
import event.GameEvent;
import move.Move;
import pokemon.Pokemon;

public class HeldItem extends Effect {

// Public Class Variables
    public static final String ASSAULT_VEST_ID = "Assault-Vest";


// Object
    public HeldItem(Pokemon p, String name, String[] flags) {
        super(p, name, flags);
    }

// Class Methods

    // Increases Pokemon's Sp.Def by 50% but disables Status Move (except Me First)
    public static HeldItem assaultVest(Pokemon p) {
        String name = HeldItem.ASSAULT_VEST_ID;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.DAMAGE_MULTIPLIER};

        p.events().addEventListener(flags[0], name, e -> {
            for (Move m : p.moves()) {
                if (m.isCategory(Move.STATUS)) m.disable();
            }
        });

        p.events().addEventListener(flags[1], name, e -> {
            if (!EventData.isTarget(p, e)) return;
            p.specialDefense().setMod(150);
        });

        return new HeldItem(p, name, flags);
	}

}
