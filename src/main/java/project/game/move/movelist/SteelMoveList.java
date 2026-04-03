package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeStat;

public class SteelMoveList {

    public enum SteelMoveName {
        Flash_Cannon(SteelMoveList::flashCannon),
        Gyro_Ball(SteelMoveList::gyroBall),
        Iron_Defense(SteelMoveList::ironDefense);

        private final Function<EventManager, Integer> func;

        SteelMoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int flashCannon(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, -1, 0, 0, 0), 10);
        return 0;
    }

    public static int gyroBall(EventManager e) {
        int power = (int) (25.0 * e.eventData.attackTarget.getSpeed().getPower() 
                           / (double) e.eventData.user.getSpeed().getPower() + 1);
        e.eventData.moveUsed.setPower(power);
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int ironDefense(EventManager e) {
        MoveListHelperFunctions.targetsUser(e.eventData);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 2, 0, 0, 0, 0, 0));
        return 0;
    }
}
