package project.game.move;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionBracing;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionChangeWeather;
import project.game.move.moveactions.MoveActionCharge;
import project.game.move.moveactions.MoveActionDamagePct;
import project.game.move.moveactions.MoveActionHealthRestore;
import project.game.move.moveactions.MoveActionSemiImmuneState;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusCondition.StatusConditionID;
import project.game.pokemon.stats.StatPoint;
import project.game.utility.Protection;

public class MoveActions {

    private MoveActionAccuracy moveActionAccuracy;
    private MoveActionAttack moveActionAttack;
    private MoveActionBracing moveActionBracing;
    private MoveActionChangeCondition moveActionChangeCondition;
    private MoveActionChangeStat moveActionChangeStat;
    private MoveActionChangeWeather moveActionChangeWeather;
    private MoveActionCharge moveActionCharge;
    private MoveActionDamagePct moveActionDamagePct;
    private MoveActionHealthRestore moveActionHealthRestore;
    private MoveActionSemiImmuneState moveActionSemiImmuneState;

    public MoveActions(EventManager e) {
        this.moveActionAccuracy = new MoveActionAccuracy(e);
        this.moveActionAttack = new MoveActionAttack(e);
        this.moveActionBracing = new MoveActionBracing(e);
        this.moveActionChangeCondition = new MoveActionChangeCondition(e);
        this.moveActionChangeStat = new MoveActionChangeStat(e);
        this.moveActionChangeWeather = new MoveActionChangeWeather(e);
        this.moveActionCharge = new MoveActionCharge(e);
        this.moveActionDamagePct = new MoveActionDamagePct(e);
        this.moveActionHealthRestore = new MoveActionHealthRestore(e);
        this.moveActionSemiImmuneState = new MoveActionSemiImmuneState(e);
    }

    public static int[] stats(int atk, int def, int spAtk, int spDef, int spd, int acc, int eva) {
        return new int[] {atk, def, spAtk, spDef, spd, acc, eva};
    }

    public void rollForAccuracy() {
        this.moveActionAccuracy.rollForAccuracy();
    }

    public void attackTarget() {
        this.moveActionAttack.attackTarget();
    }

    public void takeConfusionDamage() {
        this.moveActionAttack.takeConfusionDamage();
    }

    public void pokemonProtects(Protection name, String success) {
        this.moveActionBracing.pokemonProtects(name, success);
    }
    
    public void applyCondition(StatusConditionID id, double chance) {
        this.moveActionChangeCondition.applyCondition(id, chance);
    }

    public void applyCondition(StatusConditionID id) {
        this.applyCondition(id, 100);
    }

    public void applyConditionByName(String name) {
        this.moveActionChangeCondition.applyCondition(StatusConditionID.valueOf(name), 100);
    }

    public void logStatChange(Pokemon p, StatPoint s, int change) {
        this.moveActionChangeStat.logStatChange(p, s, change);
    }

    public void changeStats(int[] stats, double chance) {
        this.moveActionChangeStat.changeStats(stats, chance);
    }

    public void changeStats(int[] stats) {
        this.changeStats(stats, 100);
    }

    public void resetStats(Pokemon p) {
        this.moveActionChangeStat.resetStats(p);
    }

    public void changeWeather(WeatherEffect w) {
        this.moveActionChangeWeather.changeWeather(w);
    }

    public void enterChargeState(StatusConditionID id) {
        this.moveActionCharge.enterChargeState(id);
    }

    public void takeRecoilDamage(double percent) {
        this.moveActionDamagePct.takeRecoilDamage(percent);
    }

    public void drainHP(double percent) {
        this.moveActionDamagePct.drainHP(percent);
    }

    public void applyHpRestorePercent(double percent, String message, boolean checkValidation) {
        this.moveActionHealthRestore.applyHpRestorePercent(percent, message, checkValidation);
    }

    public void applyHpRestorePercent(double percent, String message) {
        this.applyHpRestorePercent(percent, message, false);
    }

    public void enterImmuneState(StatusConditionID state) {
        this.moveActionSemiImmuneState.enterImmuneState(state);
    }

    public void leaveImmuneState(StatusConditionID state, String message) {
        this.moveActionSemiImmuneState.leaveImmuneState(state, message);
    }

}
    

