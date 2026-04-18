package project.game.move.moveactions;

import java.util.Random;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.move.calculations.AttackMoveCalculations;
import project.game.move.calculations.MoveEffectiveCalculations;
import project.game.pokemon.Pokemon;

public interface MoveActionAttack extends MoveAction {
    
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

    private static void dealDamage(EventManager eventManager) {
        EventData data = eventManager.data;
        Pokemon attacker = data.user;
        Pokemon defender = data.attackTarget;
        boolean isMultiHit = data.moveUsed.isMultiHit();

        int damage = AttackMoveCalculations.calculateDamage(eventManager);
        data.damageDealt += damage;

        BattleLog.add("%s took %d damage!", defender, damage);
        BattleLog.add(!isMultiHit ? MoveEffectiveCalculations.isSuperEffective(data.moveEffectiveness) : "");
        BattleLog.add(data.criticalHit ? "A critical hit!" : "");

        attacker.addDealtDamage(damage);
        defender.takeDamage(damage);
        
        eventManager.notifyUserPokemon(EventID.ATK_MOVE_DEALS_DAMAGE);
        eventManager.notifyAttackTargetPokemon(EventID.DEF_MOVE_DEALS_DAMAGE);

        if (data.moveUsed.isContactMove()) {
            eventManager.notifyUserPokemon(EventID.ATK_MOVE_MAKES_CONTACT);
            eventManager.notifyAttackTargetPokemon(EventID.DEF_MOVE_MAKES_CONTACT);
        }
    }

    // Deals multiple hits of damage
    private static void dealMultiHitDamage(EventManager eventManager) {
        EventData data  = eventManager.data;
        data.hitCount = randomHits();

        for (int i = 0; i < data.hitCount; i++) {
            dealDamage(eventManager);
            if (data.attackTarget.getConditions().isFainted()) {
                data.hitCount = ++i;
                break;
            }
        }
        BattleLog.add("It hit %d times!", data.hitCount);
        BattleLog.add(MoveEffectiveCalculations.isSuperEffective(data.moveEffectiveness));
    }

    /**
     * Step 1: Calculates type effectivness of the move (also any immunites).<br>
     * Step 2: Rolls for accuracy (proceeds if the move hits the target).<br>
     * Step 3: Deals damage to the target Pokemon (slightly different logic for multi-hit moves).
     * 
     * @param eventManager
     */
    public static void attackTarget(EventManager eventManager) {   
        MoveEffectiveCalculations.calculateMoveEffectiveness(eventManager);
        MoveActionAccuracy.rollForAccuracy(eventManager);

        // Checks if the move is a multi-hit
        if (eventManager.data.moveUsed.isMultiHit()) {
            dealMultiHitDamage(eventManager);
        }   
        else {
            dealDamage(eventManager);
        }
        
    }

    /**
     * Confusion damage is calculated as if it were a typeless physical move with a power of 40.
     * It cannot score a critical hit, and does not receive STAB
     */
    public static void takeConfusionDamage(Pokemon p) {
        int damage = (int) (((((2 * p.getLevel()) / 5.0 + 2) * 40 * (p.getAttack().getPower() / (double) p.getDefense().getPower())) / 50.0 + 2)); 
        BattleLog.add("%s took %d damage from their own confusion!", p, damage);
        p.takeDamage(damage); 
    }

}
