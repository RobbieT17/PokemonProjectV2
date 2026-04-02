package project.game.move.movelist;

import project.game.move.Move;
import project.game.move.MoveBuilder;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.stats.Type;

public class FightingMoveList {

    public static Move auraSphere() {
        return new MoveBuilder()
        .setId(396)
        .setName("Aura Sphere")
        .setType(Type.FIGHTING)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(80)
        .setAccuracy(Move.ALWAYS_HITS)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move brickBreak() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            // TODO: Disables Light Screen / Reflect
        };

        return new MoveBuilder()
        .setId(280)
        .setName("Brick Break")
        .setType(Type.FIGHTING)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(75)
        .setAction(action)
        .build();
    }

    public static Move focusBlast() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, -1, 0, 0, 0), 10);
        };

        return new MoveBuilder()
        .setId(411)
        .setName("Focus Blast")
        .setType(Type.FIGHTING)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(120)
        .setAccuracy(70)
        .setAction(action)
        .build();
    }

    public static Move focusPunch() {
        return new MoveBuilder()
        .setId(264)
        .setName("Focus Punch")
        .setType(Type.FIGHTING)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(150)
        .setPriority(-3)
        .setAction(e -> MoveActionCharge.focusMove(e))
        .build();
    }
}
