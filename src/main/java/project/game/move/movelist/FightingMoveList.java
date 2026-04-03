package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;

public class FightingMoveList {

    public enum FightingMoveName {

        Aura_Sphere(FightingMoveList::auraSphere),
        Brick_Break(FightingMoveList::brickBreak),
        Focus_Blast(FightingMoveList::focusBlast),
        Focus_Punch(FightingMoveList::focusPunch);

        private final Function<EventManager, Integer> func;

        FightingMoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int auraSphere(EventManager e) {
        MoveActionAttackDamage.dealDamage(e); // was DEFAULT_ACTION
        return 0;
    }

    public static int brickBreak(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        // TODO: Disables Light Screen / Reflect
        return 0;
    }

    public static int focusBlast(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, -1, 0, 0, 0), 10);
        return 0;
    }

    public static int focusPunch(EventManager e) {
        MoveActionCharge.focusMove(e);
        return 0;
    }
}
