package project.game.pokemon.effects;

import java.util.function.Function;

import project.config.GameConfig;
import project.game.battle.BattleLog;
import project.game.battle.BattlePosition;
import project.game.event.GameEvents.EventID;
import project.game.event.Observer;
import project.game.exceptions.MoveInterruptedException;
import project.game.exceptions.PokemonCannotActException;
import project.game.exceptions.PokemonFaintedException;
import project.game.move.Move;
import project.game.move.Move.MoveCategory;
import project.game.move.Move.MoveTarget;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.calculations.MoveEffectiveCalculations;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Type;
import project.game.utility.Counter;
import project.game.utility.RandomValues;
import project.game.utility.State;
import project.game.pokemon.effects.StatusCondition.StatusConditionID;

public interface StatusConditionManager {

    private static void checkIfFaints(Pokemon p) {
        if (p.getConditions().isFainted()) throw new PokemonFaintedException();
    }

    private static StatusCondition applyConditionToPokemon(Pokemon p, StatusConditionID id, EventID[] flags, Observer[] obs) {
        // Add event listeners to target
        if (flags.length != obs.length) {
            throw new IllegalArgumentException("Mismatch is flags and observers list, must be same length");
        }

        for (int i = 0; i < flags.length; i++) {
            p.getEvents().addEventListener(flags[i], id.name(), obs[i]);
        }

        return new StatusCondition(p, id, flags);
    }

    public static Boolean condBurn(Pokemon p) {
        return !p.isType(Type.Fire);
    }

    public static Boolean condFreeze(Pokemon p) {
        return !p.isType(Type.Ice);
    }

    public static Boolean condInfect(Pokemon p) {
        return !p.isType(Type.Zombie);
    }

    public static Boolean condParalysis(Pokemon p) {
        return !p.isType(Type.Electric);
    }

    public static Boolean condPoison(Pokemon p) {
        return !(p.isType(Type.Poison) || p.isType(Type.Steel));
    }

    public static Boolean condSleep(Pokemon p) {
        return !p.isType(Type.Digital);
    } 

    public static Boolean condFlinch(Pokemon p) {
        return !p.getConditions().hasMoved();
    }

    public static Boolean condSeeded(Pokemon p) {
        return !p.isType(Type.Grass);
    }

    public static Boolean condLink(Pokemon p) {
        return GameConfig.DOUBLES_MODE_ENABLED && p.hasNullTarget();
    }

    /*
     * Burned Pokemon lose 1/16 of their max HP at the end of each round.
     * Reduces physical-move damage by 50%
     */
    public static StatusCondition addBurn(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Burn;

        EventID[] flags = Effect.setFlags(EventID.PRIMARY_STATUS_AFTER, EventID.ATK_DAMAGE_MULTIPLIER);

        Observer[] observers = Effect.setObservers(
            e -> {
                p.takeDamagePercentMaxHP(100.0 / 16.0, " from the burn");
                checkIfFaints(p);
            },
            e -> {
                if (e.moveUsed.isCategory(MoveCategory.Physical)) {
                    e.otherDamageMods *= 0.5;
                }
        });

        BattleLog.add("%s was burned!", p);
        return applyConditionToPokemon(p, id, flags, observers);
    }

    /*
     * Frozen Pokemon cannot act or dodge attacks
     * At the beginning of each round there is a 20%
     * chance for the Pokemon to thaw
     * 
     * Moves that remove the effect:
     * - Flare Blitz
     */
    public static StatusCondition addFreeze(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Freeze;

        EventID[] flags = Effect.setFlags(EventID.PRIMARY_STATUS_BEFORE);

        Observer[] observers = Effect.setObservers(
            e -> {
                if (p.getMoveSelected().getMoveID() == 815) {
                    return;
                }

                if (RandomValues.chance(20)) {
                    p.getConditions().setImmobilized(false);
                    p.getConditions().clearPrimary();
                    BattleLog.add("%s thawed!", p);
                    return;
                }
                p.getConditions().setImmobilized(true);
                throw new PokemonCannotActException("%s is frozen solid!", p);
            }
        );

        BattleLog.add("%s froze!", p);
        return applyConditionToPokemon(p, id, flags, observers);
    }

    /*
     * Infected Pokemon lose 1/12 of their max HP at the end of each round
     * and reduces special-move power by 50%. If the infected Pokemon (unless a Zombie-Type) 
     * uses a contact move, the targeted Pokemon has a 10% chance to also be infected.
     *  
     * If the infected Pokemon is a Zombie-Type, their speed is doubled instead.
     * Only Non-Zombie Pokemon can infect Zombie-Type Pokemon.
     */
     public static StatusCondition addInfect(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Infect;

        EventID[] flags = Effect.setFlags(
            EventID.ATK_MOVE_MAKES_CONTACT, EventID.ATK_DAMAGE_MULTIPLIER, 
            EventID.FIND_MOVE_ORDER, EventID.PRIMARY_STATUS_AFTER
        );

        Observer[] observers = Effect.setObservers(
            // Move makes contact: Infected Pokemon infects their targert
            e -> {
                Pokemon t = e.attackTarget;

                if (!p.isType(Type.Zombie) && !t.getConditions().hasPrimary()) {
                    // Rolls for infection
                    if (RandomValues.chance(10)) {
                        StatusContext context = new StatusContext(id, t);
                        context.applyEffectToTarget();
                    }
                    
                }
                
            },
            // Damage Multiplier: Special Moves deal 50% less damage
            e -> {
                if (!p.isType(Type.Zombie) && e.moveUsed.isCategory(Move.MoveCategory.Special)) {
                    e.otherDamageMods *= 0.5;
                }
            },
            // Turn Order: Infected Zombie-Type Pokemon have double speed
            e -> { 
                if (p.isType(Type.Zombie)) {
                    p.getSpeed().setMod(200);
                }
            },
            // End of Round: Infected Non-Zombie-Type Pokemon lose 1/12 of their max HP
            e -> {
                if (!p.isType(Type.Zombie)) {
                    p.takeDamagePercentMaxHP(100.0 / 12.0, " from the infection");
                }
                
            }
        );

        BattleLog.add("%s was infected!", p);
        return applyConditionToPokemon(p, id, flags, observers);
     }

    /*
     * Paralyzed Pokemon have 50% reduced speed.
     * There is a 33% chance the Pokemon cannot 
     * act for the round.
     */
    public static StatusCondition addParalysis(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Paralysis;

        EventID[] flags = Effect.setFlags(EventID.PRIMARY_STATUS_BEFORE, EventID.FIND_MOVE_ORDER);

        Observer[] observers = Effect.setObservers(
            // Before Move Effect: Chance Pokemon is immobilized before moving
            e -> {
                if (!RandomValues.chance(50)) return;
                throw new PokemonCannotActException("%s is paralyzed and cannot move!", p);                 
            },
            // Turn Order: Speed is cut in half
            e -> {
                p.getSpeed().setMod(50);
            }
        );

        BattleLog.add("%s was paralyzed!", p);
        return applyConditionToPokemon(p, id, flags, observers);
    }

    // Poisoned Pokemon lose 1/8 of their max HP at the end of each round
    public static StatusCondition addPoison(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Poison;

        EventID[] flags = Effect.setFlags(EventID.PRIMARY_STATUS_AFTER);

        Observer[] observers = Effect.setObservers(
            e -> {
                p.takeDamagePercentMaxHP(12.5, " from the poison");
                checkIfFaints(p);
            }
        );

        BattleLog.add("%s was poisoned!", p);
        return applyConditionToPokemon(p, id, flags, observers);
    }

    /*
     * Badly poisoned lose x/16 of their max HP at the end of each round
     * where x is the number of turns after being poisoned
     */
    public static StatusCondition addBadPoison(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Bad_Poison;
        Counter counter = new Counter();

        EventID[] flags = Effect.setFlags(EventID.PRIMARY_STATUS_AFTER);

        Observer[] observers = Effect.setObservers(
            e -> {
                counter.inc();
                p.takeDamagePercentMaxHP(100 * counter.getCount() / 16.0, " from the poison");
                checkIfFaints(p);
            }
        );

        BattleLog.add("%s was badly poisoned!", p);
        return applyConditionToPokemon(p, id, flags, observers);
    }

    // Pokemon is asleep for 1 to 3 turns and cannot act
    public static StatusCondition addSleep(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Sleep;
        Counter counter = new Counter(RandomValues.generateInt(1, 3));

        EventID[] flags = Effect.setFlags(EventID.PRIMARY_STATUS_BEFORE);

        Observer[] observers = Effect.setObservers(
            e -> {
                if (counter.inc()) {
                    p.getConditions().setImmobilized(false);
                    p.getConditions().clearPrimary();
                    BattleLog.add("%s woke up!", p);
                    return;
                } 
                p.getConditions().setImmobilized(true);
                if (p.getMoveSelected().getMoveID() == 214) return; // 214: Sleep Talk
                throw new PokemonCannotActException("%s is fast asleep...", p);    
            }
        );

        BattleLog.add("%s fell asleep!", p);
        return applyConditionToPokemon(p, id, flags, observers);
    }

    /**
     * Pokemon is in a semi-invulnerable state and most moves
     * will miss. Any exceptions will still hit the target 
     * in this state
     */
    public static StatusCondition addSemiImmune(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Semi_Immune;

        EventID[] flags = Effect.setFlags(EventID.DEF_MOVE_ACCURACY);

        Observer[] observers = Effect.setObservers(
            e -> {
                for (int exception : c.exceptions) {
                    if (e.moveUsed.getMoveID() == exception) {
                        return;
                    }
                }

                throw new MoveInterruptedException(c.message);      
            }
        );

        return applyConditionToPokemon(p, id, flags, observers);
	}

    
    // Pokemon flinches and can't act for the round
    public static StatusCondition addFlinch(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Flinch;

        EventID[] flags = Effect.setFlags(EventID.BEFORE_MOVE);

        Observer[] observers = Effect.setObservers(
            e -> {
                p.getConditions().removeCondition(id);
                throw new PokemonCannotActException("%s flinched and couldn't move!", p);
            }
        );

        return applyConditionToPokemon(p, id, flags, observers);
    }

    // Traps Pokemon from escaping battle and loses 1/8 of max HP
    public static StatusCondition addBound(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Bound;
        Counter counter = new Counter(RandomValues.generateInt(2, 5));


        EventID[] flags = Effect.setFlags(EventID.STATUS_AFTER, EventID.SWITCH_OUT);

        Observer[] observers = Effect.setObservers(
            // After Round: Take Damage
            e -> {
                if (counter.inc()){
                    p.getConditions().removeCondition(id);
                    return;
                }

                p.takeDamagePercentMaxHP(12.5, " from the bound");
                checkIfFaints(p);
            },
            // Switch Out: Prevents switch-out
            e -> {
                System.out.println("Expected behavior: Pokemon cannot switch out because of the bound");
            }
        );

        BattleLog.add("%s was trapped in a bound!", p);
        return applyConditionToPokemon(p, id, flags, observers);
    }

    /*
     * Confused Pokemon have a 50% chance to damage themselves
     * Confusion last for 2-4 rounds.
     */
    public static StatusCondition addConfusion(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Confusion;
        Counter counter = new Counter(RandomValues.generateInt(2, 4));
        
        EventID[] flags = Effect.setFlags(EventID.STATUS_BEFORE);

        Observer[] observers = Effect.setObservers(
            e -> {
                if (counter.inc()){
                    p.getConditions().removeCondition(id);
                    return;
                }
                BattleLog.add("%s is confused!", p);
                if (!RandomValues.chance(50)) return;

                MoveActionAttack.takeConfusionDamage(p);

             
                checkIfFaints(p);
                throw new PokemonCannotActException();
            }
        );

        BattleLog.add("%s became confused!", p);
        return applyConditionToPokemon(p, id, flags, observers);
    }

    /**
     * A Pokemon is seeded! This means the opposing
     * Pokemon in battle drains HP from the bearer
     * at the end of each round.
     */
    public static StatusCondition addSeeded(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Seeded;
        BattlePosition pos = c.source;

        EventID[] flags = Effect.setFlags(EventID.STATUS_AFTER);

        Observer[] observers = Effect.setObservers(
            e -> {
                int damage = (int) (p.getHp().getMaxHealthPoints() / 8.0);
                Pokemon r = pos.getCurrentPokemon();

                if (r != null) {
                    BattleLog.add("%s drained %d HP from %s!", r, damage, p);
                    r.restoreHP(damage);
                    p.takeDamage(damage);
                    checkIfFaints(p);
                }
                
            }
        );

        BattleLog.add("%s was seeded!", p);
        return applyConditionToPokemon(p, id, flags, observers);
    }

    // Forces Pokemon to use the move chosen for n rounds
    public static StatusCondition addForcedMove(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Forced_Move;
        Move m = c.move;
        Counter counter = new Counter(c.count);
        
        EventID[] flags = Effect.setFlags(EventID.MOVE_SELECTION, EventID.STATUS_AFTER);

        Observer[] observers = Effect.setObservers(
            // Move Selection: Auto-select forced move
            e -> {
                p.setMoveSelected(m);
            },
            // After Round: Increase counter, expires after n rounds
            e -> {
                if (counter.inc()) {
                    p.getConditions().removeCondition(id); 
                }
            }
        );
    
        return applyConditionToPokemon(p, id, flags, observers);
    }

    /*
     * Pokemon concentrates energy, and then attacks on the next turn
     * If the Pokemon is hit with an attack, it loses its focus and
     * the attack will fail.
     */
    public static StatusCondition focused(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Focused;
        Move m = c.move;
        State state = new State(); // State True: Lost Focus
        
        EventID[] flags = Effect.setFlags(EventID.MOVE_SELECTION, EventID.USE_MOVE, EventID.DEF_MOVE_HITS);

        Observer[] observers = Effect.setObservers(
            // Move Selection: Auto-selects focus move
            e -> {
                p.setMoveSelected(m);
            },
            // Use Move: Attempts to use move if focus remains
            e -> {
                p.getConditions().removeCondition(id);
                if (state.getBool()) {
                    throw new PokemonCannotActException("%s lost it's focus and couldn't move!", p);
                }     
            },
            // Hit by Move: Loses focus
            e -> {
                state.set(true);
            }
        );
        
        return applyConditionToPokemon(p, id, flags, observers);
    }

    // Grounded Pokemon are vulnerable to Ground-Type moves (even Flying-Types) 
    public static StatusCondition addGrounded(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Grounded;

        EventID[] flags = Effect.setFlags(EventID.DEF_MOVE_EFFECTIVENESS);

        Observer[] observers = Effect.setObservers(
            e -> {
                if (e.moveUsed.isType(Type.Ground) && e.moveEffectiveness == 0) {
                    e.moveEffectiveness = 1.0;
                }
            }
        );

        return applyConditionToPokemon(p, id, flags, observers);
	}

    // TODO: Make this a bool value
    public static StatusCondition addEndure(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Endure;
        EventID[] flags = Effect.setFlags(EventID.DEF_MOVE_DEALS_DAMAGE, EventID.STATUS_AFTER);

        return null;
    }

    // A glitched Pokemon cannot use the move it last used when 
    // the effect was applied for 3-5 turns
    public static StatusCondition addGlitch(StatusContext c) {
        Pokemon p = c.target; 
        StatusConditionID id = StatusConditionID.Glitch;
        Move m = p.getLastMove();
        Counter counter = new Counter(RandomValues.generateInt(3, 5));

        EventID[] flags = Effect.setFlags(EventID.MOVE_SELECTION, EventID.END_OF_ROUND);

        Observer[] observers = Effect.setObservers(
            // Move Selection: Disables glitched move
            e -> {
                if (m != null) {
                    m.disable();
                }
            },
            e -> {
                if (counter.inc()) {
                    m.enable();
                    p.getConditions().removeCondition(id);
                    BattleLog.add("%s's %s is no longer disable!", p, m);
                }
            }
        );

        m.disable();
        BattleLog.add("%s's %s was disabled!", p, m);
        return applyConditionToPokemon(p, id, flags, observers);

    }

    // Reduces the damage the user receives from Dark-type moves by 50% for 5 turns. 
    public static StatusCondition addVpn(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Vpn;
        Counter counter = new Counter(5);

        EventID[] flags = Effect.setFlags(EventID.DEF_DAMAGE_MULTIPLIER, EventID.END_OF_ROUND);

        Observer[] observers = Effect.setObservers(
            // Damage Multiplier: Reduces Dark-Type attacks by 50%
            e -> {
                if (e.moveUsed.isType(Type.Dark)) {
                    e.otherDamageMods *= 0.5;
                    BattleLog.add("%s VPN reduces the damage from the Dark-Type attack!", p);
                }
            },
            // After Round: Increase counter
            e -> {
                if (counter.inc()) {
                    p.getConditions().removeCondition(id);
                    BattleLog.add("%s's VPN has expired!");
                }
            }
        );

        BattleLog.add("%s strengthens its defense against Dark-Type attack!");
        return applyConditionToPokemon(p, id, flags, observers);
    }

    // The user links with their ally for 5 turns. Shares all stat changes, healing (50/50), damage recieved from direct attacks (60/40), 
    // and some benefical status effects
    public static StatusCondition addLink(StatusContext c) {
        Pokemon p = c.target;
        StatusConditionID id = StatusConditionID.Link;
        Pokemon linkPartner = c.target2;
        Counter counter = new Counter(5);
        
        EventID[] flags = Effect.setFlags(
            EventID.DEF_MOVE_DEALS_DAMAGE, 
            EventID.DEF_STAT_CHANGE, 
            EventID.DEF_STATUS_CONDITION_CHANGE,
            EventID.RESTORE_HEALTH,
            EventID.SWITCH_OUT,
            EventID.END_OF_ROUND);

        Observer[] observers = Effect.setObservers(
            // Move Deals Damage: Split damage with link partner
            e -> {
                // Ignore multiple target moves or fainted partner
                if (!e.moveUsed.isTarget(MoveTarget.Single_Adjacent) || linkPartner.getConditions().isFainted()) {
                    return;
                }

                // 40% of damage recieved is absorbed by the linked partner (applies effectiveness bonus)
                double effectiveness = MoveEffectiveCalculations.typeEffectiveness(e.moveUsed.getMoveType(), linkPartner);
                int linkDamage = (int) (e.damage * 0.4 * effectiveness);

                BattleLog.add("%d damage points were redirected to %s!", linkDamage, linkPartner);
                
                linkPartner.takeDamage(linkDamage);
                linkPartner.getConditions().setTookDamage(true);
                
                // Move target receives 60% of damage dealt
                int damage = (int) (e.damage * 0.6);
                e.damage = damage;

            },
            // Stat Change: Split stat changes
            e -> {
                // Ignore if partner fainted
                if (linkPartner.getConditions().isFainted()) {
                    return;
                }

                BattleLog.add("%s shared the status changes with %s!", p, linkPartner);

                for (int i = 0; i < e.statChanges.length; i++) {
                    int value = e.statChanges[i];
                    
                    if (value != 0) {
                        // Changes of more than 1 stage are divided by 2 rounded up
                        int newValue = Math.abs(value) > 1 ? (int) Math.floor((double) value / 2) : value;
                        
                        // Updates event data point
                        e.statChanges[i] = newValue;  

                        // Changes stage for partner
                        boolean success = linkPartner.getStats()[i].changeStage(newValue);
                        MoveActionChangeStat.logStatChange(success, linkPartner, linkPartner.getStats()[i], newValue);
                    
                                
                    }  
                    
                }
                
            },
            // Status Condition Change: Copies linkable status effects to partner
            e -> {
                // Ignore if partner fainted
                if (linkPartner.getConditions().isFainted()) {
                    return;
                }

                StatusConditionID[] linkableIds = new StatusConditionID[] {
                    StatusConditionID.Confusion,
                    StatusConditionID.Vpn
                };
                
                boolean validIdFound = false;

                // Checks if status change is a linkable
                for (StatusConditionID linkId : linkableIds) {
                    if (linkId == e.statusContext.id) {
                        validIdFound = true;
                        break;
                    }
                }

                if (!validIdFound) {
                    return;
                }

                // Applies status condition to partner
                StatusContext context = new StatusContext(e.statusContext.id, linkPartner);
                context.id.addCondition(context);
                
            },
            // Restore Health: Split healing with partner in half
            e -> {
                if (linkPartner.getConditions().isFainted()) {
                    return;
                }

                e.healAmount = Math.max(1, e.healAmount / 2);
                linkPartner.restoreHP(e.healAmount);
                BattleLog.add("%s shared %d points of health with %s!", p, e.healAmount, linkPartner);
            },
            // Switch Out: Breaks link with partner
            e -> {
                p.getConditions().removeCondition(id); // Partner should receive the same event notification and removes its own condition instance
            },
            // After Round: Increase counter (end early if partner faints)
            e -> {
                if (counter.inc() || linkPartner.getConditions().isFainted()) {
                    p.getConditions().removeCondition(id);
                    BattleLog.add("%s's link with %s was broken!", p, linkPartner);
                }
            }
        );

        // Creates a copy condition instasnce for linked partner
        if (!c.isSlave) {
            StatusContext newC = new StatusContext(id, linkPartner);
            newC.target2 = p;
            newC.isSlave = true;
            newC.id.addCondition(newC);
        }

        BattleLog.add(!c.isSlave ? String.format("%s and %s are linked together!", p, linkPartner) : "");
        return applyConditionToPokemon(p, id, flags, observers);
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
