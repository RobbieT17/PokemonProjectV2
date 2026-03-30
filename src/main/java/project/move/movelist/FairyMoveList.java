package project.move.movelist;

import project.move.Move;
import project.move.MoveBuilder;
import project.move.MoveListHelperFunctions;
import project.move.moveactions.MoveAction;
import project.move.moveactions.MoveActionAccuracy;
import project.move.moveactions.MoveActionChangeStat;
import project.stats.Type;

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
