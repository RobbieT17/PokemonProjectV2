package stats;

import battle.BattleLog;
import java.util.Random;
import pokemon.Pokemon;

@FunctionalInterface
public interface StatusAction {

    // Object Function
    public void apply(Pokemon p);


    // Class Functions
    public static StatusCondition burn() {
        StatusAction action = p -> {
            int damage = (int) (p.hp().max() / 16.0);
            BattleLog.add(String.format("%s took %d damage from the burn!", p, damage));
            p.takeDamage(damage);
        };
        return new StatusCondition(StatusCondition.BURN, action, false);
    }

    public static StatusCondition freeze() {
        StatusAction action = p -> {
            if (new Random().nextDouble() <= 0.2) {
                p.setImmobilized(false);
                p.setActionable(true);
                p.clearPrimaryCondition();
                BattleLog.add(String.format("%s thawed!", p));
                return;
            }

            p.setImmobilized(true);
            p.setActionable(false);
            p.setCharge(false);
            BattleLog.add(String.format("%s is frozen solid!", p));
        };
        
        return new StatusCondition(StatusCondition.FREEZE, action, true);
    }

    public static StatusCondition paralysis() {
        StatusAction action = p -> {
            BattleLog.add(String.format("%s is paralyzed!", p));
            if (new Random().nextDouble() <= 0.5){
                p.setActionable(false);
                p.setCharge(false);
                BattleLog.add(String.format("%s cannot move!", p));
                return;
            }      
            p.setActionable(true);           
        };
        return new StatusCondition(StatusCondition.PARALYSIS, action, true);
    }

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

    public static StatusCondition sleep(int duration) {
        Counter counter = new Counter(duration);

        StatusAction action = p -> {
            counter.inc();
            if (counter.terminated()) {
                p.setImmobilized(false);
                p.setActionable(true);
                p.clearPrimaryCondition();
                BattleLog.add(String.format("%s woke up!", p));
                return;
            } 

            p.setImmobilized(true);
            p.setActionable(false);
            p.setCharge(false);
            BattleLog.add(String.format("%s is fast asleep...", p));      
        };

        return new StatusCondition(StatusCondition.SLEEP, action, true);
    }

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
