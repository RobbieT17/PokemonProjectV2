package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;

public class SoundMoveList {

    public enum MoveName {
        Roar(SoundMoveList::roar);

        private final Function<EventManager, Integer> func;

        MoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int roar(EventManager e) {
        // TODO: Implement force switch logic
        // e.eventData.attackTarget.getOwner().forceSwitch(e.eventData.attackTarget);
        return 0;
    }
}

