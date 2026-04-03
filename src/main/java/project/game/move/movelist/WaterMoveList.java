package project.game.move.movelist;

import project.game.battle.Weather;
import project.game.builders.MoveBuilder;
import project.game.move.Move;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionChangeWeather;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.stats.StatusCondition;
import project.game.pokemon.stats.Type;

public class WaterMoveList {
     
    public static Move aquaTail() {
        return new MoveBuilder()
        .setId(401)
        .setName("Aqua Tail")
        .setType(Type.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(90)
        .setAccuracy(90)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move chillingWater() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(-1, 0, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(886)
        .setName("Chilling Water")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(50)
        .setAction(action)
        .build();
    }

    public static Move dive() {
        return new MoveBuilder()
        .setId(291)
        .setName("Dive")
        .setType(Type.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(80)
        .setAction(e -> MoveActionChangeCondition.enterImmuneState(e, StatusCondition.DIVE_ID))
        .build();
    }

    public static Move flipTurn() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            // TODO: Switch User out
        };

        return new MoveBuilder()
        .setId(812)
        .setName("Flip Turn")
        .setType(Type.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(60)
        .setAction(action)
        .build();
    }

    public static Move hydroCannon() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionCharge.rechargeMove(e);
        };

        return new MoveBuilder()
        .setId(308)
        .setName("Hydro Cannon")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(150)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move hydroPump() {
        return new MoveBuilder()
        .setId(56)
        .setName("Hydro Pump")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(110)
        .setAccuracy(80)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move liquidation() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, -1, 0, 0, 0, 0, 0), 20);
        };

        return new MoveBuilder()
        .setId(710)
        .setName("Liquidation")
        .setType(Type.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(85)
        .setAction(action)
        .build();
    }

    public static Move muddyWater() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, 0, -1, 0), 30);
        };

        return new MoveBuilder()
        .setId(330)
        .setName("Muddy Water")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(90)
        .setAccuracy(85)
        .setAction(action)
        .build();
    }

    public static Move rainDance() {
        return new MoveBuilder()
        .setId(240)
        .setName("Rain Dance")
        .setType(Type.WATER)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction(e -> MoveActionChangeWeather.changeWeather(e, Weather.RAIN))
        .build();
    }

    public static Move surf() {
        return new MoveBuilder()
        .setId(57)
        .setName("Surf")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(90)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move waterGun() {
        return new MoveBuilder()
        .setId(55)
        .setName("Water Gun")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPower(40)
        .setPP(25)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move waterPledge() {
        return new MoveBuilder()
        .setId(518)
        .setName("Water Pledge")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(80)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move waterPulse() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.CONFUSION_ID, 20);
        };

        return new MoveBuilder()
        .setId(352)
        .setName("Water Pulse")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAction(action)
        .build();
    }

    public static Move waveCrash() {
        return new MoveBuilder()
        .setId(834)
        .setName("Wave Crash")
        .setType(Type.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(120)
        .setAction(e -> MoveActionAttackDamage.dealDamageRecoil(e, 33))
        .build();
    }

    public static Move whirlpool() {
        MoveAction action = e -> {
            if (e.eventData.attackTarget.getConditions().hasKey(StatusCondition.DIVE_ID)) {
                e.eventData.moveUsed.doublePower();
            }

            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BOUND_ID);
        };

        return new MoveBuilder()
        .setId(250)
        .setName("Whirlpool")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(35)
        .setAccuracy(85)
        .setAction(action)
        .build();
    }

    public static Move withdraw() {
        MoveAction action = e -> {
            MoveListHelperFunctions.targetsUser(e.eventData);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 1, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(110)
        .setName("Withdraw")
        .setType(Type.WATER)
        .setCategory(Move.STATUS)
        .setPP(40)
        .setAction(action)
        .build();
    }

}
