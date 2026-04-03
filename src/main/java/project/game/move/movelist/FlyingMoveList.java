package project.game.move.movelist;

import java.util.function.Function;

import project.game.battle.BattleField;
import project.game.battle.Weather;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class FlyingMoveList {

    public enum FlyingMoveName {

        Acrobatics(FlyingMoveList::acrobatics),
        Aerial_Ace(FlyingMoveList::aerialAce),
        Air_Cutter(FlyingMoveList::airCutter),
        Air_Slash(FlyingMoveList::airSlash),
        Fly(FlyingMoveList::fly),
        Hurricane(FlyingMoveList::hurricane);

        private final Function<EventManager, Integer> func;

        FlyingMoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int acrobatics(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int aerialAce(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int airCutter(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int airSlash(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH_ID, 30);
        return 0;
    }

    public static int fly(EventManager e) {
        MoveActionChangeCondition.enterImmuneState(e, StatusConditionID.FLY_ID);
        return 0;
    }

    public static int hurricane(EventManager e) {
        // Adjust accuracy based on weather
        if (BattleField.currentWeather == Weather.RAIN) {
            e.eventData.moveUsed.perfectAccuracy();
        } else if (BattleField.currentWeather == Weather.SUNNY) {
            e.eventData.moveUsed.setAccuracy(50);
        }

        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.CONFUSION_ID, 10);
        return 0;
    }

}
