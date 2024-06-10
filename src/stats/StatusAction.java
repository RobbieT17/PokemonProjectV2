package stats;

import battle.BattleLog;
import exceptions.PokemonCannotActException;
import move.MoveAction;
import pokemon.Pokemon;
import utility.Counter;
import utility.RandomValues;

@FunctionalInterface
public interface StatusAction {

// Object Function
    public void apply(Pokemon p);

// Class Functions
    /**
     * Burned Pokemon take damage equal to 1/16 of the maximum HP
     * at the end of each round. Physical attack power is decreased
     * by 50%. The effect is indefinite.
     * @return new Burn StatusCondition
     */
    public static StatusCondition burn() {
        StatusAction action = p -> {
            int damage = (int) (p.hp().max() / 16.0);
            BattleLog.add("%s took %d damage from the burn!", p, damage);
            p.takeDamage(damage);
        };
        return new StatusCondition(StatusCondition.BURN, action, false);
    }

    /**
     * Frozen Pokemon cannot actor dodge incoming attacks. 
     * Frozen Pokemon have a 20% chance to thaw before
     * they use a move. The effect is indefinite, but end 
     * once the Pokemon thaws.
     * @return new Freeze StatusCondition
     */
    public static StatusCondition freeze() {
        StatusAction action = p -> {
            if (RandomValues.chance(20)) {
                p.conditions().setImmobilized(false);
                p.conditions().clearPrimaryCondition();
                BattleLog.add("%s thawed!", p);
                return;
            }

            p.conditions().setImmobilized(true);
            p.conditions().setCharge(false);
            throw new PokemonCannotActException("%s is frozen solid!", p);
        };
        
        return new StatusCondition(StatusCondition.FREEZE, action, true);
    }

    /**
     * Paralyzed Pokemon have reduced speed (50% decrease) 
     * and have a 50% chance to be unactionable for the turn.
     * The effect is indefinite.
     * @return new Paralysis StatusCondition
     */
    public static StatusCondition paralysis() {
        StatusAction action = p -> {
            BattleLog.add("%s is paralyzed!", p);
            if (RandomValues.chance(50)) return;   
            throw new PokemonCannotActException("%s cannot move!", p);                 
        };
        return new StatusCondition(StatusCondition.PARALYSIS, action, true);
    }

    /**
     * Poisoned Pokemon take damage equal to x/16 of their maximum HP
     * where x is the number of turns its been poisoned for. The effect
     * is indefinite.
     * @return new Poison StatusCondition
     */
    public static StatusCondition poison() {
        Counter counter = new Counter(-1);

        StatusAction action = p -> {
            counter.inc();
            int damage = (int) (p.hp().max() * (counter.count() / 16.0));
            BattleLog.add("%s took %d damage from the poison!", p, damage);
            p.takeDamage(damage);
        };
        return new StatusCondition(StatusCondition.POISON, action, false);
    }

    /**
     * Asleep Pokemon cannot act or dodge incoming attacks.
     * Sleep last for 1-3 rounds before the Pokemon wakes up.
     * 
     * @param duration rounds effect lasts for
     * @return new Sleep StatusCondition
     */
    public static StatusCondition sleep() {
        Counter counter = new Counter(RandomValues.generateInt(1, 3));

        StatusAction action = p -> {
            counter.inc();
            if (counter.terminated()) {
                p.conditions().setImmobilized(false);
                p.conditions().clearPrimaryCondition();
                BattleLog.add("%s woke up!", p);
                return;
            } 

            p.conditions().setImmobilized(true);
            p.conditions().setCharge(false);
            throw new PokemonCannotActException("%s is fast asleep...", p);    
        };

        return new StatusCondition(StatusCondition.SLEEP, action, true);
    }

    /**
     * Confused Pokemon have a 50% chance to damage themselves
     * Confusion last for 2-4 rounds.
     * @param duration rounds effect lasts for
     * @return new Confusion StatusCondition
     */
    public static StatusCondition confusion() {
        Counter counter = new Counter(RandomValues.generateInt(2, 4));

        StatusAction action = p -> {
            counter.inc();
            if (counter.terminated()){
                p.conditions().remove(StatusCondition.CONFUSION);
                BattleLog.add("%s snapped out of confusion!", p);
                return;
            }

            BattleLog.add("%s is confused!", p);
            if (RandomValues.chance(50)) return;

            MoveAction.takeConfusionDamage(p);
            throw new PokemonCannotActException();
        };

        return new StatusCondition(StatusCondition.CONFUSION, action, true);
    }

    // Gets the StatusAction based on the condition's ID number
    public static StatusCondition getCondition(int i) {
        return switch (i) {
            case StatusCondition.BURN -> burn();   
            case StatusCondition.FREEZE -> freeze();        
            case StatusCondition.PARALYSIS -> paralysis();   
            case StatusCondition.POISON -> poison();
            case StatusCondition.SLEEP -> sleep();
            case StatusCondition.CONFUSION -> confusion();
            default -> throw new IllegalArgumentException("Invalid condition id");
        };
    }

}
