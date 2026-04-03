package project.game.move.movelist;

import project.game.builders.MoveBuilder;
import project.game.move.Move;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.stats.Type;

public class ElectricMoveList {

    public static Move thunderPunch() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusConditionID.PARALYSIS_ID, 10);
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
