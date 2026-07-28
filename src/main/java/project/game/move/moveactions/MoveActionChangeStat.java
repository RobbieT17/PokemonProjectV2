package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.StatPoint;
import project.game.utility.RandomValues;

public class MoveActionChangeStat {

    private final EventManager eventManager;

    public MoveActionChangeStat(EventManager e) {
        this.eventManager = e;
    }
    
     /**
     * Changes each Pokemon stats
     */
    private void changeEachStat(int[] stats, int mods[]) {
        EventData data  = this.eventManager.data;
        Pokemon p = data.effectTarget;
    
        for (int i = 0; i < stats.length; i++) {
            int change = stats[i] * mods[i];
            if (change == 0) {
                continue;
            }
        
            StatPoint s = p.getStats()[i];

            data.statFailed = !p.getStats()[i].changeStage(change);
            
            this.logStatChange(p, s, change);
           
        }  
    }

    public void logStatChange(Pokemon p, StatPoint s, int change) {
        if(this.eventManager.data.statFailed) {
            BattleLog.add("But %s's %s won't go any %s!", p, s, (change > 0) ? "higher" : "lower");
        }
        else {
            BattleLog.add("%s's %s %s%s!", p, s, (change > 0) ? "rose" : "fell", StatPoint.sizeOfChange(change));
        }
    }

    public void changeStats(int[] stats, double chance) {
        EventData data  = this.eventManager.data;
        data.statProb = chance;
        data.statChanges = stats;

        this.eventManager.notifyUserPokemon(EventID.ATK_STAT_CHANGE);
        this.eventManager.notifyEffectTargetPokemon(EventID.DEF_STAT_CHANGE);

        if (RandomValues.chance(chance)) {
            this.changeEachStat(data.statChanges, data.statMods);    
        }
        else {
            data.statFailed = true;
        }
       
    }  

    // Resets all stat changes back to neutral
    public void resetStats(Pokemon p) {
        for (StatPoint s : p.getStats()) {
            s.setStage(0);
        } 
        BattleLog.add("%s stat changes were nullified!", p);
    }

}
