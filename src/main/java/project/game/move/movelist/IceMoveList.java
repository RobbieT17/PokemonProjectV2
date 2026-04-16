package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeStat;

public class IceMoveList {

    public static int avalanche(EventManager e) {
        if (e.data.user.getConditions().tookDamage()) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int haze(EventManager e) {
        MoveActionChangeStat.resetStats(e, e.data.user);
        MoveActionChangeStat.resetStats(e, e.data.attackTarget);
        return 0;
    }
    
}
