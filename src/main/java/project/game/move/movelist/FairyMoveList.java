package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionChangeStat;

public class FairyMoveList {

    public enum FairyMoveName {

        Charm(FairyMoveList::charm);

        private final Function<EventManager, Integer> func;

        FairyMoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int charm(EventManager e) {
        MoveActionAccuracy.moveHits(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(-2, 0, 0, 0, 0, 0, 0));
        return 0;
    }

}
