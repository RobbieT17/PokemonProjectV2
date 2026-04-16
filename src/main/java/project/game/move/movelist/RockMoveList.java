package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class RockMoveList {

    public static int smackDown(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.leaveImmuneState(e, StatusConditionID.Fly_State, "Fell from the sky!");
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Grounded);
        return 0;
    }

}
