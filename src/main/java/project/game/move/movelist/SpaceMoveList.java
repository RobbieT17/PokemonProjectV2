package project.game.move.movelist;

import project.game.builders.MoveBuilder;
import project.game.move.Move;
import project.game.move.moveactions.MoveAction;
import project.game.pokemon.stats.Type;

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
