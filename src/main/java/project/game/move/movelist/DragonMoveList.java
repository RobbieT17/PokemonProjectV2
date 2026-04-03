package project.game.move.movelist;

import project.game.builders.MoveBuilder;
import project.game.move.Move;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.stats.StatusCondition;
import project.game.pokemon.stats.Type;

public class DragonMoveList {

    public static Move breakingSwipe() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(-1, 0, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(784)
        .setName("Breaking Swipe")
        .setType(Type.DRAGON)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(60)
        .setAction(action)
        .build();
    }

    public static Move dragonBreath() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.PARALYSIS_ID, 30);
        };

        return new MoveBuilder()
        .setId(225)
        .setName("Dragon Breath")
        .setType(Type.DRAGON)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAction(action)
        .build();
    }

    public static Move dragonClaw() {
        return new MoveBuilder()
        .setId(337)
        .setName("Dragon Claw")
        .setType(Type.DRAGON)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(80)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move dragonDance() {
        MoveAction action = e -> {
            MoveListHelperFunctions.targetsUser(e.eventData);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(1, 0, 0, 0, 1, 0, 0));
        };

        return new MoveBuilder()
        .setId(349)
        .setName("Dragon Dance")
        .setType(Type.DRAGON)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

    public static Move dragonPulse() {
        return new MoveBuilder()
        .setId(406)
        .setName("Dragon Pulse")
        .setType(Type.DRAGON)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(85)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move dragonTail() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            // TODO: Force Switch
        };

        return new MoveBuilder()
        .setId(525)
        .setName("Dragon Tail")
        .setType(Type.DRAGON)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(60)
        .setAccuracy(90)
        .setPriority(-6)
        .setAction(action)
        .build();
    }

    public static Move outrage() {
        return new MoveBuilder()
        .setId(200)
        .setName("Outrage")
        .setType(Type.DRAGON)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(120)
        .setAction(e -> MoveActionCharge.rampageMove(e))
        .build();
    }

}
