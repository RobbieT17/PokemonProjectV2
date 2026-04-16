package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttack;

public class FireMoveList {

    public static int heatCrash(EventManager e) {
        double ratio = e.data.user.getWeight() / e.data.attackTarget.getWeight();

        e.data.moveUsed.setPower(
            ratio < 2 ? 40
            : ratio < 3 ? 60
            : ratio < 4 ? 80
            : ratio < 5 ? 100
            : 120
        );

        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int temperFlare(EventManager e) {
        if (e.data.user.getConditions().isInterrupted()) {
            e.data.moveUsed.doublePower();
        }

        MoveActionAttack.attackTarget(e);
        return 0;
    }

}
