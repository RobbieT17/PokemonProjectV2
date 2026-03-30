package project.move.movelist;

import project.move.Move;
import project.move.MoveBuilder;
import project.move.MoveListHelperFunctions;
import project.move.moveactions.MoveAction;
import project.move.moveactions.MoveActionAttackDamage;
import project.move.moveactions.MoveActionChangeCondition;
import project.move.moveactions.MoveActionChangeStat;
import project.stats.StatusCondition;
import project.stats.Type;

public class PsychicMoveList {

    public static Move amnesia() {
        MoveAction action = e -> {
            MoveListHelperFunctions.targetsUser(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 1, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(133)
        .setName("Amnesia")
        .setType(Type.PSYCHIC)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

    public static Move zenHeadbutt() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.FLINCH_ID, 20);
        };

        return new MoveBuilder()
        .setId(428)
        .setName("Zen Headbutt")
        .setType(Type.PSYCHIC)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(80)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

}
