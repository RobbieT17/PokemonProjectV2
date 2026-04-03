package project.game.pokemon.effects;

import java.util.function.Function;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.GameEvents;
import project.game.exceptions.MoveInterruptedException;
import project.game.exceptions.PokemonCannotActException;
import project.game.exceptions.PokemonFaintedException;
import project.game.move.Move;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Type;
import project.game.utility.Counter;
import project.game.utility.RandomValues;
import project.game.utility.State;

public interface StatusConditionManager {

    public enum StatusConditionIDs {

        BURN_ID(StatusConditionManager::burn),
        FREEZE_ID(StatusConditionManager::freeze),
        INFECT_ID(StatusConditionManager::infect),
        PARALYSIS_ID(StatusConditionManager::paralysis),
        POISON_ID(StatusConditionManager::poisoned),
        BAD_POISON_ID(StatusConditionManager::badlyPoisoned),
        SLEEP_ID(StatusConditionManager::sleep),
        FLINCH_ID(StatusConditionManager::flinch),
        BOUND_ID(StatusConditionManager::bound),
        CONFUSION_ID(StatusConditionManager::confusion),
        SEEDED_ID(StatusConditionManager::seeded),
        FORCED_MOVE_ID(StatusConditionManager::forcedMove),
        FOCUSED_ID(StatusConditionManager::focused),
        RAMPAGE_ID(StatusConditionManager::rampage),
        RECHARGE_ID(StatusConditionManager::rampage),
        GROUNDED_ID(StatusConditionManager::grounded),
        CHARGE_MOVE_ID(StatusConditionManager::chargeMove),
        FLY_ID(StatusConditionManager::fly),
        DIG_ID(StatusConditionManager::dig),
        DIVE_ID(StatusConditionManager::dive),
        // No Function Provided
        NO_INVUL_ID(null),
        PROTECT_ID(null);

        private final Function<StatusContext, StatusCondition> func;
  

        StatusConditionIDs(Function<StatusContext, StatusCondition> func) {
            this.func = func;
        }    

        public StatusCondition apply(StatusContext c) {
            return this.func.apply(c);
        }
    }

    private static void checkIfFaints(Pokemon p) {
        if (p.getConditions().isFainted()) throw new PokemonFaintedException();
    }

    /*
     * Burned Pokemon lose 1/16 of their max HP at the end of each round.
     * Reduces physical-move damage by 50%
     */
    public static StatusCondition burn(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.BURN_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.END_OF_ROUND, GameEvents.DAMAGE_MULTIPLIER};

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
    public static StatusCondition freeze(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.FREEZE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.PRIMARY_STATUS_BEFORE};

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
     public static StatusCondition infect(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.INFECT_ID;
        String name = id.name();
        String[] flags = new String[] {
            GameEvents.MOVE_MAKES_CONTACT, GameEvents.DAMAGE_MULTIPLIER, 
            GameEvents.FIND_MOVE_ORDER, GameEvents.END_OF_ROUND
        };

        // For Non-Zombie Types
        p.getEvents().addEventListener(flags[0], name, e -> {
            Pokemon t = e.effectTarget;
            if (!EventData.isUser(p, e) || t.getConditions().hasPrimary()) return;
            t.getConditions().setPrimaryCondition(infect(c));
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
    public static StatusCondition paralysis(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.PARALYSIS_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.PRIMARY_STATUS_BEFORE, GameEvents.FIND_MOVE_ORDER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (!RandomValues.chance(50)) return;
            throw new PokemonCannotActException("%s is paralyzed and cannot move!", p);                 
        });

        p.getEvents().addEventListener(flags[1], name, e -> p.getSpeed().setMod(50));

        BattleLog.add("%s was paralyzed!", p);
        return new StatusCondition(p, name, flags);
    }

    // Poisoned Pokemon lose 1/8 of their max HP at the end of each round
    public static StatusCondition poisoned(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.POISON_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.END_OF_ROUND};

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
    public static StatusCondition badlyPoisoned(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.BAD_POISON_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.END_OF_ROUND};

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
    public static StatusCondition sleep(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.SLEEP_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.PRIMARY_STATUS_BEFORE};

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


    public static StatusCondition fly(StatusContext c) {
        Pokemon p = c.target;
        Move m = c.move;
        StatusConditionIDs id = StatusConditionIDs.FLY_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.MOVE_SELECTION, GameEvents.MOVE_ACCURACY};

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

    public static StatusCondition dig(StatusContext c) {
        Pokemon p = c.target;
        Move m = c.move;
        StatusConditionIDs id = StatusConditionIDs.DIG_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.MOVE_SELECTION, GameEvents.MOVE_ACCURACY};

        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            if (!EventData.isTarget(p, e)) return;
            if (e.moveUsed.getMoveID() == 89) return;

            throw new MoveInterruptedException("But %s is high in the sky!", p);
        });

        BattleLog.add("%s dug underground!", p);
        return new StatusCondition(p, name, flags);
	}

    public static StatusCondition dive(StatusContext c) {
        Pokemon p = c.target;
        Move m = c.move;
        StatusConditionIDs id = StatusConditionIDs.DIVE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.MOVE_SELECTION, GameEvents.MOVE_ACCURACY};

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
    public static StatusCondition flinch(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.CHARGE_MOVE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.BEFORE_MOVE};

        p.getEvents().addEventListener(flags[0], name, e -> {
            p.getConditions().removeCondition(id);
            throw new PokemonCannotActException("%s flinched and couldn't move!", p);
        });

        return new StatusCondition(p, name, flags);
    }

    // Traps Pokemon from escaping battle and loses 1/8 of max HP
    public static StatusCondition bound(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.CHARGE_MOVE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.END_OF_ROUND, GameEvents.SWITCH_OUT};

        Counter counter = new Counter(RandomValues.generateInt(2, 5));

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (counter.inc()){
                p.getConditions().removeCondition(id);
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
    public static StatusCondition confusion(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.CHARGE_MOVE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.STATUS_BEFORE};

        Counter counter = new Counter(RandomValues.generateInt(2, 4));

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (counter.inc()){
                p.getConditions().removeCondition(id);
                return;
            }
            BattleLog.add("%s is confused!", p);
            if (!RandomValues.chance(50)) return;

            MoveActionAttackDamage.takeConfusionDamage(p);
            checkIfFaints(p);
            throw new PokemonCannotActException();
        });

        BattleLog.add("%s became confused!", p);
        return new StatusCondition(p, name, flags);
    }

    public static StatusCondition seeded(StatusContext c) {
        Pokemon p = c.target;
        Pokemon r = c.source;
        StatusConditionIDs id = StatusConditionIDs.CHARGE_MOVE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.END_OF_ROUND};

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
    public static StatusCondition forcedMove(StatusContext c) {
        Pokemon p = c.target;
        Move m = c.move;
        int n = c.count;
        StatusConditionIDs id = StatusConditionIDs.CHARGE_MOVE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.MOVE_SELECTION, GameEvents.END_OF_ROUND};

        Counter counter = new Counter(n);

        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            if (counter.inc()) p.getConditions().removeCondition(id); 
        });

        return new StatusCondition(p, name, flags);
    }

    /*
     * Pokemon concentrates energy, and then attacks on the next turn
     * If the Pokemon is hit with an attack, it lose it focus and
     * their attack will fail.
     */
    public static StatusCondition focused(StatusContext c) {
        Pokemon p = c.target;
        Move m = c.move;
        StatusConditionIDs id = StatusConditionIDs.CHARGE_MOVE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.MOVE_SELECTION, GameEvents.USE_MOVE, GameEvents.MOVE_HITS};

        State state = new State(); // State True: Lost Focus
    
        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));

        p.getEvents().addEventListener(flags[1], name, e -> {
            p.getConditions().removeCondition(id);
            if (state.getBool()) throw new PokemonCannotActException("%s lost it's focus and couldn't move!", p);     
        });

        p.getEvents().addEventListener(flags[2], name, e -> {
            if (!EventData.isTarget(p, e)) return;
            state.set(true);
        });

        return new StatusCondition(p, name, flags);
    }

    // Grounded Pokemon are vulnerable to Ground-Type moves (even Flying-Types) 
    public static StatusCondition grounded(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionIDs id = StatusConditionIDs.CHARGE_MOVE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.MOVE_EFFECTIVENESS};

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
    public static StatusCondition rampage(StatusContext c) {
        Pokemon p = c.target;
        Move m = c.move;
        StatusConditionIDs id = StatusConditionIDs.CHARGE_MOVE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.MOVE_SELECTION, GameEvents.END_OF_TURN, GameEvents.MOVE_INTERRUPTED};

        Counter counter = new Counter(RandomValues.generateInt(2, 3));

        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            if (counter.inc()) {
                p.getConditions().removeCondition(id);
                p.getConditions().addCondition(confusion(c));
            }  
        });
        p.getEvents().addEventListener(flags[2], name, e -> {
            p.getConditions().removeCondition(id);
        });

        return new StatusCondition(p, name, flags);
	}
    
    /*
     * Pokemon takes a turn to charge a move,
     * If not interrupted, the Pokemon acts
     * on the next turn
     */
    public static StatusCondition chargeMove(StatusContext c) {
        Pokemon p = c.target;
        Move m = c.move;
        StatusConditionIDs id = StatusConditionIDs.CHARGE_MOVE_ID;
        String name = id.name();
        String[] flags = new String[] {GameEvents.MOVE_SELECTION, GameEvents.MOVE_INTERRUPTED};

        p.getEvents().addEventListener(flags[0], name, e -> p.setMove(m));
        p.getEvents().addEventListener(flags[1], name, e -> p.getConditions().removeCondition(id));

        return new StatusCondition(p, id.name(), flags);
	}
    
// Public Class Methods
    public static String failMessage(StatusConditionIDs id) {
        return switch (id) {
            case StatusConditionIDs.BURN_ID -> "is already burned!";
            case StatusConditionIDs.FREEZE_ID -> "is already frozen!";
            case StatusConditionIDs.PARALYSIS_ID -> "is already paralyzed!";
            case StatusConditionIDs.POISON_ID, StatusConditionIDs.BAD_POISON_ID -> "is already poisoned!";
            case StatusConditionIDs.SLEEP_ID -> "is already asleep!";
            
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        };
    }

    // Message displayed when the condition expires
    public static String expireMessage(StatusConditionIDs id) {
        return switch (id) {
            case StatusConditionIDs.FREEZE_ID -> " thawed!";
            case StatusConditionIDs.SLEEP_ID -> " woke up!";
            case StatusConditionIDs.BOUND_ID -> " was freed!";
            case StatusConditionIDs.CONFUSION_ID -> " snapped out of confusion!";
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        };
    }
    

}
