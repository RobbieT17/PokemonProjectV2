package project.move.movelist;

import project.move.Move;
import project.move.MoveBuilder;
import project.move.MoveListHelperFunctions;
import project.move.moveactions.MoveAction;
import project.move.moveactions.MoveActionAccuracy;
import project.move.moveactions.MoveActionAttackDamage;
import project.move.moveactions.MoveActionChangeCondition;
import project.move.moveactions.MoveActionChangeStat;
import project.stats.StatusCondition;
import project.stats.Type;

public class PoisonMoveList {

    public static Move acidSpray() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, -2, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(491)
        .setName("Acid Spray")
        .setType(Type.POISON)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(40)
        .setAction(action)
        .build();
    }

    public static Move poisonJab() {
        MoveAction action = (e) -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.POISON_ID, 30);
        };

        return new MoveBuilder()
        .setId(398)
        .setName("Poison Jab")
        .setType(Type.POISON)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(80)
        .setAction(action)
        .build();
    }

    public static Move poisonPowder() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.POISON_ID);
            MoveAction.displayFailMessage(e);
        };

        return new MoveBuilder()
        .setId(77)
        .setName("Poison Powder")
        .setType(Type.POISON)
        .setCategory(Move.STATUS)
        .setPP(35)
        .setAccuracy(100)
        .setAction(action)
        .build();
    }

    public static Move toxic() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BAD_POISON_ID);
        };

        return new MoveBuilder()
        .setId(92)
        .setName("Toxic")
        .setType(Type.POISON)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move venoshock() {
        MoveAction action = e -> {
            if (e.eventData.attackTarget.getConditions().hasKey(StatusCondition.POISON_ID)) {
                e.eventData.moveUsed.doublePower();
            }
            
            MoveActionAttackDamage.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(474)
        .setName("Venoshock")
        .setType(Type.POISON)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(65)
        .setAction(action)
        .build();
    }

}
