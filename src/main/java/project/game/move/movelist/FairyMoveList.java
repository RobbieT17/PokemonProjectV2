package project.game.move.movelist;

import project.game.move.Move;
import project.game.move.MoveBuilder;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.stats.Type;

public class FairyMoveList {

    public static Move charm() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(-2, 0, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(204)
        .setName("Charm")
        .setType(Type.FAIRY)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

}
