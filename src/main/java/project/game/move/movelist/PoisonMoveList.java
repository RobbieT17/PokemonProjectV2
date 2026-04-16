package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttack;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class PoisonMoveList {

   
    public static int venoshock(EventManager e) {
        if (e.data.attackTarget.getConditions().hasKey(StatusConditionID.Poison)) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }
}
