package project.game.processors;

import project.data.AdditonalEffects;
import project.data.MoveEffect;
import project.game.battle.BattleLog;
import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionChangeWeather;
import project.game.move.moveactions.MoveActionCharge;
import project.game.move.moveactions.MoveActionDamagePct;
import project.game.move.moveactions.MoveActionHealthRestore;
import project.game.move.moveactions.MoveActionSemiImmuneState;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class AdditionalEffectsProcessor {

    private final EventManager eventManager;

    public AdditionalEffectsProcessor(EventManager e) {
        this.eventManager = e;
    }

    /**
     * Assigns the appropiate target of the effect by looking at the
     * <b>MoveEffect</b> object's <b>self</b> field. The effect target
     * is set to the user if true. The <b>effectTarget</b> field should
     * default to the user Pokemon's attack target (opposing/ally Pokemon).
     * @param me
     * @return
     */
    private void initEffectTarget(MoveEffect me) {
        this.eventManager.data.effectTarget = me.isSelf() 
        ? this.eventManager.data.user 
        : this.eventManager.data.attackTarget;
    }

    /**
     * Check if the effect target fainted. Additional effects do not
     * apply to fainted Pokemon.
     */
    private boolean checkIfPokemonFainted() {
        if (this.eventManager.data.effectTarget.getConditions().isFainted()) {
           return false;
        }
        return true;
    }

    /**
     * Checks if the effect can be applied. 
     */
    private boolean canApplyEffect(MoveEffect me) {
        if (me == null) {
            return false;
        }

        this.initEffectTarget(me);
        return this.checkIfPokemonFainted();
    }

    /**
     * Some condition need to be procted before the Pokemon
     * takes action.
     */
    public void applyAdditionEffectsBefore() {
        AdditonalEffects ae = this.eventManager.data.moveUsed.getAdditionalEffects();

        if (ae == null) {
            return;
        }

        MoveEffect charge = ae.getCharge();
        MoveEffect semiImmune = ae.getSemiImmune();

        if (this.canApplyEffect(charge)) {
            StatusConditionID c = StatusConditionID.valueOf(charge.getEffect());
            MoveActionCharge.enterChargeState(this.eventManager, c);
        }

        if (this.canApplyEffect(semiImmune)) {
            StatusConditionID c = StatusConditionID.valueOf(semiImmune.getEffect());
            MoveActionSemiImmuneState.enterImmuneState(this.eventManager, c);
        }
        
    }

    /**
     * Applies all additional effects using the <b>AdditionEffects</b> class.
     * Skips over any null values. Ignores if user fainted during the round.
     */
    public void applyAdditionEffects() {
        AdditonalEffects ae = this.eventManager.data.moveUsed.getAdditionalEffects();

        if (ae == null) {
            return;
        }

        MoveEffect statChange = ae.getStatChange();
        MoveEffect conditionChange = ae.getConditionChange();
        MoveEffect conditionChange2 = ae.getConditionChange2();
        MoveEffect weatherChange = ae.getWeatherChange();
        MoveEffect terrainChange = ae.getTerrainChange();
        MoveEffect barrierChange = ae.getTerrainChange();
        MoveEffect switchOut = ae.getSwitchOut();
        MoveEffect recoil = ae.getRecoil();
        MoveEffect heal = ae.getHeal();
        MoveEffect lifeSteal = ae.getLifeSteal();

        if (this.canApplyEffect(statChange)) {
            MoveActionChangeStat.changeStats(this.eventManager, statChange.statsToIntArray(), statChange.getChance());
        }

        if (this.canApplyEffect(conditionChange)) {
            StatusConditionID c = StatusConditionID.valueOf(conditionChange.getEffect());
            MoveActionChangeCondition.applyCondition(this.eventManager, c, conditionChange.getChance());
        }

        if (this.canApplyEffect(conditionChange2)) {
            StatusConditionID c = StatusConditionID.valueOf(conditionChange2.getEffect());
            MoveActionChangeCondition.applyCondition(this.eventManager, c, conditionChange2.getChance());
        }

        if (this.canApplyEffect(weatherChange)) {
            WeatherEffect w = WeatherEffect.valueOf(weatherChange.getEffect());
            MoveActionChangeWeather.changeWeather(this.eventManager, w);
        }

        // NOT IMPLEMENTED YET
        if (this.canApplyEffect(terrainChange)) {
            BattleLog.add("TERRAIN CHANGE NOT IMPLEMENTED");
        }
        
        // NOT IMPLEMENTED YET
        if (this.canApplyEffect(barrierChange)) {
            BattleLog.add("BARRIER CHANGE NOT IMPLEMENTED");
        }   

        // NOT IMPLEMENTED YET
        if (this.canApplyEffect(switchOut)) {
            BattleLog.add("SWITCH OUT NOT IMPLEMENTED");
        }

        if (this.canApplyEffect(recoil)) {
            MoveActionDamagePct.recoilDamage(this.eventManager, recoil.getPercent());

        }

        if (this.canApplyEffect(lifeSteal)) {
            MoveActionDamagePct.drainHP(this.eventManager, lifeSteal.getPercent());
        }

        if (this.canApplyEffect(heal)) { 
            MoveActionHealthRestore.restoreHp(this.eventManager, heal.getPercent());
        }   
    }
    
} 
