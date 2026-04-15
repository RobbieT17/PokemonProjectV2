package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class GroundMoveList {

    public static int bulldoze(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, -1, 0, 0));
        return 0;
    }

    public static int dig(EventManager e) {
        MoveActionChangeCondition.enterImmuneState(e, StatusConditionID.Dig_State);
        return 0;
    }

    public static int earthquake(EventManager e) {
        if (e.data.attackTarget.getConditions().hasKey(StatusConditionID.Dig_State)) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int earthPower(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, -1, 0, 0, 0), 10);
        return 0;
    }

    public static int mudShot(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, -1, 0, 0));
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
