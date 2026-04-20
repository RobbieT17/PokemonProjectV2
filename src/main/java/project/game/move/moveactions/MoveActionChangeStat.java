package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.StatPoint;
import project.game.utility.RandomValues;

public interface MoveActionChangeStat extends MoveAction {
     /**
     * Changes each Pokemon stats
     */
    private static void changeEachStat(EventManager eventManager, int[] stats, int mods[]) {
        EventData data  = eventManager.data;
        Pokemon p = data.effectTarget;
    
        for (int i = 0; i < stats.length; i++) {
            int change = stats[i] * mods[i];
            if (change == 0) {
                continue;
            }
        
            StatPoint s = p.getStats()[i];
            if (s.isAtHighestOrLowestStage(change)) {
                BattleLog.add("But %s's %s won't go any %s!", p, s, (change > 0) ? "higher" : "lower");
                data.statFailed = true;
                continue;
            }
            p.getStats()[i].changeStage(change);
            BattleLog.add("%s's %s %s%s!", p, s, (change > 0) ? "rose" : "fell", StatPoint.sizeOfChange(change));
        }  
    }

    public static void changeStats(EventManager eventManager, int[] stats, double chance) {
        EventData data  = eventManager.data;
        data.statProb = chance;
        data.statChanges = stats;

        eventManager.notifyUserPokemon(EventID.ATK_STAT_CHANGE);
        eventManager.notifyEffectTargetPokemon(EventID.DEF_STAT_CHANGE);

        if (RandomValues.chance(chance)) {
            MoveActionChangeStat.changeEachStat(eventManager, data.statChanges, data.statMods);    
        }
        else {
            data.statFailed = true;
        }
       
    }  

    public static void changeStats(EventManager eventManager, int[] stats) {
        MoveActionChangeStat.changeStats(eventManager, stats, 100);
    }

    // Resets all stat changes back to neutral
    public static void resetStats(EventManager eventManager, Pokemon p) {
        for (StatPoint s : p.getStats()) {
            s.setStage(0);
        } 
        BattleLog.add("%s stat changes were nullified!", p);
    }

}
