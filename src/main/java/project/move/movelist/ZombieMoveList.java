package project.move.movelist;

import project.move.Move;
import project.move.MoveBuilder;
import project.move.moveactions.MoveAction;
import project.move.moveactions.MoveActionAttackDamage;
import project.move.moveactions.MoveActionChangeCondition;
import project.stats.StatusCondition;
import project.stats.Type;

public class ZombieMoveList {

    public static Move infect() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.INFECT_ID);
        };

        return new MoveBuilder()
        .setId(964)
        .setName("Infect")
        .setType(Type.ZOMBIE)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(30)
        .setAction(action)
        .build();
    }
    
}
