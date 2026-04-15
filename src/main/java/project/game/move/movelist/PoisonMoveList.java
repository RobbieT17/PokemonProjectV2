package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class PoisonMoveList {

    public static int acidSpray(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, -2, 0, 0, 0));
        return 0;
    }

    public static int poisonJab(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Poison, 30);
        return 0;
    }

    public static int poisonPowder(EventManager e) {
        MoveActionAccuracy.rollForAccuracy(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Poison);
        return 0;
    }

    public static int toxic(EventManager e) {
        MoveActionAccuracy.rollForAccuracy(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Bad_Poison);
        return 0;
    }

    public static int venoshock(EventManager e) {
        if (e.data.attackTarget.getConditions().hasKey(StatusConditionID.Poison)) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }
}
