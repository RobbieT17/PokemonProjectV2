package project.game.move.movelist;

import project.game.builders.MoveBuilder;
import project.game.move.Move;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.stats.Type;

public class ZombieMoveList {

    public static Move infect() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusConditionID.INFECT_ID);
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
