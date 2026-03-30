package project.move.movelist;

import project.move.Move;
import project.move.MoveBuilder;
import project.move.moveactions.MoveAction;
import project.move.moveactions.MoveActionAttackDamage;
import project.move.moveactions.MoveActionChangeCondition;
import project.stats.StatusCondition;
import project.stats.Type;

public class ElectricMoveList {

    public static Move thunderPunch() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.PARALYSIS_ID, 10);
        };

        return new MoveBuilder()
        .setId(9)
        .setName("Thunder Punch")
        .setType(Type.ELECTRIC)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(75)
        .setAction(action)
        .build();
    }

}
