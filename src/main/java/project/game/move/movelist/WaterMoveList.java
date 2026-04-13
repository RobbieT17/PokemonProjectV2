package project.game.move.movelist;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionChangeWeather;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class WaterMoveList {

    public static int aquaJet(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int aquaTail(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int chillingWater(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(-1, 0, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int dive(EventManager e) {
        MoveActionChangeCondition.enterImmuneState(e, StatusConditionID.Dive_State);
        return 0;
    }

    public static int flipTurn(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int hydroCannon(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionCharge.rechargeMove(e);
        return 0;
    }

    public static int hydroPump(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int liquidation(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, -1, 0, 0, 0, 0, 0), 20);
        return 0;
    }

    public static int muddyWater(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, 0, -1, 0), 30);
        return 0;
    }

    public static int rainDance(EventManager e) {
        MoveActionChangeWeather.changeWeather(e, WeatherEffect.Rain);
        return 0;
    }

    public static int surf(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int waterGun(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int waterPledge(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int waterPulse(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Confusion, 20);
        return 0;
    }

    public static int waveCrash(EventManager e) {
        MoveActionAttackDamage.dealDamageRecoil(e, 33);
        return 0;
    }

    public static int whirlpool(EventManager e) {
        if (e.eventData.attackTarget.getConditions().hasKey(StatusConditionID.Dive_State)) {
            e.eventData.moveUsed.doublePower();
        }
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Bound);
        return 0;
    }

    public static int withdraw(EventManager e) {
        MoveAction.targetsUser(e.eventData);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 1, 0, 0, 0, 0, 0));
        return 0;
    }
}
