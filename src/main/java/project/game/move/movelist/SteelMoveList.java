package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttack;

public class SteelMoveList {

    public static int gyroBall(EventManager e) {
        int power = (int) (25.0 * e.data.attackTarget.getSpeed().getPower() 
                           / (double) e.data.user.getSpeed().getPower() + 1);
        e.data.moveUsed.setPower(power);
        MoveActionAttack.attackTarget(e);
        return 0;
    }


}
