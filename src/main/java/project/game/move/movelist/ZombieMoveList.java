package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class ZombieMoveList {

    // Infect: deals minor damage and applies "Infect" status to the target
    // Type: Zombie, Category: Physical, Power: 30, PP: 10, Accuracy: 100
    public static int infect(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Infect);
        return 0;
    }

    // Rotten Bite: strong bite that may lower the target's Attack
    // Type: Zombie, Category: Physical, Power: 70, PP: 15, Accuracy: 95
    public static int rottenBite(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(-1, 0, 0, 0, 0, 0, 0), 30);
        return 0;
    }

    // Necrotic Claw: high damage move with a chance to confuse the target
    // Type: Zombie, Category: Physical, Power: 90, PP: 10, Accuracy: 90
    public static int necroticClaw(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Confusion, 20);
        return 0;
    }

    // Viral Spread: lowers target's defenses while spreading infection to all opponents
    // Type: Zombie, Category: Special, Power: 50, PP: 5, Accuracy: 100
    public static int viralSpread(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(-1, 0, -1, 0, 0, 0, 0), 100);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Infect, 10); // 10% chance to infect others
        return 0;
    }

    // Grave Shock: very powerful move, requires a turn to charge
    // Type: Zombie, Category: Special, Power: 120, PP: 5, Accuracy: 85
    public static int graveShock(EventManager e) {
        MoveActionCharge.rampageMove(e);
        MoveActionAttack.attackTarget(e);
        return 0;
    }
}

