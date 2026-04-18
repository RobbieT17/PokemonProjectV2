package project.game.pokemon.effects;

import java.util.function.Function;

import project.game.battle.BattleLog;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.MoveInterruptedException;
import project.game.exceptions.PokemonCannotActException;
import project.game.exceptions.PokemonFaintedException;
import project.game.move.Move;
import project.game.move.Move.MoveCategory;
import project.game.move.moveactions.MoveActionAttack;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Type;
import project.game.utility.Counter;
import project.game.utility.RandomValues;
import project.game.utility.State;

public interface StatusConditionManager {

    public enum StatusConditionID {

        Burn(StatusConditionManager::burn),
        Freeze(StatusConditionManager::freeze),
        Infect(StatusConditionManager::infect),
        Paralysis(StatusConditionManager::paralysis),
        Poison(StatusConditionManager::poisoned),
        Bad_Poison(StatusConditionManager::badlyPoisoned),
        Sleep(StatusConditionManager::sleep),
        Flinch(StatusConditionManager::flinch),
        Bound(StatusConditionManager::bound),
        Confusion(StatusConditionManager::confusion),
        Charge(StatusConditionManager::chargeMove),
        Recharge(StatusConditionManager::rampage),
        Forced_Move(StatusConditionManager::forcedMove),
        Focused(StatusConditionManager::focused),
        Rampage(StatusConditionManager::rampage),
        Grounded(StatusConditionManager::grounded),
        Seeded(StatusConditionManager::seeded),
        Endure(StatusConditionManager::endure),
        Fly_State(StatusConditionManager::fly),
        Dig_State(StatusConditionManager::dig),
        Dive_State(StatusConditionManager::dive),
        // No Function Provided
        No_Invul(null),
        Protect(null);

        private final Function<StatusContext, StatusCondition> func;
  
        StatusConditionID(Function<StatusContext, StatusCondition> func) {
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
        StatusConditionID id = StatusConditionID.Burn;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.PRIMARY_STATUS_AFTER, EventID.ATK_DAMAGE_MULTIPLIER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            p.takeDamagePercentMaxHP(100.0 / 16.0, " from the burn");
            checkIfFaints(p);
        });

        p.getEvents().addEventListener(flags[1], name, e -> {
            if (e.moveUsed.isCategory(MoveCategory.Physical)) {
                e.otherMoveMods *= 0.5;
            }
           
        });

        BattleLog.add("%s was burned!", p);
        return new StatusCondition(p, name, flags);
    }

    /*
     * Frozen Pokemon cannot act or dodge attacks
     * At the beginning of each round there is a 20%
     * chance for the Pokemon to thaw
     * 
     * Moves that remove the effect:
     * - Flare Blitz
     */
    public static StatusCondition freeze(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Freeze;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.PRIMARY_STATUS_BEFORE};

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
     * Infected Pokemon lose 1/12 of their max HP at the end of each round
     * and reduces special-move power by 50%. If the infected Pokemon (unless a Zombie-Type) 
     * uses a contact move, the targeted Pokemon has a 10% chance to also be infected.
     *  
     * If the infected Pokemon is a Zombie-Type, their speed is doubled instead.
     * Only Non-Zombie Pokemon can infect Zombie-Type Pokemon.
     */
     public static StatusCondition infect(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Infect;
        String name = id.name();
        EventID[] flags = new EventID[] {
            EventID.ATK_MOVE_MAKES_CONTACT, EventID.ATK_DAMAGE_MULTIPLIER, 
            EventID.FIND_MOVE_ORDER, EventID.PRIMARY_STATUS_AFTER
        };

        // Move makes contact: Infected Pokemon infects their targert
        p.getEvents().addEventListener(flags[0], name, e -> {
            Pokemon t = e.attackTarget;

            if (!p.isType(Type.Zombie) && !t.getConditions().hasPrimary()) {
                // Rolls for infection
                if (RandomValues.chance(10)) {
                    StatusContext context = new StatusContext(t);
                    t.getConditions().setPrimaryCondition(infect(context));
                }
                
            }
            
        }); 
        
        // Damage Multiplier: Special Moves deal 50% less damage
        p.getEvents().addEventListener(flags[1], name, e -> {
            if (!p.isType(Type.Zombie) && e.moveUsed.isCategory(Move.MoveCategory.Special)) {
                e.otherMoveMods *= 0.5;
            }
        });

        // Turn Order: Infected Zombie-Type Pokemon have double speed
        p.getEvents().addEventListener(flags[2], name, e -> {
            if (p.isType(Type.Zombie)) {
                p.getSpeed().setMod(200);
            }
        });

        // End of Round: Infected Non-Zombie-Type Pokemon lose 1/12 of their max HP
        p.getEvents().addEventListener(flags[3], name, e -> {
            if (!p.isType(Type.Zombie)) {
                p.takeDamagePercentMaxHP(100.0 / 12.0, " from the infection");
            }
            
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
        StatusConditionID id = StatusConditionID.Paralysis;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.PRIMARY_STATUS_BEFORE, EventID.FIND_MOVE_ORDER};

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
        StatusConditionID id = StatusConditionID.Poison;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.PRIMARY_STATUS_AFTER};

        p.getEvents().addEventListener(flags[0], name, e -> {
            p.takeDamagePercentMaxHP(12.5, " from the poison");
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
        StatusConditionID id = StatusConditionID.Bad_Poison;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.PRIMARY_STATUS_AFTER};

        Counter counter = new Counter();

        p.getEvents().addEventListener(flags[0], name, e -> {
            counter.inc();
            p.takeDamagePercentMaxHP(100 * counter.getCount() / 16.0, " from the poison");
            checkIfFaints(p);
        });

        BattleLog.add("%s was badly poisoned!", p);
        return new StatusCondition(p, name, flags);
    }

    // Pokemon is asleep for 1 to 3 turns and cannot act
    public static StatusCondition sleep(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Sleep;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.PRIMARY_STATUS_BEFORE};

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
        StatusConditionID id = StatusConditionID.Fly_State;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.DEF_MOVE_ACCURACY};

        p.getEvents().addEventListener(flags[0], name, e -> {
            p.setMoveSelected(m);
        });
        p.getEvents().addEventListener(flags[1], name, e -> {
            Move a = e.moveUsed;

            if (a.getMoveID() == 479 ||  a.getMoveID() == 542) {
                return;
            }
            
            throw new MoveInterruptedException("But %s is high in the sky!", p);
        });

        BattleLog.add("%s flew into the sky!", p);
        return new StatusCondition(p, name, flags);
	}

    public static StatusCondition dig(StatusContext c) {
        Pokemon p = c.target;
        Move m = c.move;
        StatusConditionID id = StatusConditionID.Dig_State;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.DEF_MOVE_ACCURACY};

        p.getEvents().addEventListener(flags[0], name, e -> p.setMoveSelected(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            if (e.moveUsed.getMoveID() != 89) {
                return;
            }

            throw new MoveInterruptedException("But %s is underground!", p);
        });

        BattleLog.add("%s dug underground!", p);
        return new StatusCondition(p, name, flags);
	}

    public static StatusCondition dive(StatusContext c) {
        Pokemon p = c.target;
        Move m = c.move;
        StatusConditionID id = StatusConditionID.Dive_State;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.DEF_MOVE_ACCURACY};

        p.getEvents().addEventListener(flags[0], name, e -> p.setMoveSelected(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            Move a = e.moveUsed;

            if (a.getMoveID() == 57 || a.getMoveID() == 250) {
                return;
            }

            throw new MoveInterruptedException("But %s is underwater!", p);
        });

        BattleLog.add("%s dove underwater!", p);
        return new StatusCondition(p, name, flags);
	}

    // Pokemon flinches and can't act for the round
    public static StatusCondition flinch(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Flinch;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.BEFORE_MOVE};

        p.getEvents().addEventListener(flags[0], name, e -> {
            p.getConditions().removeCondition(id);
            throw new PokemonCannotActException("%s flinched and couldn't move!", p);
        });

        return new StatusCondition(p, name, flags);
    }

    // Traps Pokemon from escaping battle and loses 1/8 of max HP
    public static StatusCondition bound(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Bound;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.STATUS_AFTER, EventID.SWITCH_OUT};

        Counter counter = new Counter(RandomValues.generateInt(2, 5));

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (counter.inc()){
                p.getConditions().removeCondition(id);
                return;
            }

            p.takeDamagePercentMaxHP(12.5, " from the bound");
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
        StatusConditionID id = StatusConditionID.Confusion;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.STATUS_BEFORE};

        Counter counter = new Counter(RandomValues.generateInt(2, 4));

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (counter.inc()){
                p.getConditions().removeCondition(id);
                return;
            }
            BattleLog.add("%s is confused!", p);
            if (!RandomValues.chance(50)) return;

            MoveActionAttack.takeConfusionDamage(p);
            checkIfFaints(p);
            throw new PokemonCannotActException();
        });

        BattleLog.add("%s became confused!", p);
        return new StatusCondition(p, name, flags);
    }

    /**
     * A Pokemon is seeded! This means the opposing
     * Pokemon in battle drains HP from the bearer
     * at the end of each round.
     */
    public static StatusCondition seeded(StatusContext c) {
        Pokemon p = c.target;
        Pokemon r = c.source;
        StatusConditionID id = StatusConditionID.Seeded;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.STATUS_AFTER};

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
        StatusConditionID id = StatusConditionID.Forced_Move;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.STATUS_AFTER};

        Counter counter = new Counter(n);

        p.getEvents().addEventListener(flags[0], name, e -> p.setMoveSelected(m));
        p.getEvents().addEventListener(flags[1], name, e -> {
            if (counter.inc()) p.getConditions().removeCondition(id); 
        });

        return new StatusCondition(p, name, flags);
    }

    /*
     * Pokemon concentrates energy, and then attacks on the next turn
     * If the Pokemon is hit with an attack, it loses its focus and
     * the attack will fail.
     */
    public static StatusCondition focused(StatusContext c) {
        Pokemon p = c.target;
        Move m = c.move;
        StatusConditionID id = StatusConditionID.Focused;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.USE_MOVE, EventID.DEF_MOVE_HITS};

        State state = new State(); // State True: Lost Focus
    
        p.getEvents().addEventListener(flags[0], name, e -> p.setMoveSelected(m));

        p.getEvents().addEventListener(flags[1], name, e -> {
            p.getConditions().removeCondition(id);
            if (state.getBool()) {
                throw new PokemonCannotActException("%s lost it's focus and couldn't move!", p);
            }     
        });

        p.getEvents().addEventListener(flags[2], name, e -> {
            state.set(true);
        });

        return new StatusCondition(p, name, flags);
    }

    // Grounded Pokemon are vulnerable to Ground-Type moves (even Flying-Types) 
    public static StatusCondition grounded(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Grounded;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.DEF_MOVE_EFFECTIVENESS};

        p.getEvents().addEventListener(flags[0], name, e -> {
            if (e.moveUsed.isType(Type.Ground) && e.moveEffectiveness == 0) {
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
        StatusConditionID id = StatusConditionID.Rampage;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.END_OF_TURN, EventID.MOVE_INTERRUPTED};

        Counter counter = new Counter(RandomValues.generateInt(2, 3));

        p.getEvents().addEventListener(flags[0], name, e -> p.setMoveSelected(m));
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
        StatusConditionID id = StatusConditionID.Charge;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.MOVE_SELECTION, EventID.MOVE_INTERRUPTED};

        p.getEvents().addEventListener(flags[0], name, e -> {
            p.setMoveSelected(m);
        });
        p.getEvents().addEventListener(flags[1], name, e -> {
            p.getConditions().removeCondition(id);
        });

        return new StatusCondition(p, id.name(), flags);
	}

    public static StatusCondition endure(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Endure;
        String name = id.name();
        EventID[] flags = new EventID[] {EventID.DEF_MOVE_DEALS_DAMAGE, EventID.STATUS_AFTER};

        return new StatusCondition(p, name, flags);
    }
    
// Public Class Methods
    public static String failMessage(StatusConditionID id) {
        return switch (id) {
            case StatusConditionID.Burn -> "is already burned!";
            case StatusConditionID.Infect -> "is already infected!";
            case StatusConditionID.Freeze -> "is already frozen!";
            case StatusConditionID.Paralysis -> "is already paralyzed!";
            case StatusConditionID.Poison, StatusConditionID.Bad_Poison -> "is already poisoned!";
            case StatusConditionID.Sleep -> "is already asleep!";
            
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR + id);
        };
    }

    // Message displayed when the condition expires
    public static String expireMessage(StatusConditionID id) {
        return switch (id) {
            case StatusConditionID.Freeze -> " thawed!";
            case StatusConditionID.Sleep -> " woke up!";
            case StatusConditionID.Bound -> " was freed!";
            case StatusConditionID.Confusion -> " snapped out of confusion!";
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR + id);
        };
    }
    
}
