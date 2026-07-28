 package project.game.processors;

import project.data.AdditonalEffects;
import project.data.MoveEffect;
import project.game.battle.BattleLog;
import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.MoveActions;
import project.game.pokemon.effects.StatusCondition.StatusConditionID;

public class AdditionalEffectsProcessor implements Processor {

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
        ? this.eventManager.data.attackUser 
        : this.eventManager.data.attackTarget;
    }

    /**
     * Check if the effect target fainted. Additional effects do not
     * apply to fainted Pokemon.
     */
    private boolean checkIfPokemonFainted() {
        return !this.eventManager.data.effectTarget.getConditions().isFainted();
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
     * Checks if the effect and be applied to the user 
     */
    private boolean canApplySelfEffect(MoveEffect me) {
        if (me == null) {
            return false;
        }
        this.eventManager.data.effectTarget = this.eventManager.data.attackUser;
        return true;
    }

    /**
     * Multiplies each stat modifier by given stat values
     */
    public void prodStatMods(int atk, int def, int spAtk, int spDef, int spd, int acc, int eva) {
        int[] stats = new int[] {atk, def, spAtk, spDef, spd, acc, eva};

        for (int i = 0; i < this.eventManager.data.statMods.length; i++) {
            if (stats[i] == 0) { // Skips zero value to avoid multiplier staying at zero
                continue;
            }
            this.eventManager.data.statMods[i] *= stats[i];
        }
    }

    /**
     * Multiplies percent mod by value
     */
    public void prodPercentMods(double m) {
        this.eventManager.data.percentMod *= m;
    }

    /**
     * Some condition need to be procted before the Pokemon
     * takes action.
     */
    public void processBeforeMove() {
        AdditonalEffects ae = this.eventManager.data.moveUsed.getAdditionalEffects();

        if (ae == null) {
            return;
        }

        MoveEffect charge = ae.getCharge();
        MoveEffect semiImmune = ae.getSemiImmune();

        MoveActions actions = this.eventManager.moveActions;

        if (this.canApplySelfEffect(charge)) {
            StatusConditionID c = StatusConditionID.valueOf(charge.getEffect());
            actions.enterChargeState(c);
        }

        if (this.canApplySelfEffect(semiImmune)) {
            StatusConditionID c = StatusConditionID.valueOf(semiImmune.getEffect());
            actions.enterImmuneState(c);
        }
        
    }

    /**
     * Applies all additional effects using the <b>AdditionEffects</b> class.
     * Skips over any null values. Ignores if user fainted during the round.
     */
    @Override
    public void process() {
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

        MoveActions actions = this.eventManager.moveActions;

        if (this.canApplyEffect(statChange)) {
            actions.changeStats(statChange.statsToIntArray(), statChange.getChance());
        }

        if (this.canApplyEffect(conditionChange)) {
            StatusConditionID c = StatusConditionID.valueOf(conditionChange.getEffect());
            actions.applyCondition(c, conditionChange.getChance());
        }

        if (this.canApplyEffect(conditionChange2)) {
            StatusConditionID c = StatusConditionID.valueOf(conditionChange2.getEffect());
            actions.applyCondition(c, conditionChange2.getChance());
        }

        if (this.canApplyEffect(weatherChange)) {
            WeatherEffect w = WeatherEffect.valueOf(weatherChange.getEffect());
            actions.changeWeather(w);
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

        if (this.canApplySelfEffect(recoil)) {
            actions.takeRecoilDamage(recoil.getPercent());

        }

        if (this.canApplySelfEffect(lifeSteal)) {
            actions.drainHP(lifeSteal.getPercent());
        }

        if (this.canApplyEffect(heal)) { 
            actions.applyHpRestorePercent(heal.getPercent(), "", true);
        }   

    }

    
} 
