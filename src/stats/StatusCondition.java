package stats;

import battle.BattleLog;
import event.EventData;
import event.GameEvent;
import event.Observer;
import exceptions.PokemonCannotActException;
import exceptions.PokemonFaintedException;
import exceptions.StatusFailedException;
import move.Move;
import move.MoveAction;
import pokemon.Pokemon;
import utility.Counter;
import utility.RandomValues;

public class StatusCondition extends Effect {
// Error Messages
    public static final String ID_ERR = "Invalid status condition ID";

// Public Class Methods

    // Non-Volatile
    public static final String BURN_ID = "BURNED";
    public static final String FREEZE_ID = "FROZEN";
    public static final String PARALYSIS_ID = "PARALYZED";
    public static final String POISON_ID = "POISONED";
    public static final String BAD_POISON_ID = "BADLY POISONED";
    public static final String SLEEP_ID = "ASLEEP";

    // Volatile
    public static final String FLINCH_ID = "Flinched";
    public static final String BOUND_ID = "Trapped";
    public static final String CONFUSION_ID = "Confused";
    public static final String SEEDED_ID = "Seeded";

    // Semi-Invulnerable State
    public static final String NO_INVUL_ID = "Normal State";
    public static final String FLY_ID = "Flying State";
    public static final String DIG_ID = "Underground State";
    public static final String DIVE_ID = "Underwater State";
    
// Object
    public StatusCondition(Pokemon p, String name, String[] flags) {
        super(p, name, flags);
    }


    @Override
    public void removeEffect() {
        this.bearer().events().removeEventSubscribers(this.flags(), this.effectName());
        this.bearer().events().removeEventSubscriber(GameEvent.STATUS_CONDITION_CHANGE, this.effectName());
    }

// Static Methods
    private static void checkIfFaints(Pokemon p) {
        if (p.conditions().fainted()) throw new PokemonFaintedException();
    }

    /*
     * All conditions update on the events where conditions are applied
     * 
     */
    private static void canApplyStatus(Pokemon p, String id, Observer o) {
        p.events().addEventSubscriber(GameEvent.STATUS_CONDITION_CHANGE, id, o);
    }

    /*
     * Burned Pokemon lose 1/8 of their max HP at the end of each round.
     * Reduces physical move damage by 50%
     */
    public static StatusCondition burn(Pokemon p) {
        String name = StatusCondition.BURN_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND, GameEvent.DAMAGE_MULTIPLIER};

        p.events().addEventSubscriber(flags[0], name, e -> {
            int damage = (int) (p.hp().max() / 16.0);
            BattleLog.add("%s took %d damage from the burn!", p, damage);
            p.takeDamage(damage);
            checkIfFaints(p);
        });

        p.events().addEventSubscriber(flags[1], name, e -> {
            if (!EventData.isUser(p, e))
            e.moveUsed.changePowerByPercent(50);
        });

        /*
         * Cannot be burned if
         * 1) Pokemon is a Fire-Type
         * 2) Pokemon already has a primary condition
         */
        canApplyStatus(p, name, e -> {
            if (!e.statusChange.equals(name)) return;
            if (p.isType(Type.FIRE)) throw new StatusFailedException(Move.noEffectOn(p));
            if (p.hasPrimaryCondition()) throw new StatusFailedException(Move.FAILED);      
        });

        return new StatusCondition(p, name, flags);
    }

    /*
     * Frozen Pokemon cannot act or dodge attacks
     * At the beginning of each round there is a 20%
     * chance for the Pokemon to thaw
     */
    public static StatusCondition freeze(Pokemon p) {
        String name = StatusCondition.FREEZE_ID;
        String[] flags = new String[] {GameEvent.STATUS_BEFORE, GameEvent.MOVE_ACCURACY};

        p.events().addEventSubscriber(flags[0], name, e -> {
            if (p.moveSelected().moveID() == 815) return;

            if (RandomValues.chance(20)) {
                p.conditions().setImmobilized(false);
                p.clearPrimaryCondition(StatusCondition.FREEZE_ID);
                return;
            }
            p.conditions().setImmobilized(true);
            throw new PokemonCannotActException("%s is frozen solid!", p);
        });

        /*
         * Cannot be frozen if
         * 1) Pokemon is an Ice-Type
         * 2) Pokemon already has a primary condition
         */
        canApplyStatus(p, name, e -> {
            if (!e.statusChange.equals(name)) return;
            if (p.isType(Type.ICE)) throw new StatusFailedException(Move.noEffectOn(p));
            if (p.hasPrimaryCondition()) throw new StatusFailedException(Move.FAILED);      
        });

        return new StatusCondition(p, name, flags);
    }

    /*
     * Paralyzed Pokemon have 50% reduced speed.
     * There is a 33% chance the Pokemon cannot 
     * act for the round.
     */
    public static StatusCondition paralysis(Pokemon p) {
        String name = StatusCondition.PARALYSIS_ID;
        String[] flags = new String[] {GameEvent.STATUS_BEFORE, GameEvent.FIND_MOVE_ORDER};

        p.events().addEventSubscriber(flags[0], name, e -> {
            if (!RandomValues.chance(50)) return;
            throw new PokemonCannotActException("%s is paralyzed and cannot move!", p);                 
        });

        p.events().addEventSubscriber(flags[1], name, e -> p.modifySpeedByPercent(50));

        /*
         * Cannot be paralyzed if
         * 1) Pokemon is an Electric-Type
         * 2) Pokemon already has a primary condition
         */
        canApplyStatus(p, name, e -> {
            if (!e.statusChange.equals(name)) return;
            if (p.isType(Type.ELECTRIC)) throw new StatusFailedException(Move.noEffectOn(p));
            if (p.hasPrimaryCondition()) throw new StatusFailedException(Move.FAILED);      
        });

        return new StatusCondition(p, name, flags);
    }

    
    // Poisoned Pokemon lose 1/8 of their max HP at the end of each round
    public static StatusCondition poisoned(Pokemon p) {
        String name = StatusCondition.POISON_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND};

        p.events().addEventSubscriber(flags[0], name, e -> {
            int damage = (int) (p.hp().max() / 8.0);
            BattleLog.add("%s took %d damage from the poison!", p, damage);
            p.takeDamage(damage);
            checkIfFaints(p);
        });

         /*
         * Cannot be poisoned if
         * 1) Pokemon is a Poison-Type
         * 2) Pokemon already has a primary condition
         */
        canApplyStatus(p, name, e -> {
            if (!e.statusChange.equals(name)) return;
            if (p.isType(Type.POISON) || p.isType(Type.STEEL)) throw new StatusFailedException(Move.noEffectOn(p));
            if (p.hasPrimaryCondition()) throw new StatusFailedException(Move.FAILED);      
        });

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

        p.events().addEventSubscriber(flags[0], name, e -> {
            counter.inc();
            int damage = (int) (p.hp().max() * (counter.count() / 16.0));
            BattleLog.add("%s took %d damage from the poison!", p, damage);
            p.takeDamage(damage);
            checkIfFaints(p);
        });

        /*
         * Cannot be badly poisoned if
         * 1) Pokemon is a Poison-Type
         * 2) Pokemon already has a primary condition
         */
        canApplyStatus(p, name, e -> {
            if (!e.statusChange.equals(name)) return;
            if (p.isType(Type.POISON) || p.isType(Type.STEEL)) throw new StatusFailedException(Move.noEffectOn(p));
            if (p.hasPrimaryCondition()) throw new StatusFailedException(Move.FAILED);      
        });

        return new StatusCondition(p, name, flags);
    }

    // Pokemon is asleep for 1 to 3 turns and cannot act
    public static StatusCondition sleep(Pokemon p) {
        String name = StatusCondition.SLEEP_ID;
        String[] flags = new String[] {GameEvent.STATUS_BEFORE};

        Counter counter = new Counter(RandomValues.generateInt(1, 3));

        p.events().addEventSubscriber(flags[0], name, e -> {
            counter.inc();
            if (counter.terminated()) {
                p.conditions().setImmobilized(false);
                p.clearPrimaryCondition(StatusCondition.SLEEP_ID);
                return;
            } 
            p.conditions().setImmobilized(true);
            if (p.moveSelected().moveID() == 214) return; // 214: Sleep Talk
            throw new PokemonCannotActException("%s is fast asleep...", p);    
        });

        /*
         * Cannot fall asleep if
         * 1) Pokemon is a Digital-Type
         * 2) Pokemon already has a primary condition
         */
        canApplyStatus(p, name, e -> {
            if (!e.statusChange.equals(name)) return;
            if (p.isType(Type.DIGITAL)) throw new StatusFailedException(Move.noEffectOn(p));
            if (p.hasPrimaryCondition()) throw new StatusFailedException(Move.FAILED);      
        });

        return new StatusCondition(p, name, flags);
    }

    // Pokemon flinches and can't act for the round
    public static StatusCondition flinch(Pokemon p) {
        String name = StatusCondition.FLINCH_ID;
        String[] flags = new String[] {GameEvent.BEFORE_MOVE};

        p.events().addEventSubscriber(flags[0], name, e -> {
            p.clearCondition(StatusCondition.FLINCH_ID);
            throw new PokemonCannotActException("%s flinched and couldn't move!", p);
        });

        // Cannot Flinch if Pokemon already moved that round
        canApplyStatus(p, name, e -> {
            if (!e.statusChange.equals(name)) return;
            if (p.conditions().hasMoved()) throw new StatusFailedException();      
        });

        return new StatusCondition(p, name, flags);
    }

    // Traps Pokemon from escaping battle and loses 1/8 of max HP
    public static StatusCondition bound(Pokemon p) {
        String name = StatusCondition.BOUND_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND, GameEvent.SWITCH_OUT};

        Counter counter = new Counter(RandomValues.generateInt(2, 5));

        p.events().addEventSubscriber(flags[0], name, e -> {
            counter.inc();
            if (counter.terminated()){
                p.clearCondition(StatusCondition.BOUND_ID);
                return;
            }

            int damage = (int) (p.hp().max() / 16.0);
            BattleLog.add("%s took %d damage from the bound!", p, damage);
            p.takeDamage(damage);
            checkIfFaints(p);

        });

        // Fails if already trapped
        canApplyStatus(p, name, e -> {
            if (!e.statusChange.equals(name)) return;
            if (p.hasCondition(name)) throw new StatusFailedException();      
        });

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

        p.events().addEventSubscriber(flags[0], name, e -> {
            counter.inc();
            if (counter.terminated()){
                p.clearCondition(StatusCondition.CONFUSION_ID);
                return;
            }
            BattleLog.add("%s is confused!", p);
            if (!RandomValues.chance(50)) return;

            MoveAction.takeConfusionDamage(p);
            checkIfFaints(p);
            throw new PokemonCannotActException();
        });

        // Fails if already confused
        canApplyStatus(p, name, e -> {
            if (!e.statusChange.equals(name)) return;
            if (p.hasCondition(name)) throw new StatusFailedException();      
        });

        return new StatusCondition(p, name, flags);
    }

    public static StatusCondition seeded(Pokemon p, Pokemon r) {
        String name = StatusCondition.SEEDED_ID;
        String[] flags = new String[] {GameEvent.END_OF_ROUND};

        p.events().addEventSubscriber(flags[0], name, e -> {
            int damage = (int) (p.hp().max() / 8.0);
            BattleLog.add("%s drained %d HP from %s!", r, damage, p);

            r.healDamage(damage);
            p.takeDamage(damage);
            checkIfFaints(p);
        });

        // Fails if already seeded or is a Grass-Type
        canApplyStatus(p, name, e -> {
            if (!e.statusChange.equals(name)) return;
            if (p.isType(Type.GRASS)) throw new StatusFailedException(Move.noEffectOn(p));
            if (p.hasCondition(name)) throw new StatusFailedException();      
        });

        return new StatusCondition(p, name, flags);
    }



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
