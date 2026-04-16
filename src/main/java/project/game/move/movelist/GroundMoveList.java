package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class GroundMoveList {

    public static int earthquake(EventManager e) {
        if (e.data.attackTarget.getConditions().hasKey(StatusConditionID.Dig_State)) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int scorchingSands(EventManager e) {
        e.data.user.getConditions().removeCondition(StatusConditionID.Freeze);
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 30);
        return 0;
    }

    public static int stompingTantrum(EventManager e) {
        if (e.data.user.getConditions().isInterrupted()) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }
}
