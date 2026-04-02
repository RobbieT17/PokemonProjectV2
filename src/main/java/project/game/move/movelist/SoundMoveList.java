package project.game.move.movelist;

import project.game.move.Move;
import project.game.move.MoveBuilder;
import project.game.move.moveactions.MoveAction;
import project.game.stats.Type;

public class SoundMoveList {

    public static Move roar() {
        MoveAction action = e -> {
            // TODO: Implement force switch (need to track Pokemon's owner in Pokemon class)
        };

        return new MoveBuilder()
        .setId(46)
        .setName("Roar")
        .setType(Type.SOUND)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setPriority(-6)
        .setAction(action)
        .build();
    }
    
}
