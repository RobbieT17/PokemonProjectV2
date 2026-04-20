package project.game.move.movelist;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;

import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionCharge;
import project.game.move.moveactions.MoveActionHealthRestore;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.processors.AdditionalEffectsProcessor;

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
            e.data.user.getConditions().removeCondition(StatusConditionID.Charge);
        } else {
            MoveActionCharge.enterChargeState(e, StatusConditionID.Charge);
            if (!e.data.moveEndedEarly) {
                MoveActionAttack.attackTarget(e);
            }
        }
        return 0;
    }

    public static int synthesis(EventManager e) {
        double mod;

        // Restores more HP in Sunny weather
        if (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            mod = 1.33;
        }
        else if (e.data.battleData.isCurrentWeather(WeatherEffect.Clear)) {
            mod = 1.0;
        }
        else { // Restores less HP other weather conditions
            mod = 0.5;
        }
        
        AdditionalEffectsProcessor aep = new AdditionalEffectsProcessor(e); 
        aep.prodPercentMods(mod);
        aep.process();
        return 0;
    }

}
