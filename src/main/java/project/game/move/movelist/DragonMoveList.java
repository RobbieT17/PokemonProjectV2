package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class DragonMoveList {

    public static int breakingSwipe(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(-1, 0, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int dragonBreath(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Paralysis, 30);
        return 0;
    }

    public static int dragonClaw(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int dragonDance(EventManager e) {
        MoveAction.targetsUser(e.data);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(1, 0, 0, 0, 1, 0, 0));
        return 0;
    }

    public static int dragonPulse(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int dragonTail(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int outrage(EventManager e) {
        MoveActionCharge.rampageMove(e);
        return 0;
    }

}
