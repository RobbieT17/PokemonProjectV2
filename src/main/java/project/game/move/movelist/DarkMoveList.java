package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class DarkMoveList {

    public static int fakeOut(EventManager e) {
        if (!e.data.user.firstRound()) {
            throw new MoveInterruptedException(Move.FAILED);
        }

        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Flinch);

        return 0;
    }
}
