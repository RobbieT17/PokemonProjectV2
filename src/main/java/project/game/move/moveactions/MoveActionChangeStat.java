package project.game.move.moveactions;

import java.util.Random;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.StatPoint;

public interface MoveActionChangeStat extends MoveAction {
     /**
     * Changes one of a Pokemon's stats.
     * @param p Pokemon whose stat will be changed
     * @param change number of stages stat will increase/decrease
     * @param id Stat ID
     * @param chance The stat change success rate
     */
    private static void changeEachStat(EventManager eventManager, int[] stats) {
        EventData data  = eventManager.data;
        Pokemon p = data.effectTarget;
        data.statChanges = stats;

        eventManager.notifyAttackTargetPokemon(EventID.STAT_CHANGE);
        
        for (int i = 0; i < stats.length; i++) {
            int change = stats[i];
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
        if (data.user.getConditions().isFainted() || new Random().nextDouble() > chance * 0.01) {
            data.statFailed = true;
            return;
        }
        changeEachStat(eventManager, stats);
    }

    public static void changeStats(EventManager eventManager, int[] stats) {
        changeStats(eventManager, stats, 100);
    }

    // Resets all stat changes back to neutral
    public static void resetStats(EventManager eventManager, Pokemon p) {
        for (StatPoint s : p.getStats()) s.setStage(0);
        BattleLog.add("%s stat changes were cleared...", p);
    }

}
