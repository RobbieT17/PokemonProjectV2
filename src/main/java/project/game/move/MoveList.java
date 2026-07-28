package project.game.move;

import project.game.battle.BattleLog;
import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move.MoveCategory;
import project.game.move.Move.MoveStatus;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusCondition;
import project.game.pokemon.effects.StatusCondition.StatusConditionID;
import project.game.pokemon.stats.StatPoint;
import project.game.pokemon.stats.Type;
import project.game.processors.AdditionalEffectsProcessor;
import project.game.processors.MoveProcessor;
import project.game.utility.RandomValues;

public class MoveList {

    public static int avalanche(EventManager e) {
        if (e.data.attackUser.getConditions().tookDamage()) {
            e.data.moveUsed.doublePower();
        }
        e.moveActions.attackTarget();
        return 0;
    }

    /* Sharply raises a stat based on the current weather:
        Sunny - Attack
        Rainy - Sp.Attack
        Sandstorm - Defense
        Hail - Sp. Defense
        None - Speed
    */
    public static int cloudSync(EventManager e) {
        int[] stats = new int[7];

        switch (e.data.battleData.getCurrenWeather().getWeatherEffect()) {
            case WeatherEffect.Sunny -> stats[0] = 2; // Attack
            case WeatherEffect.Sandstorm -> stats[1] = 2; // Defense
            case WeatherEffect.Rain -> stats[2] = 2; // Sp. Atk
            case WeatherEffect.Hail -> stats[3] = 2; // Sp. Def
            case WeatherEffect.Clear -> stats[4] = 2; // Speed
            default -> throw new IllegalStateException("Invalid weather id");  
        }

        e.data.effectTarget = e.data.attackUser;
        e.moveActions.changeStats(stats);

        return 0;
    }

    // Reverses the target's stat boost/drops
    public static int dataCorruption(EventManager e) {
        e.moveActions.rollForAccuracy();
        e.data.effectTarget = e.data.attackTarget;
        Pokemon t = e.data.effectTarget;

        for (StatPoint stat : t.getStats()) {
            stat.setStage(-stat.getStage());
        }

        BattleLog.add("%s's stats were inverted!", t);
        return 0;
    }

    public static int debugger(EventManager e) {
        e.data.invertedType = Type.Bug;
        e.moveActions.attackTarget();
        
        return 0;
    }

    public static int earthquake(EventManager e) {
        if (e.data.attackTarget.getConditions().hasKey(StatusConditionID.Dig_State)) {
            e.data.moveUsed.doublePower();
        }
        e.moveActions.attackTarget();
        return 0;
    }

    // TODO: Remove
    public static int endure(EventManager e) {
        e.moveActions.pokemonProtects(e.data.attackUser.getConditions().getEndure(), e.data.attackUser + " braced itself!");
        return 0;
    }

    // 1.5x power for last attack used if it failed (Perfect accuracy if last move used was a status move)
    public static int errorCorrection(EventManager e) {
        Pokemon p = e.data.attackUser;
        Move lastMove = p.getLastMove();
        
        if (lastMove == null || lastMove.isStatus(MoveStatus.Success) || lastMove.getMoveID() == 946) {
            throw new MoveInterruptedException("But it failed!");
        }

        if (lastMove.isCategory(MoveCategory.Status)) {
            lastMove.perfectAccuracy();
        }
        else {
            lastMove.changePowerByPercent(150);
        }

        // Sets new move
        p.setMoveSelected(lastMove);

        BattleLog.add("%s used %s!", p, lastMove);
        
        EventManager newE = new EventManager(e.data.battleData, p);
        e.startNewProcess(new MoveProcessor(newE));

        return 0;
    }

    public static int facade(EventManager e) {
        Pokemon a = e.data.attackUser;
        if (a.getConditions().hasKey(StatusConditionID.Burn) ||
            a.getConditions().hasKey(StatusConditionID.Paralysis) ||
            a.getConditions().hasKey(StatusConditionID.Poison) ||
            a.getConditions().hasKey(StatusConditionID.Bad_Poison)) {
            e.data.moveUsed.doublePower();
        }
        e.moveActions.attackTarget();
        return 0;
    }

    // Resets all Pokemon stat changes, volatile conditions, and weather/field effects.
    public static int factoryReset(EventManager e) {
        for (Pokemon p : e.data.battleData.getAllPokemonInBattle()) {
            p.resetStatChanges();
            p.getConditions().clearVolatileConditions();
        }
        e.data.battleData.setCurrenWeather(WeatherEffect.Clear);
        return 0;
    }

    public static int falseSwipe(EventManager e) {
        e.data.attackTarget.getConditions().getEndure().setActive(true);
        e.moveActions.attackTarget();
        return 0;
    }

    public static int fakeOut(EventManager e) {
        if (!e.data.attackUser.isFirstRound()) {
            throw new MoveInterruptedException(Move.FAILED);
        }

        e.data.effectTarget = e.data.attackTarget;
        e.moveActions.attackTarget();
        e.moveActions.applyCondition(StatusConditionID.Flinch);

        return 0;
    }

    // Copies the user's conditions to the target. 
    public static int fileCopy(EventManager e) {
        Pokemon user = e.data.attackUser;
        Pokemon target = e.data.attackTarget;
        e.data.effectTarget = target;

        // Ignores accuracy check for allies
        if (user.getOwner() != target.getOwner()) {
            e.moveActions.rollForAccuracy();
        }

        // Copy primary condition
        if (user.getConditions().hasPrimary()) {
            StatusCondition c = user.getConditions().getPrimaryCondition();
            e.moveActions.applyConditionByName(c.getEffectName());
        }

        // Copies all other conditions
        for (StatusCondition c : user.getConditions().getVolatileConditions().values()) {
            e.moveActions.applyConditionByName(c.getEffectName());
        }

        return 0;
    }

    // Transfer all stat changes to the target. The user loses those effects
    public static int fileTransfer(EventManager e) {
        Pokemon user = e.data.attackUser;
        Pokemon target = e.data.attackTarget;
        e.data.effectTarget = target;

        // Ignores accuracy check for allies
        if (user.getOwner() != target.getOwner()) {
            e.moveActions.rollForAccuracy();
        }

        for (int i = 0; i < user.getStats().length; i++) {
            StatPoint stat = user.getStats()[i];        
            target.getStats()[i].changeStage(stat.getStage());
            stat.setStage(0);
        }

        BattleLog.add("%s's stat changes were transferred to %s!", user, target);

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
        e.moveActions.attackTarget();
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
        int power = (int) (25.0 * e.data.attackTarget.getSpeed().calcPower() 
                           / (double) e.data.attackUser.getSpeed().calcPower() + 1);
        e.data.moveUsed.setPower(power);
        e.moveActions.attackTarget();
        return 0;
    }

    public static int haze(EventManager e) {
        e.moveActions.resetStats(e.data.attackUser);
        e.moveActions.resetStats(e.data.attackTarget);
        return 0;
    }

    public static int heatCrash(EventManager e) {
        double ratio = e.data.attackUser.getWeight() / e.data.attackTarget.getWeight();

        e.data.moveUsed.setPower(
            ratio < 2 ? 40
            : ratio < 3 ? 60
            : ratio < 4 ? 80
            : ratio < 5 ? 100
            : 120
        );

        e.moveActions.attackTarget();
        return 0;
    }

    // Randomly gives the target a non-voltatile status condition
    public static int hijack(EventManager e) {
        e.data.effectTarget = e.data.attackTarget;
        e.moveActions.rollForAccuracy();

        double value = RandomValues.randomDouble();
        StatusConditionID id;

        if (value < 0.05) { // 5% Sleep Chance
            id = StatusConditionID.Sleep;
        }
        else if (value <= 0.05 && value < 0.15) { // 10% Freeze Chance
            id = StatusConditionID.Freeze;
        }
        else if (value <= 0.15 && value < 0.3) { // 15% Paralysis Chance
            id = StatusConditionID.Paralysis;
        }
        else if (value <= 0.3 && value < 0.5) { // 20% Burn Chance
            id = StatusConditionID.Burn;
        }
        else if (value <= 0.5 && value < 0.8) { // 30% Poison Chance
            id = StatusConditionID.Poison;
        }
        else { // 20% Infect Chance
            id = StatusConditionID.Infect;
        }

        e.moveActions.applyCondition(id);

        return 0;
    }

    public static int hurricane(EventManager e) {
        // Adjust accuracy based on weather
        if (e.data.battleData.isCurrentWeather(WeatherEffect.Rain)) {
            e.data.moveUsed.perfectAccuracy();
        } else if (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            e.data.moveUsed.setAccuracy(50);
        }

        e.moveActions.attackTarget();
        new AdditionalEffectsProcessor(e).process();
        return 0;
    }

    public static int malware(EventManager e) {
        e.moveActions.attackTarget();

        int[] stats = new int[7];
        int random = RandomValues.generateInt(0, 6);

        stats[random] = -2;

        e.moveActions.changeStats(stats);
        return 0;
    }

    public static int protect(EventManager e) {
        e.moveActions.pokemonProtects(e.data.attackUser.getConditions().getProtect(), e.data.attackUser + " protected itself!");
        return 0;
    }

    public static int ramRush(EventManager e) {
        e.moveActions.attackTarget();

        int[] stats = new int[7];
        int random = RandomValues.generateInt(0, 2);
        
        // Increase stat based on random value
        switch (random) {
            case 0 -> stats[0] = 1; // Attack
            case 1 -> stats[4] = 1; // Speed
            case 2 -> stats[5] = 1; // Accuracy
            default -> throw new IllegalStateException("Random value of bounds");
        }

        e.moveActions.changeStats(stats);
        return 0;
    }

    // Fully heals the user but removes all stat boosts and beneficial status conditions, user must recharge
    public static int reboot(EventManager e) {
        AdditionalEffectsProcessor aep = new AdditionalEffectsProcessor(e);
        aep.processBeforeMove();
        aep.process();

        Pokemon p = e.data.attackUser;

        // Remove positive conditions
        for (StatusCondition c : p.getConditions().getVolatileConditions().values()) {
            if (c.getId().isPositive()) {
                p.getConditions().removeCondition(c.getEffectName());
            }
        }

        // Removes stat boosts
        for (StatPoint s : p.getStats()) {
            if (s.getStage() > 0) {
                s.setStage(0);
            }
        }

        return 0;
    }

    public static int scorchingSands(EventManager e) {
        e.data.attackUser.getConditions().removeCondition(StatusConditionID.Freeze);
        e.moveActions.attackTarget();
        e.moveActions.applyCondition(StatusConditionID.Burn, 30);
        return 0;
    }

    public static int sleepTalk(EventManager e) {
        Pokemon a = e.data.attackUser;
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
        new MoveProcessor(newE).process();

        return 0;
    }

    public static int smackDown(EventManager e) {
        e.moveActions.attackTarget();
        e.moveActions.leaveImmuneState(StatusConditionID.Fly_State, "Fell from the sky!");
        e.moveActions.applyCondition(StatusConditionID.Grounded);
        return 0;
    }

    
    public static int solarBeam(EventManager e) {
        if (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            e.moveActions.attackTarget();
        } else {
            e.moveActions.enterChargeState(StatusConditionID.Charge);
            e.moveActions.attackTarget();
        }
        return 0;
    }

    public static int stompingTantrum(EventManager e) {
        if (e.data.attackUser.getLastMove().isStatus(MoveStatus.Failed)) {
            e.data.moveUsed.doublePower();
        }
        e.moveActions.attackTarget();
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
        if (e.data.attackUser.getLastMove().isStatus(MoveStatus.Failed)) {
            e.data.moveUsed.doublePower();
        }

        e.moveActions.attackTarget();
        return 0;
    }

    public static int venoshock(EventManager e) {
        if (e.data.attackTarget.getConditions().hasPrimaryCondition(StatusConditionID.Poison) ||
            e.data.attackTarget.getConditions().hasPrimaryCondition(StatusConditionID.Bad_Poison)) 
        {
            e.data.moveUsed.doublePower();
        }
        e.moveActions.attackTarget();
        return 0;
    }

    public static int whirlpool(EventManager e) {
        if (e.data.attackTarget.getConditions().hasKey(StatusConditionID.Dive_State)) {
            e.data.moveUsed.doublePower();
        }
        e.moveActions.attackTarget();
        e.moveActions.applyCondition(StatusConditionID.Bound);
        return 0;
    }

}
