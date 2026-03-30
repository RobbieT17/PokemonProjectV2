package project.move.movelist;

import project.move.Move;
import project.move.MoveBuilder;
import project.move.MoveListHelperFunctions;
import project.move.moveactions.MoveAction;
import project.move.moveactions.MoveActionAttackDamage;
import project.move.moveactions.MoveActionChangeStat;
import project.stats.Type;

public class SteelMoveList {

    public static Move flashCannon() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, -1, 0, 0, 0), 10);
        };

        return new MoveBuilder()
        .setId(430)
        .setName("Flash Cannon")
        .setType(Type.STEEL)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(80)
        .setAction(action)
        .build();
    }

    public static Move gyroBall() {
        MoveAction action = e -> {
            e.moveUsed.setPower((int) (25.0 * e.attackTarget.getSpeed().getPower() / (double) e.user.getSpeed().getPower() + 1));
            MoveActionAttackDamage.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(360)
        .setName("Gyro Ball")
        .setType(Type.STEEL)
        .setCategory(Move.PHYSICAL)
        .setPP(5)
        .setPower(-1) // Varies
        .setAction(action)
        .build();
    }

    public static Move ironDefense() {
        MoveAction action = e -> {
            MoveListHelperFunctions.targetsUser(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 2, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(334)
        .setName("Iron Defense")
        .setType(Type.STEEL)
        .setCategory(Move.STATUS)
        .setPP(15)
        .setAction(action)
        .build();
    }

}
