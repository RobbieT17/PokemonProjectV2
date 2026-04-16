package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class WaterMoveList {

    public static int whirlpool(EventManager e) {
        if (e.data.attackTarget.getConditions().hasKey(StatusConditionID.Dive_State)) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Bound);
        return 0;
    }

}
