package project.game.move.movelist;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class FlyingMoveList {

    public static int hurricane(EventManager e) {
        // Adjust accuracy based on weather
        if (e.data.battleData.isCurrentWeather(WeatherEffect.Rain)) {
            e.data.moveUsed.perfectAccuracy();
        } else if (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            e.data.moveUsed.setAccuracy(50);
        }

        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Confusion, 10);
        return 0;
    }

}
