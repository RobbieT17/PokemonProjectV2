package project.game.move.moveactions;

import project.data.AdditonalEffects;
import project.data.MoveEffect;
import project.game.battle.BattleLog;
import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public interface MoveActionAdditionalEffects {

    /**
     * Assigns the appropiate target of the effect by looking at the
     * <b>MoveEffect</b> object's <b>self</b> field. The effect target
     * is set to the user if true. The <b>effectTarget</b> field should
     * default to the user Pokemon's attack target (opposing/ally Pokemon).
     * @param me
     * @return
     */
    private static void initEffectTarget(EventManager e, MoveEffect me) {
        e.data.effectTarget = me.isSelf() ? e.data.user : e.data.attackTarget;
    }

    /**
     * Applies all additional effects using the <b>AdditionEffects</b> class.
     * Skips over any null values.
     * @param e EventManager
     */
    public static void applyAdditionEffects(EventManager e) {
        AdditonalEffects ae = e.data.moveUsed.getAdditionalEffects();

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

        if (statChange != null) {
            initEffectTarget(e, statChange);
            MoveActionChangeStat.changeStats(e, statChange.statsToIntArray(), statChange.getChance());
        }

        if (conditionChange != null) {
            initEffectTarget(e, conditionChange);
            StatusConditionID c = StatusConditionID.valueOf(conditionChange.getEffect());
            MoveActionChangeCondition.applyCondition(e, c, conditionChange.getChance());
        }

        if (conditionChange2 != null) {
            initEffectTarget(e, conditionChange2);
            StatusConditionID c = StatusConditionID.valueOf(conditionChange2.getEffect());
            MoveActionChangeCondition.applyCondition(e, c, conditionChange2.getChance());
        }

        if (weatherChange != null) {
            initEffectTarget(e, weatherChange);
            WeatherEffect w = WeatherEffect.valueOf(weatherChange.getEffect());
            MoveActionChangeWeather.changeWeather(e, w);
        }

        // NOT IMPLEMENTED YET
        if (terrainChange != null) {
            initEffectTarget(e, terrainChange);
            
            BattleLog.add("TERRAIN CHANGE NOT IMPLEMENTED");
        }
        
        // NOT IMPLEMENTED YET
        if (barrierChange != null) {
            initEffectTarget(e, barrierChange);
            BattleLog.add("BARRIER CHANGE NOT IMPLEMENTED");
        }   

        // NOT IMPLEMENTED YET
        if (switchOut != null) {
            initEffectTarget(e, switchOut);
            BattleLog.add("SWITCH OUT NOT IMPLEMENTED");
        }

        if (recoil != null) {
            initEffectTarget(e, recoil);
            MoveActionDamagePct.recoilDamage(e, recoil.getPercent());

        }

        if (lifeSteal != null) {
            initEffectTarget(e, lifeSteal);
            MoveActionDamagePct.drainHP(e, lifeSteal.getPercent());
        }

        if (heal != null) { 
            initEffectTarget(e, heal);
            MoveActionHealthRestore.restoreHp(e, heal.getPercent());
        }   
    }
    
} 
