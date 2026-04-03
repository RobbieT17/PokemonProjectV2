package project.game.move.movelist;

import project.game.battle.BattleField;
import project.game.battle.Weather;
import project.game.builders.MoveBuilder;
import project.game.move.Move;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.stats.Type;

public class FlyingMoveList {

    public static Move acrobatics() {
        return new MoveBuilder()
        .setId(512)
        .setName("Acrobatics")
        .setType(Type.FLYING)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(55)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move aerialAce() {
        return new MoveBuilder()
        .setId(332)
        .setName("Aerial Ace")
        .setType(Type.FLYING)
        .setCategory(Move.PHYSICAL)
        .setPP(60)
        .setAccuracy(Move.ALWAYS_HITS)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move airCutter() {
        return new MoveBuilder()
        .setId(314)
        .setName("Air Cutter")
        .setType(Type.FLYING)
        .setCategory(Move.SPECIAL)
        .setPP(25)
        .setPower(60)
        .setAccuracy(95)
        .setCritRatio(Move.HIGH_CRIT)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move airSlash() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH_ID, 30);
        };

        return new MoveBuilder()
        .setId(403)
        .setName("Air Slash")
        .setType(Type.FLYING)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(75)
        .setAccuracy(95)
        .setAction(action)
        .build();
    }

    public static Move fly() {
        return new MoveBuilder()
        .setId(19)
        .setName("Fly")
        .setType(Type.FLYING)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(90)
        .setAccuracy(95)
        .setAction(e -> MoveActionChangeCondition.enterImmuneState(e, StatusConditionID.FLY_ID))
        .build();
    }

    public static Move hurricane() {
        MoveAction action = e -> {
            Move m = e.eventData.moveUsed;
            // Perfect accuracy in the rain, 50% accuracy in harsh sunlight
            if (BattleField.currentWeather == Weather.RAIN) {
                m.perfectAccuracy();
            }
            else if (BattleField.currentWeather == Weather.SUNNY) {
                m.setAccuracy(50);
            }

           MoveActionAttackDamage.dealDamage(e);
           MoveActionChangeCondition.applyCondition(e, StatusConditionID.CONFUSION_ID, 10);
        };

        return new MoveBuilder()
        .setId(542)
        .setName("Hurricane")
        .setType(Type.FLYING)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(110)
        .setAccuracy(70)
        .setAction(action)
        .build();
    }
}
