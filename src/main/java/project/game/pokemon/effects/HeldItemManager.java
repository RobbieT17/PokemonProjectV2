package project.game.pokemon.effects;

import project.game.battle.BattleLog;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.move.Move;
import project.game.move.Move.MoveCategory;
import project.game.move.moveactions.MoveActionHealthRestore;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.HeldItem.HeldItemID;
import project.game.pokemon.stats.Type;
import project.game.utility.Counter;

public interface HeldItemManager {

    // Increases Pokemon's Sp.Def by 50% but disables Status Move (except Me First)
    public static HeldItem assaultVest(Pokemon p) {
        String name = HeldItemID.Assault_Vest.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.DEF_DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            for (Move m : p.getMoves()) {
                if (m.isCategory(Move.MoveCategory.Status)) m.disable();
            }
        });

        p.getEvents().addEventListener(flags[1], name, e -> {
            p.getSpecialDefense().setMod(150);
        });

        return new HeldItem(p, name, flags);
	}

    // Poison Type restore 1/16 of their Max HP, all other types lose that amount
    public static HeldItem blackSludge(Pokemon p) {
        String name = HeldItemID.Black_Sludge.name();
        EventID[] flags = new EventID[] {EventID.END_OF_ROUND};

        p.getEvents().addEventListener(flags[0], name, e -> {
            double percent = 6.25;
            if (p.isType(Type.Poison)) {
                MoveActionHealthRestore.applyHpRestorePercent(new EventManager(e.battleData, p), percent, " from its Black Sludge");
            }
            else {
               p.takeDamagePercentMaxHP(percent, " from its Black Sludge"); 
            }   
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
        EventID[] flags = new EventID[] {EventID.END_OF_ROUND, EventID.DEF_MOVE_HITS, EventID.SWITCH_OUT};

        Counter c = new Counter(3);

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (c.inc()) {
                BattleLog.add("Surprise! %s's Bomb Surprise goes off!", p);

                for (Pokemon t : e.battleData.getAllPokemonInBattle()) {
                    t.faints();
                }
                
            }
        });

        p.getEvents().addEventListener(flags[1], name, e -> {
            c.reset();
        });
        p.getEvents().addEventListener(flags[2], name, e -> {
            c.reset();
        });

        return new HeldItem(p, name, flags);
	}

    // Boosts Attack stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceBand(Pokemon p) {
        String name = HeldItemID.Choice_Band.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.ATK_DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (p.isFirstRound()) return;
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
            if (p.isFirstRound()) return;
            p.setMoveSelected(p.getFirstMove());
        });

        p.getEvents().addEventListener(flags[1], name, e -> p.getSpeed().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Boosts Sp-Attack stat by 50% but forces Pokemon to use first move selected (rests after switch out)
    public static HeldItem choiceSpecs(Pokemon p) {
        String name = HeldItemID.Choice_Specs.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.ATK_DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (p.isFirstRound()) return;
            p.setMoveSelected(p.getFirstMove());
        });

        p.getEvents().addEventListener(flags[1], name, e -> p.getSpecialAttack().setMod(150));

        return new HeldItem(p, name, flags);
	}

    // Restore 1/16 of Max HP at the end of each round
    public static HeldItem leftovers(Pokemon p) {
        String name = HeldItemID.Leftovers.name();
        EventID[] flags = new EventID[] {EventID.END_OF_ROUND};

        p.getEvents().addEventListener(flags[0], name, e -> {
                MoveActionHealthRestore.applyHpRestorePercent(new EventManager(e.battleData, p), 6.25, " from its Leftovers");
            }
        );

        return new HeldItem(p, name, flags);
	}

    // Boosts physical-move power by 10%
    public static HeldItem muscleBand(Pokemon p) {
        String name = HeldItemID.Muscle_Band.name();
        EventID[] flags = new EventID[] {EventID.ATK_DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (e.moveUsed.isCategory(MoveCategory.Physical)) {
                e.otherDamageMods *= 1.1;
            }
        });

        return new HeldItem(p, name, flags);
	}

    // Contact with this Pokemon causes the attack to lose 1/6 of its max HP
    public static HeldItem rockyHelmet(Pokemon p) {
        String name = HeldItemID.Rocky_Helmet.name();
        EventID[] flags = new EventID[] {EventID.DEF_MOVE_MAKES_CONTACT};

        p.getEvents().addEventListener(flags[0], name, e -> {
            e.attackUser.takeDamagePercentMaxHP(100.0 / 6.0, String.format(" from %s's Rocky Helmet", p));
        });

        return new HeldItem(p, name, flags);
	}

    // Boosts special-move power by 10%
    public static HeldItem wiseGlasses(Pokemon p) {
        String name = HeldItemID.Wise_Glasses.name();
        EventID[] flags = new EventID[] {EventID.ATK_DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (e.moveUsed.isCategory(MoveCategory.Special)) {
                e.otherDamageMods *= 1.1;
            }
        });

        return new HeldItem(p, name, flags);
	}    
}
