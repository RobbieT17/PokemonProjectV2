package project.game.move.movelist;

import project.game.battle.Weather;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionChangeWeather;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class RockMoveList {

    public static int rockSlide(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Flinch, 30);
        return 0;
    }

    public static int rockTomb(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, -1, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int sandstorm(EventManager e) {
        MoveActionChangeWeather.changeWeather(e, Weather.SANDSTORM);
        return 0;
    }

    public static int smackDown(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.leaveImmuneState(e, StatusConditionID.Fly_State, "Fell from the sky!");
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Grounded);
        return 0;
    }
}
