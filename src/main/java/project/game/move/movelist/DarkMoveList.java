package project.game.move.movelist;

import project.game.builders.MoveBuilder;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.stats.Type;

public class DarkMoveList {

    public static Move bite() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e ,StatusConditionID.FLINCH_ID,30);
        };

        return new MoveBuilder()
        .setId(44)
        .setName("Bite")
        .setType(Type.DARK)
        .setCategory(Move.PHYSICAL)
        .setPP(25)
        .setPower(60)
        .setAction(action)
        .build();
    }

    public static Move crunch() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, -1, 0, 0, 0, 0, 0), 20);
        };

        return new MoveBuilder()
        .setId(242)
        .setName("Crunch")
        .setType(Type.DARK)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(80)
        .setAction(action)
        .build();
    }

    public static Move darkPulse() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH_ID, 20);
        };

        return new MoveBuilder()
        .setId(399)
        .setName("Dark Pulse")
        .setType(Type.DARK)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(80)
        .setAction(action)
        .build();
    }

    public static Move fakeOut() {
        MoveAction action = e -> {
            if (!e.eventData.user.firstRound()) {
                throw new MoveInterruptedException(Move.FAILED);
            }

            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH_ID);
        };

        return new MoveBuilder()
        .setId(252)
        .setName("Fake Out")
        .setType(Type.DARK)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(40)
        .setAction(action)
        .build();
    }
}
