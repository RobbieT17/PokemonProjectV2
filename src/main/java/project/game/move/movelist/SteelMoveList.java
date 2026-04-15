package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeStat;

public class SteelMoveList {

    public static int flashCannon(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, -1, 0, 0, 0), 10);
        return 0;
    }

    public static int gyroBall(EventManager e) {
        int power = (int) (25.0 * e.data.attackTarget.getSpeed().getPower() 
                           / (double) e.data.user.getSpeed().getPower() + 1);
        e.data.moveUsed.setPower(power);
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int ironDefense(EventManager e) {
        MoveAction.targetsUser(e.data);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 2, 0, 0, 0, 0, 0));
        return 0;
    }
}
