package project.game.move.movelist;

import project.game.move.Move;
import project.game.move.MoveBuilder;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.stats.StatusCondition;
import project.game.stats.Type;

public class GhostMoveList {

    public static Move confuseRay() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.CONFUSION_ID);
            MoveAction.displayFailMessage(e);
        };

        return new MoveBuilder()
        .setId(109)
        .setName("Confuse Ray")
        .setType(Type.GHOST)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

    public static Move curse() {
        // TODO: Two Verison of move, Ghost-Type, and Non-Ghost-Type
        MoveAction action = e -> {
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(1, 1, 0, 0, -1, 0, 0));
        };

        return new MoveBuilder()
        .setId(174)
        .setName("Curse")
        .setType(Type.GHOST)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAction(action)
        .build();
    }

    public static Move shadowClaw() {
        return new MoveBuilder()
        .setId(421)
        .setName("Shadow Claw")
        .setType(Type.GHOST)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(70)
        .setCritRatio(Move.HIGH_CRIT)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }
    
}
