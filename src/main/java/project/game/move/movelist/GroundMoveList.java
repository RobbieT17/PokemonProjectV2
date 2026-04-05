package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class GroundMoveList {

    public static int bulldoze(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, -1, 0, 0));
        return 0;
    }

    public static int dig(EventManager e) {
        MoveActionChangeCondition.enterImmuneState(e, StatusConditionID.DIG);
        return 0;
    }

    public static int earthquake(EventManager e) {
        if (e.eventData.attackTarget.getConditions().hasKey(StatusConditionID.DIG)) {
            e.eventData.moveUsed.doublePower();
        }
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int earthPower(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, -1, 0, 0, 0), 10);
        return 0;
    }

    public static int mudShot(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, -1, 0, 0));
        return 0;
    }

    public static int scorchingSands(EventManager e) {
        e.eventData.user.getConditions().removeCondition(StatusConditionID.FREEZE);
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.BURN, 30);
        return 0;
    }

    public static int stompingTantrum(EventManager e) {
        if (e.eventData.user.getConditions().isInterrupted()) {
            e.eventData.moveUsed.doublePower();
        }
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }
}
