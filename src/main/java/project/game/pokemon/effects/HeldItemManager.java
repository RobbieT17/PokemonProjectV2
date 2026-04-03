package project.game.pokemon.effects;

import java.util.function.Function;

import project.game.battle.BattleField;
import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Type;
import project.game.utility.Counter;

public interface HeldItemManager {

    public enum HeldItemID {
        ASSAULT_VEST_ID(HeldItemManager::assaultVest),
        BLACK_SLUDGE_ID(HeldItemManager::blackSludge),
        BOMB_SURPRISE_ID(HeldItemManager::bombSurprise),
        CHOICE_BAND_ID(HeldItemManager::choiceBand),
        CHOICE_SCARF_ID(HeldItemManager::choiceScarf),
        CHOICE_SPECS_ID(HeldItemManager::choiceSpecs),
        LEFTOVERS_ID(HeldItemManager::leftovers),
        MUSCLE_BAND_ID(HeldItemManager::muscleBand),
        ROCKY_HELMET_ID(HeldItemManager::rockyHelmet),
        WISE_GLASSES_ID(HeldItemManager::wiseGlasses);

        private final Function<Pokemon, HeldItem> func;

        HeldItemID(Function<Pokemon, HeldItem> func) {
            this.func = func;
        }    

        public HeldItem apply(Pokemon p) {
            return this.func.apply(p);
        }
    }
    
    // Increases Pokemon's Sp.Def by 50% but disables Status Move (except Me First)
    public static HeldItem assaultVest(Pokemon p) {
        String name = HeldItemID.ASSAULT_VEST_ID.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            for (Move m : p.getMoves()) {
                if (m.isCategory(Move.STATUS)) m.disable();
            }
        });

        p.getEvents().addEventListener(flags[1], name, e -> {
            if (!EventData.isTarget(p, e)) return;
            p.getSpecialDefense().setMod(150);
        });

        return new HeldItem(p, name, flags);
	}

    // Poison Type restore 1/16 of their Max HP, all other types lose that amount
    public static HeldItem blackSludge(Pokemon p) {
        String name = HeldItemID.BLACK_SLUDGE_ID.name();
        EventID[] flags = new EventID[] {EventID.END_OF_ROUND};

        p.getEvents().addEventListener(flags[0], name, e -> {
            double percent = 1.0 / 16.0;
            if (p.isType(Type.POISON)) {
                p.restoreHpPercentMaxHP(percent, " from its Black Sludge");
                return;
            }
            p.takeDamagePercentMaxHP(percent, " from its Black Sludge");
        });

        return new HeldItem(p, name, flags);
	}

    // Blows up all Pokemon on field after 3 rounds. Timer resets if the holder switches out or is attacked.
    public static HeldItem bombSurprise(Pokemon p) {
        String name = HeldItemID.BOMB_SURPRISE_ID.name();
        EventID[] flags = new EventID[] {EventID.BEFORE_MOVE, EventID.SWITCH_OUT};

        Counter c = new Counter(5);

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (c.inc()) {
                BattleLog.add("Surprise! %s's Bomb Surprise goes off!", p);
                BattleField.player1.getPokemonInBattle().faints();
                BattleField.player2.getPokemonInBattle().faints();
                
                throw new MoveInterruptedException();
            }
        });

        p.getEvents().addEventListener(flags[1], name, e -> c.reset());

        return new HeldItem(p, name, flags);
	}

    // Boosts Attack stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceBand(Pokemon p) {
        String name = HeldItemID.CHOICE_BAND_ID.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (p.firstRound()) return;
            p.setMove(p.getFirstMove());
        });

        p.getEvents().addEventListener(flags[1], name, e -> p.getAttack().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Boosts Speed stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceScarf(Pokemon p) {
        String name = HeldItemID.CHOICE_SCARF_ID.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.FIND_MOVE_ORDER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (p.firstRound()) return;
            p.setMove(p.getFirstMove());
        });

        p.getEvents().addEventListener(flags[1], name, e -> p.getSpeed().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Boosts Sp-Attack stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceSpecs(Pokemon p) {
        String name = HeldItemID.CHOICE_SPECS_ID.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (p.firstRound()) return;
            p.setMove(p.getFirstMove());
        });

        p.getEvents().addEventListener(flags[1], name, e -> p.getSpecialAttack().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Restore 1/16 of Max HP at the end of each round
    public static HeldItem leftovers(Pokemon p) {
        String name = HeldItemID.LEFTOVERS_ID.name();
        EventID[] flags = new EventID[] {EventID.END_OF_ROUND};

        p.getEvents().addEventListener(flags[0], name, e -> p.restoreHpPercentMaxHP(1.0 / 16.0, " from its Leftovers"));

        return new HeldItem(p, name, flags);
	}

    // Boosts physical-move power by 10%
    public static HeldItem muscleBand(Pokemon p) {
        String name = HeldItemID.MUSCLE_BAND_ID.name();
        EventID[] flags = new EventID[] {EventID.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!(EventData.isUser(p, e) && e.moveUsed.isCategory(Move.PHYSICAL))) return;
            e.otherMoveMods *= 1.1;
        });

        return new HeldItem(p, name, flags);
	}

    // Contact with this Pokemon causes the attack to lose 1/6 of its max HP
    public static HeldItem rockyHelmet(Pokemon p) {
        String name = HeldItemID.ROCKY_HELMET_ID.name();
        EventID[] flags = new EventID[] {EventID.MOVE_MAKES_CONTACT};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!EventData.isTarget(p, e)) return;

            e.user.takeDamagePercentMaxHP(1.0 / 6.0, String.format(" from %s's Rocky Helmet", p));
        });

        return new HeldItem(p, name, flags);
	}

    // Boosts special-move power by 10%
    public static HeldItem wiseGlasses(Pokemon p) {
        String name = HeldItemID.WISE_GLASSES_ID.name();
        EventID[] flags = new EventID[] {EventID.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!(EventData.isUser(p, e) && e.moveUsed.isCategory(Move.SPECIAL))) return;
            e.otherMoveMods *= 1.1;
        });

        return new HeldItem(p, name, flags);
	}    
}
