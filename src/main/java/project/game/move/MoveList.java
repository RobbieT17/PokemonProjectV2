package project.game.move;

import project.game.battle.BattleLog;
import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionBracing;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.move.moveactions.MoveActionSemiImmuneState;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.processors.AdditionalEffectsProcessor;
import project.game.utility.RandomValues;

public class MoveList {

    public static int avalanche(EventManager e) {
        if (e.data.user.getConditions().tookDamage()) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    

    public static int earthquake(EventManager e) {
        if (e.data.attackTarget.getConditions().hasKey(StatusConditionID.Dig_State)) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int endure(EventManager e) {
        MoveActionBracing.pokemonProtects(e, e.data.user.getConditions().getEndure(), e.data.user + " braced itself!");
        return 0;
    }

    public static int facade(EventManager e) {
        Pokemon a = e.data.user;
        if (a.getConditions().hasKey(StatusConditionID.Burn) ||
            a.getConditions().hasKey(StatusConditionID.Paralysis) ||
            a.getConditions().hasKey(StatusConditionID.Poison)) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int falseSwipe(EventManager e) {
        e.data.attackTarget.getConditions().getEndure().setActive(true);
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    

    public static int fakeOut(EventManager e) {
        if (!e.data.user.isFirstRound()) {
            throw new MoveInterruptedException(Move.FAILED);
        }

        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Flinch);

        return 0;
    }

    public static int grassKnot(EventManager e) {
        Pokemon d = e.data.attackTarget;
        e.data.moveUsed.setPower(
            d.getWeight() <= 21.8 ? 20 :
            d.getWeight() <= 54.9 ? 40 :
            d.getWeight() <= 110 ? 60 :
            d.getWeight() <= 220.2 ? 80 :
            d.getWeight() < 440.7 ? 100 : 120
        );
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int growth(EventManager e) {
        AdditionalEffectsProcessor aep = new AdditionalEffectsProcessor(e);

        // Doubles Attack and Special-Attack in Sunny weather
        if (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            aep.prodStatMods(2, 0, 2, 0, 0, 0, 0);
        } 
        
        aep.process();
        return 0;
    }

    public static int gyroBall(EventManager e) {
        int power = (int) (25.0 * e.data.attackTarget.getSpeed().getPower() 
                           / (double) e.data.user.getSpeed().getPower() + 1);
        e.data.moveUsed.setPower(power);
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int haze(EventManager e) {
        MoveActionChangeStat.resetStats(e, e.data.user);
        MoveActionChangeStat.resetStats(e, e.data.attackTarget);
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

    public static int hurricane(EventManager e) {
        // Adjust accuracy based on weather
        if (e.data.battleData.isCurrentWeather(WeatherEffect.Rain)) {
            e.data.moveUsed.perfectAccuracy();
        } else if (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            e.data.moveUsed.setAccuracy(50);
        }

        MoveActionAttack.attackTarget(e);
        new AdditionalEffectsProcessor(e).process();
        return 0;
    }

    public static int protect(EventManager e) {
        MoveActionBracing.pokemonProtects(e, e.data.user.getConditions().getProtect(), e.data.user + " protected itself!");
        return 0;
    }

    public static int scorchingSands(EventManager e) {
        e.data.user.getConditions().removeCondition(StatusConditionID.Freeze);
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Burn, 30);
        return 0;
    }

    public static int sleepTalk(EventManager e) {
        Pokemon a = e.data.user;
        Move m = e.data.moveUsed;
        if (!a.getConditions().hasKey(StatusConditionID.Sleep)) {
            BattleLog.add(Move.FAILED);
            return 0;
        }

        Move randomMove = m;
        while (randomMove.equals(m)) {
            int i = RandomValues.generateInt(0, a.getMoves().size() - 1);
            randomMove = a.getMoves().get(i);
        }

        // Sets new move
        a.setMoveSelected(randomMove);

        BattleLog.add("%s used %s!", a, randomMove);
        
        EventManager newE = new EventManager(e.data.battleData, a);
        Movedex.processMove(newE);
        return 0;
    }

    public static int smackDown(EventManager e) {
        MoveActionAttack.attackTarget(e);
        MoveActionSemiImmuneState.leaveImmuneState(e, StatusConditionID.Fly_State, "Fell from the sky!");
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Grounded);
        return 0;
    }

    
    public static int solarBeam(EventManager e) {
        if (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            MoveActionAttack.attackTarget(e);
        } else {
            MoveActionCharge.enterChargeState(e, StatusConditionID.Charge);
            MoveActionAttack.attackTarget(e);
        }
        return 0;
    }

    public static int stompingTantrum(EventManager e) {
        if (e.data.user.getConditions().isInterrupted()) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
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

    public static int temperFlare(EventManager e) {
        if (e.data.user.getConditions().isInterrupted()) {
            e.data.moveUsed.doublePower();
        }

        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int venoshock(EventManager e) {
        if (e.data.attackTarget.getConditions().hasPrimaryCondition(StatusConditionID.Poison) ||
            e.data.attackTarget.getConditions().hasPrimaryCondition(StatusConditionID.Bad_Poison)) 
        {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int whirlpool(EventManager e) {
        if (e.data.attackTarget.getConditions().hasKey(StatusConditionID.Dive_State)) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.Bound);
        return 0;
    }

}
