package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.battle.Weather;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionChangeWeather;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class IceMoveList {

    public enum IceMoveName {

        Avalanche(IceMoveList::avalanche),
        Blizzard(IceMoveList::blizzard),
        Hail(IceMoveList::hail),
        Haze(IceMoveList::haze),
        Ice_Beam(IceMoveList::iceBeam),
        Ice_Punch(IceMoveList::icePunch),
        Ice_Spinner(IceMoveList::iceSpinner),
        Icy_Wind(IceMoveList::icyWind);

        private final Function<EventManager, Integer> func;

        IceMoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int avalanche(EventManager e) {
        if (e.eventData.user.getConditions().tookDamage()) {
            e.eventData.moveUsed.doublePower();
        }
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int blizzard(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.FREEZE_ID, 10);
        return 0;
    }

    public static int hail(EventManager e) {
        MoveActionChangeWeather.changeWeather(e, Weather.HAIL);
        return 0;
    }

    public static int haze(EventManager e) {
        MoveActionChangeStat.resetStats(e, e.eventData.user);
        MoveActionChangeStat.resetStats(e, e.eventData.attackTarget);
        return 0;
    }

    public static int iceBeam(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.FREEZE_ID, 10);
        return 0;
    }

    public static int icePunch(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.FREEZE_ID, 10);
        return 0;
    }

    public static int iceSpinner(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        // TODO: Clears Terrain effect
        return 0;
    }

    public static int icyWind(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, -1, 0, 0));
        return 0;
    }
}
