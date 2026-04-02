package project.game.move.moveactions;

import java.util.Random;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents;
import project.game.move.calculations.AttackMoveCalculations;
import project.game.move.calculations.MoveEffectiveCalculations;
import project.game.pokemon.Pokemon;

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

    private static void damageTaken(EventManager eventManager, boolean multiHit) {
        EventData data = eventManager.eventData;
        Pokemon attacker = data.user;
        Pokemon defender = data.attackTarget;

        int damage = AttackMoveCalculations.calculateDamage(eventManager);
        data.damageDealt += damage;

        BattleLog.add("%s took %d damage!", defender, damage);
        BattleLog.add(!multiHit ? MoveEffectiveCalculations.isSuperEffective(data.moveEffectiveness) : "");
        BattleLog.add(data.criticalHit ? "Critical hit!" : "");

        attacker.addDealtDamage(damage);
        defender.addDamageReceived(damage);
        defender.takeDamage(damage);    

        if (data.moveUsed.getMakesContact()) {
            eventManager.notifyAllPokemon(GameEvents.MOVE_MAKES_CONTACT);
        }
    }

    // Pokemon takes damage based on some percent of the damage dealt
    private static void recoilDamage(EventManager eventManager) {
        EventData data  = eventManager.eventData;
        Pokemon p = data.user;

        if (p.getDamageDealt() == 0) {
            return;
        }

        int damage = (int) (0.01 * data.recoilPercent * p.getDamageDealt()); 
        BattleLog.add("%s took %d damage from the recoil!", p, damage);
        p.addDamageReceived(damage);
        p.takeDamage(damage);
    }

    private static void drainHP(EventManager eventManager) {
        EventData data  = eventManager.eventData;
        Pokemon p = data.user;

        if (p.getDamageDealt() == 0) return;

        int heal = (int) (0.01 * data.drainPercent * p.getDamageDealt()); 
        BattleLog.add("%s restored %d HP!", p, heal);
        p.restoreHP(heal);
    }

    // Deals damage to the target
    public static void dealDamage(EventManager eventManager) {   
        MoveEffectiveCalculations.moveEffectiveness(eventManager); 
        MoveActionAccuracy.moveHits(eventManager); 
        damageTaken(eventManager, false);
    }

    // Deals multiple hits of damage
    public static void multiHit(EventManager eventManager) {
        EventData data  = eventManager.eventData;

        MoveEffectiveCalculations.moveEffectiveness(eventManager);
        MoveActionAccuracy.moveHits(eventManager);

        Pokemon defender = data.attackTarget;
        data.hitCount = randomHits();

        for (int i = 0; i < data.hitCount; i++) {
            damageTaken(eventManager, true);
            if (defender.getConditions().isFainted()) {
                data.hitCount = i;
                break;
            }
        }
        BattleLog.add("It hit %d times!", data.hitCount);
        BattleLog.add(MoveEffectiveCalculations.isSuperEffective(data.moveEffectiveness));
    }

    // Deals damage, attacking Pokemon receives a percentage of the damage dealt
    public static void dealDamageRecoil(EventManager eventManager, double percent) {
        EventData data  = eventManager.eventData;
        data.recoilPercent = percent;

        dealDamage(eventManager);
        recoilDamage(eventManager);
    }

    // Deals damage, attacking Pokemon heals from percentage of the damage dealt
    public static void dealDamageDrain(EventManager eventManager, double percent) {
        EventData data  = eventManager.eventData;
        data.drainPercent = percent;

        dealDamage(eventManager);
        drainHP(eventManager);
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
