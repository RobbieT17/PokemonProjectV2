package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class ElectricMoveList {

    public static int thunderPunch(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Paralysis, 10);
        return 0;
    }

}
