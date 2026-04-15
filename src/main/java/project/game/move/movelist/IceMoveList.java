package project.game.move.movelist;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionChangeWeather;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class IceMoveList {

    public static int avalanche(EventManager e) {
        if (e.data.user.getConditions().tookDamage()) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int blizzard(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Freeze, 10);
        return 0;
    }

    public static int hail(EventManager e) {
        MoveActionChangeWeather.changeWeather(e, WeatherEffect.Hail);
        return 0;
    }

    public static int haze(EventManager e) {
        MoveActionChangeStat.resetStats(e, e.data.user);
        MoveActionChangeStat.resetStats(e, e.data.attackTarget);
        return 0;
    }

    public static int iceBeam(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Freeze, 10);
        return 0;
    }

    public static int icePunch(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Freeze, 10);
        return 0;
    }

    public static int iceSpinner(EventManager e) {
        MoveActionAttack.attackTarget(e);
        // TODO: Clears Terrain effect
        return 0;
    }

    public static int icyWind(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, -1, 0, 0));
        return 0;
    }
}
