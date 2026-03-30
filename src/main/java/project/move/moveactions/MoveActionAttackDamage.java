package project.move.moveactions;

import java.util.Random;

import project.battle.BattleLog;
import project.event.EventData;
import project.event.GameEvent;
import project.move.calculations.AttackMoveCalculations;
import project.move.calculations.MoveEffectiveCalculations;
import project.pokemon.Pokemon;

public interface MoveActionAttackDamage extends MoveAction {
    
    /**
     * Number of times a multi-hit damages the opponent
     * 
     * 2 turns: 35% chance
     * 3 turns: 35% chance
     * 4 turns: 15% chance
     * 5 turns: 15% chance
     * 
     * @return # of hits
     */
    private static int randomHits() {
        double value = new Random().nextDouble();
        return value <= 0.35 
        ? 2
        : value <= 0.7
            ? 3
            : value <= 0.85
                ? 4
                : 5;
    }

    private static void damageTaken(EventData data, boolean multiHit) {
        Pokemon attacker = data.user;
        Pokemon defender = data.attackTarget;

        int damage = AttackMoveCalculations.calculateDamage(data);
        data.damageDealt += damage;

        BattleLog.add("%s took %d damage!", defender, damage);
        BattleLog.add(!multiHit ? MoveEffectiveCalculations.isSuperEffective(data.moveEffectiveness) : "");
        BattleLog.add(data.criticalHit ? "Critical hit!" : "");

        attacker.addDealtDamage(damage);
        defender.addDamageReceived(damage);
        defender.takeDamage(damage);    

        if (data.moveUsed.getMakesContact()) data.notifyEvent(GameEvent.MOVE_MAKES_CONTACT);
    }

    // Pokemon takes damage based on some percent of the damage dealt
    private static void recoilDamage(EventData data) {
        Pokemon p = data.user;
        if (p.getDamageDealt() == 0) return;

        int damage = (int) (0.01 * data.recoilPercent * p.getDamageDealt()); 
        BattleLog.add("%s took %d damage from the recoil!", p, damage);
        p.addDamageReceived(damage);
        p.takeDamage(damage);
    }

    private static void drainHP(EventData data) {
        Pokemon p = data.user;
        if (p.getDamageDealt() == 0) return;

        int heal = (int) (0.01 * data.drainPercent * p.getDamageDealt()); 
        BattleLog.add("%s restored %d HP!", p, heal);
        p.restoreHP(heal);
    }

    // Deals damage to the target
    public static void dealDamage(EventData data) {       
        MoveEffectiveCalculations.moveEffectiveness(data); 
        MoveActionAccuracy.moveHits(data); 
        damageTaken(data, false);
    }

    // Deals multiple hits of damage
    public static void multiHit(EventData data) {
        MoveEffectiveCalculations.moveEffectiveness(data);
        MoveActionAccuracy.moveHits(data);

        Pokemon defender = data.attackTarget;
        data.hitCount = randomHits();

        for (int i = 0; i < data.hitCount; i++) {
            damageTaken(data, true);
            if (defender.getConditions().isFainted()) {
                data.hitCount = i;
                break;
            }
        }
        BattleLog.add("It hit %d times!", data.hitCount);
        BattleLog.add(MoveEffectiveCalculations.isSuperEffective(data.moveEffectiveness));
    }

    // Deals damage, attacking Pokemon receives a percentage of the damage dealt
    public static void dealDamageRecoil(EventData data, double percent) {
        data.recoilPercent = percent;
        dealDamage(data);
        recoilDamage(data);
    }

    // Deals damage, attacking Pokemon heals from percentage of the damage dealt
    public static void dealDamageDrain(EventData data, double percent) {
        data.drainPercent = percent;
        dealDamage(data);
        drainHP(data);
    }

    /**
     * Confusion damage is calculated as if it were a typeless physical move with a power of 40
     * It cannot score a critical hit, and does not receive STAB
     */
    public static void takeConfusionDamage(Pokemon p) {
        int damage = (int) (((((2 * p.getLevel()) / 5.0 + 2) * 40 * (p.getAttack().getPower() / (double) p.getDefense().getPower())) / 50.0 + 2)); 
        BattleLog.add("%s took %d damage from their own confusion!", p, damage);
        p.addDamageReceived(damage);
        p.takeDamage(damage); 
    }

}
