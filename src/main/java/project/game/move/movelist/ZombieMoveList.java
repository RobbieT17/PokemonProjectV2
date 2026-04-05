package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class ZombieMoveList {

    // Infect: deals minor damage and applies "Infect" status to the target
    // Type: Zombie, Category: Physical, Power: 30, PP: 10, Accuracy: 100
    public static int infect(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.INFECT_ID);
        return 0;
    }

    // Rotten Bite: strong bite that may lower the target's Attack
    // Type: Zombie, Category: Physical, Power: 70, PP: 15, Accuracy: 95
    public static int rottenBite(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(-1, 0, 0, 0, 0, 0, 0), 30);
        return 0;
    }

    // Necrotic Claw: high damage move with a chance to confuse the target
    // Type: Zombie, Category: Physical, Power: 90, PP: 10, Accuracy: 90
    public static int necroticClaw(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.CONFUSION_ID, 20);
        return 0;
    }

    // Viral Spread: lowers target's defenses while spreading infection to all opponents
    // Type: Zombie, Category: Special, Power: 50, PP: 5, Accuracy: 100
    public static int viralSpread(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(-1, 0, -1, 0, 0, 0, 0), 100);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.INFECT_ID, 10); // 10% chance to infect others
        return 0;
    }

    // Grave Shock: very powerful move, requires a turn to charge
    // Type: Zombie, Category: Special, Power: 120, PP: 5, Accuracy: 85
    public static int graveShock(EventManager e) {
        MoveActionCharge.rampageMove(e);
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }
}

