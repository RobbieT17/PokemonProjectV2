package project.game.move.movelist;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class FlyingMoveList {

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
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Flinch, 30);
        return 0;
    }

    public static int fly(EventManager e) {
        MoveActionChangeCondition.enterImmuneState(e, StatusConditionID.Fly_State);
        return 0;
    }

    public static int hurricane(EventManager e) {
        // Adjust accuracy based on weather
        if (e.battleData.isCurrentWeather(WeatherEffect.Rain)) {
            e.eventData.moveUsed.perfectAccuracy();
        } else if (e.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            e.eventData.moveUsed.setAccuracy(50);
        }

        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Confusion, 10);
        return 0;
    }

}
