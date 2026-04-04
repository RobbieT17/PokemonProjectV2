package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;
import project.game.move.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class DarkMoveList {

    public static int bite(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e ,StatusConditionID.FLINCH_ID,30);
        return 0;
    }

    public static int crunch(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, -1, 0, 0, 0, 0, 0), 20);
        return 0;
    }

    public static int darkPulse(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH_ID, 20);
        return 0;
    }

    public static int fakeOut(EventManager e) {
        if (!e.eventData.user.firstRound()) {
            throw new MoveInterruptedException(Move.FAILED);
        }

        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH_ID);

        return 0;
    }
}
