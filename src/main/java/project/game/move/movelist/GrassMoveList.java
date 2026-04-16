package project.game.move.movelist;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;

import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionCharge;
import project.game.move.moveactions.MoveActionHealthRestore;
import project.game.pokemon.Pokemon;

public class GrassMoveList {


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

    public static int solarBeam(EventManager e) {
        if (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            MoveActionAttack.attackTarget(e);
        } else {
            MoveActionCharge.chargeMove(e);
        }
        return 0;
    }

    public static int synthesis(EventManager e) {
        MoveAction.targetsUser(e.data);
        double percent = (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) ? 67 :
                         (e.data.battleData.isCurrentWeather(WeatherEffect.Clear)) ? 50 : 25;
        MoveActionHealthRestore.restoreHp(e, percent);
        return 0;
    }

}
