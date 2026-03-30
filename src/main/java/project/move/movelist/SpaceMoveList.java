package project.move.movelist;

import project.move.Move;
import project.move.MoveBuilder;
import project.move.moveactions.MoveAction;
import project.stats.Type;

public class SpaceMoveList {

    public static Move swift() {
        return new MoveBuilder()
        .setId(129)
        .setName("Swift")
        .setType(Type.SPACE)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAccuracy(Move.ALWAYS_HITS)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }
    
}
