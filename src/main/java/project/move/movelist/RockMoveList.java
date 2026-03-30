package project.move.movelist;

import project.battle.Weather;
import project.move.Move;
import project.move.MoveBuilder;
import project.move.MoveListHelperFunctions;
import project.move.moveactions.MoveAction;
import project.move.moveactions.MoveActionAttackDamage;
import project.move.moveactions.MoveActionChangeCondition;
import project.move.moveactions.MoveActionChangeStat;
import project.move.moveactions.MoveActionChangeWeather;
import project.stats.StatusCondition;
import project.stats.Type;

public class RockMoveList {

    public static Move rockSlide() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.FLINCH_ID, 30);
        };

        return new MoveBuilder()
        .setId(157)
        .setName("Rock Slide")
        .setType(Type.ROCK)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(75)
        .setAccuracy(90)
        .setContact(false)
        .setAction(action)
        .build();
    }

    public static Move rockTomb() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, -1, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(317)
        .setName("Rock Tomb")
        .setType(Type.ROCK)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(60)
        .setAccuracy(95)
        .setContact(false)
        .setAction(action)
        .build();
    }
    
    public static Move sandstorm() {
        return new MoveBuilder()
        .setId(201)
        .setName("Sandstorm")
        .setType(Type.ROCK)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAction(e -> MoveActionChangeWeather.changeWeather(e, Weather.SANDSTORM))
        .build();
    }

    public static Move smackDown() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.leaveImmuneState(e, StatusCondition.FLY_ID, "Fell from the sky!");
            MoveActionChangeCondition.applyCondition(e, StatusCondition.GROUNDED_ID);
        };

        return new MoveBuilder()
        .setId(479)
        .setName("Smack Down")
        .setType(Type.ROCK)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(50)
        .setContact(false)
        .setAction(action)
        .build();
    }
}
