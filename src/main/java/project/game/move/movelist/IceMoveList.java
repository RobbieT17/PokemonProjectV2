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
import project.game.pokemon.effects.StatusCondition;
import project.game.pokemon.stats.Type;

public class IceMoveList {

    public static Move avalanche() {
        MoveAction action =  e -> {
            if (e.eventData.user.getConditions().tookDamage()) {
                e.eventData.moveUsed.doublePower();
            }
            
            MoveActionAttackDamage.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(419)
        .setName("Avalanche")
        .setType(Type.ICE)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(60)
        .setPriority(-4)
        .setAction(action)
        .build();
    }

    public static Move blizzard() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.FREEZE_ID, 10);
        };

        return new MoveBuilder()
        .setId(59)
        .setName("Blizzard")
        .setType(Type.ICE)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(110)
        .setAccuracy(70)
        .setAction(action)
        .build();
    }

    public static Move hail() {
        return new MoveBuilder()
        .setId(258)
        .setName("Hail")
        .setType(Type.ICE)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAction(e -> MoveActionChangeWeather.changeWeather(e, Weather.HAIL))
        .build();
    }

    public static Move haze() {
        MoveAction action = e -> {
            MoveActionChangeStat.resetStats(e, e.eventData.user);
            MoveActionChangeStat.resetStats(e, e.eventData.attackTarget);
        };

        return new MoveBuilder()
        .setId(114)
        .setName("Haze")
        .setType(Type.ICE)
        .setCategory(Move.STATUS)
        .setPP(30)
        .setAction(action)
        .build();
    }

    public static Move iceBeam() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.FREEZE_ID, 10);
        };

        return new MoveBuilder()
        .setId(58)
        .setName("Ice Beam")
        .setType(Type.ICE)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(90)
        .setAction(action)
        .build();
    }

    public static Move icePunch() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.FREEZE_ID, 10);
        };

        return new MoveBuilder()
        .setId(8)
        .setName("Ice Punch")
        .setType(Type.ICE)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(75)
        .setAction(action)
        .build();
    }

    public static Move iceSpinner() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            // TODO: Clears Terrain effect
        };

        return new MoveBuilder()
        .setId(861)
        .setName("Ice Spinner")
        .setType(Type.ICE)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setAction(action)
        .build();
    }


    public static Move icyWind() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, -1, 0, 0));
        };

        return new MoveBuilder()
        .setId(196)
        .setName("Icy Wind")
        .setType(Type.ICE)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(55)
        .setAccuracy(95)
        .setAction(action)
        .build();
    }
}
