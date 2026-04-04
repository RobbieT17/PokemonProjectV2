package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionHealthRestore;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class PsychicMoveList {

    public static int amnesia(EventManager e) {
        MoveListHelperFunctions.targetsUser(e.eventData);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 1, 0, 0, 0));
        return 0;
    }

    public static int rest(EventManager e) {
        MoveListHelperFunctions.targetsUser(e.eventData);
        MoveActionHealthRestore.restoreHp(e, 100);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.SLEEP_ID);
        return 0;
    }

    public static int zenHeadbutt(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH_ID, 20);
        return 0;
    }
}
