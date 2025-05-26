package project.stats;

import project.event.EventData;
import project.event.GameEvent;
import project.battle.BattleField;
import project.battle.BattleLog;
import project.exceptions.MoveInterruptedException;
import project.move.Move;
import project.pokemon.Pokemon;
import project.utility.Counter;

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
        this.bearer().events().removeEventListener(this.flags(), this.effectName());
        this.bearer().setItem(null);
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

    // Poison Type restore 1/16 of their Max HP, all other types lose that amount
    public static HeldItem blackSludge(Pokemon p) {
        String name = HeldItem.BLACK_SLUDGE_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND};

        p.events().addEventListener(flags[0], name, e -> {
            double percent = 1.0 / 16.0;
            if (p.isType(Type.POISON)) {
                p.restoreHpPercentMaxHP(percent, " from its Black Sludge");
                return;
            }
            p.takeDamagePercentMaxHP(percent, " from its Black Sludge");
        });

        return new HeldItem(p, name, flags);
	}

    // Blows up all Pokemon on field after 5 rounds, timer resets if user switches out
    public static HeldItem bombSurprise(Pokemon p) {
        String name = HeldItem.BOMB_SURPRISE_ID;
        String[] flags = new String[] {GameEvent.BEFORE_MOVE, GameEvent.SWITCH_OUT};

        Counter c = new Counter(5);

        p.events().addEventListener(flags[0], name, e -> {
            if (c.inc()) {
                BattleLog.add("Surprise! %s's Bomb Surprise goes off!", p);
                BattleField.player1.pokemonInBattle().faints();
                BattleField.player2.pokemonInBattle().faints();
                
                throw new MoveInterruptedException();
            }
        });

        p.events().addEventListener(flags[1], name, e -> c.reset());

        return new HeldItem(p, name, flags);
	}

    // Boosts Attack stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceBand(Pokemon p) {
        String name = HeldItem.CHOICE_BAND_ID;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.DAMAGE_MULTIPLIER};

        p.events().addEventListener(flags[0], name, e -> {
            if (p.firstRound()) return;
            p.setMove(p.firstMove());
        });

        p.events().addEventListener(flags[1], name, e -> p.attack().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Boosts Speed stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceScarf(Pokemon p) {
        String name = HeldItem.CHOICE_SCARF_ID;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.FIND_MOVE_ORDER};

        p.events().addEventListener(flags[0], name, e -> {
            if (p.firstRound()) return;
            p.setMove(p.firstMove());
        });

        p.events().addEventListener(flags[1], name, e -> p.speed().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Boosts Sp-Attack stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceSpecs(Pokemon p) {
        String name = HeldItem.CHOICE_SPECS_ID;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.DAMAGE_MULTIPLIER};

        p.events().addEventListener(flags[0], name, e -> {
            if (p.firstRound()) return;
            p.setMove(p.firstMove());
        });

        p.events().addEventListener(flags[1], name, e -> p.specialAttack().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Restore 1/16 of Max HP at the end of each round
    public static HeldItem leftovers(Pokemon p) {
        String name = HeldItem.LEFTOVERS_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND};

        p.events().addEventListener(flags[0], name, e -> p.restoreHpPercentMaxHP(1.0 / 16.0, " from its Leftovers"));

        return new HeldItem(p, name, flags);
	}

    // Boosts physical-move power by 10%
    public static HeldItem muscleBand(Pokemon p) {
        String name = HeldItem.MUSCLE_BAND_ID;
        String[] flags = new String[] {GameEvent.DAMAGE_MULTIPLIER};

        p.events().addEventListener(flags[0], name, e -> {
            if (!(EventData.isUser(p, e) && e.moveUsed.isCategory(Move.PHYSICAL))) return;
            e.otherMoveMods *= 1.1;
        });

        return new HeldItem(p, name, flags);
	}

    // Contact with this Pokemon causes the attack to lose 1/6 of its max HP
    public static HeldItem rockyHelmet(Pokemon p) {
        String name = HeldItem.ROCKY_HELMET_ID;
        String[] flags = new String[] {GameEvent.MOVE_MAKES_CONTACT};

        p.events().addEventListener(flags[0], name, e -> {
            if (!EventData.isTarget(p, e)) return;

            e.user.takeDamagePercentMaxHP(1.0 / 6.0, String.format(" from %s's Rocky Helmet", p));
        });

        return new HeldItem(p, name, flags);
	}

    // Boosts special-move power by 10%
    public static HeldItem wiseGlasses(Pokemon p) {
        String name = HeldItem.WISE_GLASSES_ID;
        String[] flags = new String[] {GameEvent.DAMAGE_MULTIPLIER};

        p.events().addEventListener(flags[0], name, e -> {
            if (!(EventData.isUser(p, e) && e.moveUsed.isCategory(Move.SPECIAL))) return;
            e.otherMoveMods *= 1.1;
        });

        return new HeldItem(p, name, flags);
	}    

}
