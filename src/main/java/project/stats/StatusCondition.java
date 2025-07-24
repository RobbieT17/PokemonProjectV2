package project.stats;

import project.event.EventData;
import project.event.GameEvent;
import project.battle.BattleLog;
import project.exceptions.MoveInterruptedException;
import project.exceptions.PokemonCannotActException;
import project.exceptions.PokemonFaintedException;
import project.move.Move;
import project.move.MoveAction;
import project.pokemon.Pokemon;
import project.utility.Counter;
import project.utility.RandomValues;
import project.utility.State;

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
    

// Static Methods
    private static void checkIfFaints(Pokemon p) {
        if (p.getConditions().isFainted()) throw new PokemonFaintedException();
    }

    /*
     * Burned Pokemon lose 1/16 of their max HP at the end of each round.
     * Reduces physical-move damage by 50%
     */
    public static StatusCondition burn(Pokemon p) {
        String name = StatusCondition.BURN_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND, GameEvent.DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            p.takeDamagePercentMaxHP(1.0 / 16.0, " from the burn");
            checkIfFaints(p);
        });

        p.getEvents().addEventListener(flags[1], name, e -> {
            if (!(EventData.isUser(p, e) && e.moveUsed.isCategory(Move.PHYSICAL))) return;
            e.otherMoveMods *= 0.5;
        });

        BattleLog.add("%s was burned!", p);
        return new StatusCondition(p, name, flags);
    }

    /*
     * Frozen Pokemon cannot act or dodge attacks
     * At the beginning of each round there is a 20%
     * chance for the Pokemon to thaw
     */
    public static StatusCondition freeze(Pokemon p) {
        String name = StatusCondition.FREEZE_ID;
        String[] flags = new String[] {GameEvent.PRIMARY_STATUS_BEFORE};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (p.getMoveSelected().getMoveID() == 815) return;

            if (RandomValues.chance(20)) {
                p.getConditions().setImmobilized(false);
                p.getConditions().clearPrimary();
                BattleLog.add("%s thawed!", p);
                return;
            }
            p.getConditions().setImmobilized(true);
            throw new PokemonCannotActException("%s is frozen solid!", p);
        });

        BattleLog.add("%s froze!", p);
        return new StatusCondition(p, name, flags);
    }


    /*
     * Reduces special-move damage by 50%
     * If the infect Pokemon uses a contact move, their target is also infected. 
     * Zombie Pokemon gain a 200% speed boost and restore 1/8 of their max HP at the end of the round, 
     * however only Non-Zombie Pokemon can infect Zombies.
     */
     public static StatusCondition infect(Pokemon p) {
        String name = StatusCondition.INFECT_ID;
        String[] flags = new String[] {
            GameEvent.MOVE_MAKES_CONTACT, GameEvent.DAMAGE_MULTIPLIER, 
            GameEvent.FIND_MOVE_ORDER, GameEvent.END_OF_ROUND
        };

        // For Non-Zombie Types
        p.getEvents().addEventListener(flags[0], name, e -> {
            Pokemon t = e.effectTarget;
            if (!EventData.isUser(p, e) || t.getConditions().hasPrimary()) return;
            t.getConditions().setPrimaryCondition(infect(t));
        });  
        p.getEvents().addEventListener(flags[1], name, e -> {
            if (!(EventData.isUser(p, e) && e.moveUsed.isCategory(Move.SPECIAL))) return;
            e.otherMoveMods *= 0.5; 
        });

        // For Zombie-Types
        p.getEvents().addEventListener(flags[2], name, e -> {
            if (!p.isType(Type.ZOMBIE)) return;
            p.getSpeed().setMod(200);
        });
        p.getEvents().addEventListener(flags[3], name, e -> {
            if (!p.isType(Type.ZOMBIE)) return;
            p.restoreHpPercentMaxHP(1.0 / 8.0, " from the infection");
        });      

        BattleLog.add("%s was infected!", p);
        return new StatusCondition(p, name, flags);
     }

    /*
     * Paralyzed Pokemon have 50% reduced speed.
     * There is a 33% chance the Pokemon cannot 
     * act for the round.
     */
    public static StatusCondition paralysis(Pokemon p) {
        String name = StatusCondition.PARALYSIS_ID;
        String[] flags = new String[] {GameEvent.PRIMARY_STATUS_BEFORE, GameEvent.FIND_MOVE_ORDER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!RandomValues.chance(50)) return;
            throw new PokemonCannotActException("%s is paralyzed and cannot move!", p);                 
        });

        p.getEvents().addEventListener(flags[1], name, e -> p.getSpeed().setMod(50));

        BattleLog.add("%s was paralyzed!", p);
        return new StatusCondition(p, name, flags);
    }

    
    // Poisoned Pokemon lose 1/8 of their max HP at the end of each round
    public static StatusCondition poisoned(Pokemon p) {
        String name = StatusCondition.POISON_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND};

        p.getEvents().addEventListener(flags[0], name, e -> {
            p.takeDamagePercentMaxHP(1.0 / 8.0, " from the poison");
            checkIfFaints(p);
        });

        BattleLog.add("%s was poisoned!", p);
        return new StatusCondition(p, name, flags);
    }

    /*
     * Badly poisoned lose x/16 of their max HP at the end of each round
     * where x is the number of turns after being poisoned
     */
    public static StatusCondition badlyPoisoned(Pokemon p) {
        String name = StatusCondition.BAD_POISON_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND};

        Counter counter = new Counter();

        p.getEvents().addEventListener(flags[0], name, e -> {
            counter.inc();
            p.takeDamagePercentMaxHP(counter.getCount() / 16.0, " from the poison");
            checkIfFaints(p);
        });

        BattleLog.add("%s was badly poisoned!", p);
        return new StatusCondition(p, name, flags);
    }

    // Pokemon is asleep for 1 to 3 turns and cannot act
    public static StatusCondition sleep(Pokemon p) {
        String name = StatusCondition.SLEEP_ID;
        String[] flags = new String[] {GameEvent.PRIMARY_STATUS_BEFORE};

        Counter counter = new Counter(RandomValues.generateInt(1, 3));

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (counter.inc()) {
                p.getConditions().setImmobilized(false);
                p.getConditions().clearPrimary();
                BattleLog.add("%s woke up!", p);
                return;
            } 
            p.getConditions().setImmobilized(true);
            if (p.getMoveSelected().getMoveID() == 214) return; // 214: Sleep Talk
            throw new PokemonCannotActException("%s is fast asleep...", p);    
        });

        BattleLog.add("%s fell asleep!", p);
        return new StatusCondition(p, name, flags);
    }


    public static StatusCondition fly(Pokemon p, Move m) {
        String name = StatusCondition.FLY_ID;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.MOVE_ACCURACY};

        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            Move a = e.moveUsed;
            if (!EventData.isTarget(p, e)) return;
            if (a.getMoveID() == 479 ||  a.getMoveID() == 542) return;
            
            throw new MoveInterruptedException("But %s is high in the sky!", p);
        });

        BattleLog.add("%s flew into the sky!", p);
        return new StatusCondition(p, name, flags);
	}

    public static StatusCondition dig(Pokemon p, Move m) {
        String name = StatusCondition.FLY_ID;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.MOVE_ACCURACY};

        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            if (!EventData.isTarget(p, e)) return;
            if (e.moveUsed.getMoveID() == 89) return;

            throw new MoveInterruptedException("But %s is high in the sky!", p);
        });

        BattleLog.add("%s dug underground!", p);
        return new StatusCondition(p, name, flags);
	}

    public static StatusCondition dive(Pokemon p, Move m) {
        String name = StatusCondition.DIVE_ID;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.MOVE_ACCURACY};

        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            Move a = e.moveUsed;
            if (!EventData.isTarget(p, e)) return; 
            if (a.getMoveID() == 57 || a.getMoveID() == 250) return;

            throw new MoveInterruptedException("But %s is underwater!", p);
        });

        BattleLog.add("%s dove underwater!", p);
        return new StatusCondition(p, name, flags);
	}

    // Pokemon flinches and can't act for the round
    public static StatusCondition flinch(Pokemon p) {
        String name = StatusCondition.FLINCH_ID;
        String[] flags = new String[] {GameEvent.BEFORE_MOVE};

        p.getEvents().addEventListener(flags[0], name, e -> {
            p.getConditions().removeCondition(name);
            throw new PokemonCannotActException("%s flinched and couldn't move!", p);
        });

        return new StatusCondition(p, name, flags);
    }

    // Traps Pokemon from escaping battle and loses 1/8 of max HP
    public static StatusCondition bound(Pokemon p) {
        String name = StatusCondition.BOUND_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND, GameEvent.SWITCH_OUT};

        Counter counter = new Counter(RandomValues.generateInt(2, 5));

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (counter.inc()){
                p.getConditions().removeCondition(name);
                return;
            }

            p.takeDamagePercentMaxHP(1.0 / 8.0, " from the bound");
            checkIfFaints(p);
        });

        BattleLog.add("%s was trapped in a bound!", p);
        return new StatusCondition(p, name, flags);
    }

    /*
     * Confused Pokemon have a 50% chance to damage themselves
     * Confusion last for 2-4 rounds.
     */
    public static StatusCondition confusion(Pokemon p) {
        String name = StatusCondition.CONFUSION_ID;
        String[] flags = new String[] {GameEvent.STATUS_BEFORE};

        Counter counter = new Counter(RandomValues.generateInt(2, 4));

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (counter.inc()){
                p.getConditions().removeCondition(name);
                return;
            }
            BattleLog.add("%s is confused!", p);
            if (!RandomValues.chance(50)) return;

            MoveAction.takeConfusionDamage(p);
            checkIfFaints(p);
            throw new PokemonCannotActException();
        });

        BattleLog.add("%s became confused!", p);
        return new StatusCondition(p, name, flags);
    }

    public static StatusCondition seeded(Pokemon p, Pokemon r) {
        String name = StatusCondition.SEEDED_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND};

        p.getEvents().addEventListener(flags[0], name, e -> {
            int damage = (int) (p.getHp().getMaxHealthPoints() / 8.0);
            BattleLog.add("%s drained %d HP from %s!", r, damage, p);

            r.restoreHP(damage);
            p.takeDamage(damage);
            checkIfFaints(p);
        });

        BattleLog.add("%s was seeded!", p);
        return new StatusCondition(p, name, flags);
    }

    // Forces Pokemon to use the move chosen for n rounds
    public static StatusCondition forcedMove(Pokemon p, Move m, int n) {
        String name = StatusCondition.FORCED_MOVE_ID;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.END_OF_ROUND};

        Counter counter = new Counter(n);

        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            if (counter.inc()) p.getConditions().removeCondition(name); 
        });

        return new StatusCondition(p, name, flags);
    }

    /*
     * Pokemon concentrates energy, and then attacks on the next turn
     * If the Pokemon is hit with an attack, it lose it focus and
     * their attack will fail.
     */
    public static StatusCondition focused(Pokemon p, Move m) {
        String name = StatusCondition.FOCUSED_ID;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.USE_MOVE, GameEvent.MOVE_HITS};

        State state = new State(); // State True: Lost Focus
    
        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));

        p.getEvents().addEventListener(flags[1], name, e -> {
            p.getConditions().removeCondition(name);
            if (state.getBool()) throw new PokemonCannotActException("%s lost it's focus and couldn't move!", p);     
        });

        p.getEvents().addEventListener(flags[2], name, e -> {
            if (!EventData.isTarget(p, e)) return;
            state.set(true);
        });

        return new StatusCondition(p, name, flags);
    }

    // Grounded Pokemon are vulnerable to Ground-Type moves (even Flying-Types) 
    public static StatusCondition grounded(Pokemon p) {
        String name = StatusCondition.GROUNDED_ID;
        String[] flags = new String[] {GameEvent.MOVE_EFFECTIVENESS};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!EventData.isTarget(p, e)) return;

            if (e.moveUsed.isType(Type.GROUND) && e.moveEffectiveness == 0) {
                e.moveEffectiveness = 1.0;
            }
        });

        return new StatusCondition(p, name, flags);
	}

    /*
     * Pokemon uses the same move 2-3 turns then becomes confused.
     * The rampage ends early if the move is interrupted.
     */
    public static StatusCondition rampage(Pokemon p, Move m) {
        String name = StatusCondition.RAMPAGE_ID;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.END_OF_TURN, GameEvent.MOVE_INTERRUPTED};

        Counter counter = new Counter(RandomValues.generateInt(2, 3));

        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            if (counter.inc()) {
                p.getConditions().removeCondition(name);
                p.getConditions().addCondition(confusion(p));
            }  
        });
        p.getEvents().addEventListener(flags[2], name, e -> {
            p.getConditions().removeCondition(name);
        });

        return new StatusCondition(p, name, flags);
	}
    
    /*
     * Pokemon takes a turn to charge a move,
     * If not interrupted, the Pokemon acts
     * on the next turn
     */
    public static StatusCondition chargeMove(Pokemon p, Move m) {
        String name = StatusCondition.CHARGE_MOVE;
        String[] flags = new String[] {GameEvent.MOVE_SELECTION, GameEvent.MOVE_INTERRUPTED};

        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));
        p.getEvents().addEventListener(flags[1], name, e -> p.getConditions().removeCondition(name));

        return new StatusCondition(p, name, flags);
	}
    
// Public Class Methods
    public static String failMessage(String id) {
        return switch (id) {
            case StatusCondition.BURN_ID -> "is already burned!";
            case StatusCondition.FREEZE_ID -> "is already frozen!";
            case StatusCondition.PARALYSIS_ID -> "is already paralyzed!";
            case StatusCondition.POISON_ID, StatusCondition.BAD_POISON_ID -> "is already poisoned!";
            case StatusCondition.SLEEP_ID -> "is already asleep!";
            
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        };
    }

    // Message displayed when the condition expires
    public static String expireMessage(String id) {
        return switch (id) {
            case StatusCondition.FREEZE_ID -> " thawed!";
            case StatusCondition.SLEEP_ID -> " woke up!";
            case StatusCondition.BOUND_ID -> " was freed!";
            case StatusCondition.CONFUSION_ID -> " snapped out of confusion!";
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        };
    }


}
