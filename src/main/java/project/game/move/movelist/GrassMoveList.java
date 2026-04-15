package project.game.move.movelist;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.move.moveactions.MoveActionHealthRestore;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class GrassMoveList {

    public static int bulletSeed(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int energyBall(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, -1, 0, 0, 0), 10);
        return 0;
    }

    public static int frenzyPlant(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionCharge.rechargeMove(e);
        return 0;
    }

    public static int gigaDrain(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int grassKnot(EventManager e) {
        Pokemon d = e.data.attackTarget;
        e.data.moveUsed.setPower(
            d.getWeight() <= 21.8 ? 20 :
            d.getWeight() <= 54.9 ? 40 :
            d.getWeight() <= 110 ? 60 :
            d.getWeight() < 220.2 ? 80 :
            d.getWeight() < 440.7 ? 100 : 120
        );
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int leafStorm(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, -2, 0, 0, 0, 0));
        return 0;
    }

    public static int leechSeed(EventManager e) {
        MoveActionAccuracy.rollForAccuracy(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Seeded);
        return 0;
    }

    public static int magicalLeaf(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int petalBlizzard(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int petalDance(EventManager e) {
        MoveActionCharge.rampageMove(e);
        return 0;
    }

    public static int powerWhip(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int razorLeaf(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int seedBomb(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int sleepPowder(EventManager e) {
        MoveActionAccuracy.rollForAccuracy(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Sleep);
        MoveAction.displayFailMessage(e);
        return 0;
    }

    public static int solarBeam(EventManager e) {
        if (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            MoveActionAttack.attackTarget(e);
        } else {
            MoveActionCharge.chargeMove(e);
        }
        return 0;
    }

    public static int stunSpore(EventManager e) {
        MoveActionAccuracy.rollForAccuracy(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Paralysis);
        return 0;
    }

    public static int synthesis(EventManager e) {
        MoveAction.targetsUser(e.data);
        double percent = (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) ? 67 :
                         (e.data.battleData.isCurrentWeather(WeatherEffect.Clear)) ? 50 : 25;
        MoveActionHealthRestore.restoreHp(e, percent);
        return 0;
    }

    public static int trailblaze(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, 1, 0, 0));
        return 0;
    }

    public static int vineWhip(EventManager e) {
        MoveActionAttack.attackTarget(e);
        return 0;
    }
}
