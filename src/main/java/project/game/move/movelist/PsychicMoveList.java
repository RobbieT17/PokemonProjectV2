package project.game.move.movelist;

import project.game.builders.MoveBuilder;
import project.game.move.Move;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.stats.Type;

public class PsychicMoveList {

    public static Move amnesia() {
        MoveAction action = e -> {
            MoveListHelperFunctions.targetsUser(e.eventData);
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
            MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH_ID, 20);
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
