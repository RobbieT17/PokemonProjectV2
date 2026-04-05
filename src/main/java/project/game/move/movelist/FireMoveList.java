package project.game.move.movelist;

import project.game.battle.BattleLog;
import project.game.battle.Weather;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionChangeWeather;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class FireMoveList {

    public static int blastBurn(EventManager e) {
        MoveActionAccuracy.moveHits(e);
        MoveActionCharge.rechargeMove(e);
        return 0;
    }

    public static int ember(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10); 
        return 0;
    }

    public static int fireBlast(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 30);
        return 0;
    }

    public static int fireFang(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Flinch, 30);
        return 0;
    }

    public static int firePledge(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int firePunch(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10);
        return 0;
    }

    public static int flameCharge(EventManager e) {
        MoveAction.affectsUser(e.eventData);
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, 1, 0, 0));
        return 0;
    }

    public static int flamethrower(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10);
        return 0;
    }

    public static int flareBlitz(EventManager e) {
        MoveActionAttackDamage.dealDamageRecoil(e, 33);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10);
        return 0;
    }

    public static int heatCrash(EventManager e) {
        double ratio = e.eventData.user.getWeight() / e.eventData.attackTarget.getWeight();

        e.eventData.moveUsed.setPower(
            ratio < 2 ? 40
            : ratio < 3 ? 60
            : ratio < 4 ? 80
            : ratio < 5 ? 100
            : 120
        );

        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int heatWave(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 10);
        return 0;
    }

    public static int inferno(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn);
        return 0;
    }

    public static int overheat(EventManager e) {
        MoveAction.affectsUser(e.eventData);
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(-2, 0, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int sunnyDay(EventManager e) {
        MoveActionChangeWeather.changeWeather(e, Weather.SUNNY);
        return 0;
    }

    public static int temperFlare(EventManager e) {
        if (e.eventData.user.getConditions().isInterrupted()) {
            e.eventData.moveUsed.doublePower();
        }

        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int willOWisp(EventManager e) {
        MoveActionAccuracy.moveHits(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn);
        BattleLog.add(e.eventData.message);
        return 0;
    }
}
