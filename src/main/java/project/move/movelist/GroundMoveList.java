package project.move.movelist;

import project.move.Move;
import project.move.MoveBuilder;
import project.move.MoveListHelperFunctions;
import project.move.moveactions.MoveAction;
import project.move.moveactions.MoveActionAttackDamage;
import project.move.moveactions.MoveActionChangeCondition;
import project.move.moveactions.MoveActionChangeStat;
import project.stats.StatusCondition;
import project.stats.Type;

public class GroundMoveList {

    public static Move bulldoze() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, -1, 0, 0));
        };

        return new MoveBuilder()
        .setId(523)
        .setName("Bulldoze")
        .setType(Type.GROUND)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(60)
        .setContact(false)
        .setAction(action)
        .build();
    }

    public static Move dig() {
        return new MoveBuilder()
        .setId(91)
        .setName("Dig")
        .setType(Type.GROUND)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(80)
        .setAction(e -> MoveActionChangeCondition.enterImmuneState(e, StatusCondition.DIG_ID))
        .build();
    }

    public static Move earthquake() {
        MoveAction action = e -> {
            // Deals double power to opponents digging
            if (e.attackTarget.getConditions().hasKey(StatusCondition.DIG_ID)) e.moveUsed.doublePower(); 
            MoveActionAttackDamage.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(89)
        .setName("Earthquake")
        .setType(Type.GROUND)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(100)
        .setContact(false)
        .setAction(action)
        .build();
    }

    public static Move earthPower() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, -1, 0, 0, 0), 10);
        };

        return new MoveBuilder()
        .setId(414)
        .setName("Earth Power")
        .setType(Type.GROUND)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(90)
        .setAction(action)
        .build();
    }

    public static Move mudShot() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, -1, 0, 0));
        };

        return new MoveBuilder()
        .setId(341)
        .setName("Mud Shot")
        .setType(Type.GROUND)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(55)
        .setAccuracy(95)
        .setAction(action)
        .build();
    }

    public static Move scorchingSands() {
        MoveAction action = e -> {
            e.user.getConditions().removeCondition(StatusCondition.FREEZE_ID);
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BURN_ID, 30);
        };

        return new MoveBuilder()
        .setId(815)
        .setName("Scorching Sands")
        .setType(Type.GROUND)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(70)
        .setAction(action)
        .build();
    }

    public static Move stompingTantrum() {
        MoveAction action = e -> {
            if (e.user.getConditions().isInterrupted()) e.moveUsed.doublePower();
            MoveActionAttackDamage.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(707)
        .setName("Stomping Tantrum")
        .setType(Type.GROUND)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(75)
        .setAction(action)
        .build();
    }


}
