package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionChangeStat;

public class FairyMoveList {

    public static int charm(EventManager e) {
        MoveActionAccuracy.moveHits(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(-2, 0, 0, 0, 0, 0, 0));
        return 0;
    }

}
