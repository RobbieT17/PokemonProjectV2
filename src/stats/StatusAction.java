package stats;

import battle.BattleLog;
import battle.PokemonCannotActException;
import java.util.Random;
import pokemon.Pokemon;

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
            BattleLog.add(String.format("%s took %d damage from the burn!", p, damage));
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
            if (new Random().nextDouble() <= 0.2) {
                p.setImmobilized(false);
                p.clearPrimaryCondition();
                BattleLog.add(String.format("%s thawed!", p));
                return;
            }

            p.setImmobilized(true);
            p.setCharge(false);
            throw new PokemonCannotActException(String.format("%s is frozen solid!", p));
        };
        
        return new StatusCondition(StatusCondition.FREEZE, action, true);
    }

    /**
     * Paralyzed Pokemon have reduced speed (50% decrease) 
     * and have a 33% chance to be unactionable for the turn.
     * The effect is indefinite.
     * @return new Paralysis StatusCondition
     */
    public static StatusCondition paralysis() {
        StatusAction action = p -> {
            BattleLog.add(String.format("%s is paralyzed!", p));
            if (new Random().nextDouble() > 0.33) return;   
            throw new PokemonCannotActException(String.format("%s cannot move!", p));                 
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
            BattleLog.add(String.format("%s took %d damage from the poison!", p, damage));
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
    public static StatusCondition sleep(int duration) {
        Counter counter = new Counter(duration);

        StatusAction action = p -> {
            counter.inc();
            if (counter.terminated()) {
                p.setImmobilized(false);
                p.clearPrimaryCondition();
                BattleLog.add(String.format("%s woke up!", p));
                return;
            } 

            p.setImmobilized(true);
            p.setCharge(false);
            throw new PokemonCannotActException(String.format("%s is fast asleep...", p));    
        };

        return new StatusCondition(StatusCondition.SLEEP, action, true);
    }

    // Gets the StatusAction based on the condition's ID number
    public static StatusCondition getId(int i) {
        return switch (i) {
            case StatusCondition.BURN -> burn();   
            case StatusCondition.FREEZE -> freeze();        
            case StatusCondition.PARALYSIS -> paralysis();   
            case StatusCondition.POISON -> poison();
            default -> throw new IllegalArgumentException("Invalid condition id");
        };
    }

}
