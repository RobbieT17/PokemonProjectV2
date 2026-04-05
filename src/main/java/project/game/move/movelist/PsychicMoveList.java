package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionHealthRestore;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class PsychicMoveList {

    public static int amnesia(EventManager e) {
        MoveAction.targetsUser(e.eventData);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 1, 0, 0, 0));
        return 0;
    }

    public static int rest(EventManager e) {
        MoveAction.targetsUser(e.eventData);
        MoveActionHealthRestore.restoreHp(e, 100);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.SLEEP);
        return 0;
    }

    public static int zenHeadbutt(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH, 20);
        return 0;
    }
}
