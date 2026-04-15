package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class GhostMoveList {

    public static int confuseRay(EventManager e) {
        MoveActionAccuracy.rollForAccuracy(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Confusion);
        MoveAction.displayFailMessage(e);
        return 0;
    }

    public static int curse(EventManager e) {
        MoveActionChangeStat.changeStats(e, MoveAction.stats(1, 1, 0, 0, -1, 0, 0));
        return 0;
    }

    public static int shadowClaw(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }
}
