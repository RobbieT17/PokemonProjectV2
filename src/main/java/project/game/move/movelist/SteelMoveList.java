package project.game.move.movelist;

import project.game.builders.MoveBuilder;
import project.game.move.Move;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.pokemon.stats.Type;

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
            int power = (int) (25.0 * e.eventData.attackTarget.getSpeed().getPower() / (double) e.eventData.user.getSpeed().getPower() + 1);
            e.eventData.moveUsed.setPower(power);
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
            MoveListHelperFunctions.targetsUser(e.eventData);
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
