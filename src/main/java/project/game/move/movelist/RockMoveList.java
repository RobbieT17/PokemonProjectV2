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
import project.game.pokemon.effects.StatusConditionManager.StatusConditionIDs;
import project.game.pokemon.stats.Type;

public class RockMoveList {

    public static Move rockSlide() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusConditionIDs.FLINCH_ID, 30);
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
            MoveActionChangeCondition.leaveImmuneState(e, StatusConditionIDs.FLY_ID, "Fell from the sky!");
            MoveActionChangeCondition.applyCondition(e, StatusConditionIDs.GROUNDED_ID);
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
