package project.game.move.movelist;

import project.game.battle.BattleLog;
import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionChangeWeather;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class FireMoveList {

    public static int blastBurn(EventManager e) {
        MoveActionAccuracy.rollForAccuracy(e);
        MoveActionCharge.rechargeMove(e);
        return 0;
    }

    public static int ember(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10); 
        return 0;
    }

    public static int fireBlast(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 30);
        return 0;
    }

    public static int fireFang(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Flinch, 30);
        return 0;
    }

    public static int firePledge(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int firePunch(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10);
        return 0;
    }

    public static int flameCharge(EventManager e) {
        MoveAction.affectsUser(e.data);
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, 1, 0, 0));
        return 0;
    }

    public static int flamethrower(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10);
        return 0;
    }

    public static int flareBlitz(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int heatCrash(EventManager e) {
        double ratio = e.data.user.getWeight() / e.data.attackTarget.getWeight();

        e.data.moveUsed.setPower(
            ratio < 2 ? 40
            : ratio < 3 ? 60
            : ratio < 4 ? 80
            : ratio < 5 ? 100
            : 120
        );

        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int heatWave(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10);
        return 0;
    }

    public static int inferno(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn);
        return 0;
    }

    public static int overheat(EventManager e) {
        MoveAction.affectsUser(e.data);
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(-2, 0, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int sunnyDay(EventManager e) {
        MoveActionChangeWeather.changeWeather(e, WeatherEffect.Sunny);
        return 0;
    }

    public static int temperFlare(EventManager e) {
        if (e.data.user.getConditions().isInterrupted()) {
            e.data.moveUsed.doublePower();
        }

        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int willOWisp(EventManager e) {
        MoveActionAccuracy.rollForAccuracy(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn);
        BattleLog.add(e.data.message);
        return 0;
    }
}
