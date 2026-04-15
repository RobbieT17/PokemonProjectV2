package project.game.pokemon.effects;

import java.util.function.Function;

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
        Assault_Vest(HeldItemManager::assaultVest),
        Black_Sludge(HeldItemManager::blackSludge),
        Bomb_Surprise(HeldItemManager::bombSurprise),
        Choice_Band(HeldItemManager::choiceBand),
        Choice_Scarf(HeldItemManager::choiceScarf),
        Choice_Specs(HeldItemManager::choiceSpecs),
        Leftovers(HeldItemManager::leftovers),
        Muscle_Band(HeldItemManager::muscleBand),
        Rocky_Helmet(HeldItemManager::rockyHelmet),
        Wise_Glasses(HeldItemManager::wiseGlasses);

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
        String name = HeldItemID.Assault_Vest.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            for (Move m : p.getMoves()) {
                if (m.isCategory(Move.MoveCategory.Status)) m.disable();
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
        String name = HeldItemID.Black_Sludge.name();
        EventID[] flags = new EventID[] {EventID.END_OF_ROUND};

        p.getEvents().addEventListener(flags[0], name, e -> {
            double percent = 1.0 / 16.0;
            if (p.isType(Type.Poison)) {
                p.restoreHpPercentMaxHP(percent, " from its Black Sludge");
                return;
            }
            p.takeDamagePercentMaxHP(percent, " from its Black Sludge");
        });

        return new HeldItem(p, name, flags);
	}

    /**
     * Blows up all Pokemon on field after 3 rounds. 
     * Timer resets if the holder switches out or if
     * the holder is the target of a non-status move 
     * attack (whether the move deals damage is irrelevant)
     * 
     */
    public static HeldItem bombSurprise(Pokemon p) {
        String name = HeldItemID.Bomb_Surprise.name();
        EventID[] flags = new EventID[] {EventID.BEFORE_MOVE, EventID.MOVE_HITS, EventID.SWITCH_OUT};

        Counter c = new Counter(3);

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (c.inc()) {
                BattleLog.add("Surprise! %s's Bomb Surprise goes off!", p);
                e.battleData.getPlayer1().getPokemonInBattle().faints();
                e.battleData.getPlayer2().getPokemonInBattle().faints();
                
                throw new MoveInterruptedException();
            }
        });

        p.getEvents().addEventListener(flags[1], name, e -> c.reset());
        p.getEvents().addEventListener(flags[2], name, e -> c.reset());


        return new HeldItem(p, name, flags);
	}

    // Boosts Attack stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceBand(Pokemon p) {
        String name = HeldItemID.Choice_Band.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (p.firstRound()) return;
            p.setMoveSelected(p.getFirstMove());
        });

        p.getEvents().addEventListener(flags[1], name, e -> p.getAttack().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Boosts Speed stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceScarf(Pokemon p) {
        String name = HeldItemID.Choice_Scarf.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.FIND_MOVE_ORDER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (p.firstRound()) return;
            p.setMoveSelected(p.getFirstMove());
        });

        p.getEvents().addEventListener(flags[1], name, e -> p.getSpeed().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Boosts Sp-Attack stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceSpecs(Pokemon p) {
        String name = HeldItemID.Choice_Specs.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (p.firstRound()) return;
            p.setMoveSelected(p.getFirstMove());
        });

        p.getEvents().addEventListener(flags[1], name, e -> p.getSpecialAttack().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Restore 1/16 of Max HP at the end of each round
    public static HeldItem leftovers(Pokemon p) {
        String name = HeldItemID.Leftovers.name();
        EventID[] flags = new EventID[] {EventID.END_OF_ROUND};

        p.getEvents().addEventListener(flags[0], name, e -> p.restoreHpPercentMaxHP(1.0 / 16.0, " from its Leftovers"));

        return new HeldItem(p, name, flags);
	}

    // Boosts physical-move power by 10%
    public static HeldItem muscleBand(Pokemon p) {
        String name = HeldItemID.Muscle_Band.name();
        EventID[] flags = new EventID[] {EventID.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!(EventData.isUser(p, e) && e.moveUsed.isCategory(Move.MoveCategory.Physical))) return;
            e.otherMoveMods *= 1.1;
        });

        return new HeldItem(p, name, flags);
	}

    // Contact with this Pokemon causes the attack to lose 1/6 of its max HP
    public static HeldItem rockyHelmet(Pokemon p) {
        String name = HeldItemID.Rocky_Helmet.name();
        EventID[] flags = new EventID[] {EventID.MOVE_MAKES_CONTACT};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!EventData.isTarget(p, e)) return;

            e.user.takeDamagePercentMaxHP(1.0 / 6.0, String.format(" from %s's Rocky Helmet", p));
        });

        return new HeldItem(p, name, flags);
	}

    // Boosts special-move power by 10%
    public static HeldItem wiseGlasses(Pokemon p) {
        String name = HeldItemID.Wise_Glasses.name();
        EventID[] flags = new EventID[] {EventID.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!(EventData.isUser(p, e) && e.moveUsed.isCategory(Move.MoveCategory.Special))) return;
            e.otherMoveMods *= 1.1;
        });

        return new HeldItem(p, name, flags);
	}    
}
