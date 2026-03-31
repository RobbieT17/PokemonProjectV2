package project.move.moveactions;

import java.util.Random;

import project.battle.BattleLog;
import project.event.EventData;
import project.event.EventManager;
import project.event.GameEvents;
import project.pokemon.Pokemon;
import project.pokemon.PokemonStat;

public interface MoveActionChangeStat extends MoveAction {
     /**
     * Changes one of a Pokemon's stats.
     * @param p Pokemon whose stat will be changed
     * @param change number of stages stat will increase/decrease
     * @param id Stat ID
     * @param chance The stat change success rate
     */
    private static void changeEachStat(EventManager eventManager, int[] stats) {
        EventData data  = eventManager.eventData;
        Pokemon p = data.effectTarget;
        data.statChanges = stats;

        eventManager.notifyPokemon(GameEvents.STAT_CHANGE);
        
        for (int i = 0; i < stats.length; i++) {
            int change = stats[i];
            if (change == 0) {
                continue;
            }
        
            PokemonStat s = p.getStats()[i];
            if (s.isAtHighestOrLowestStage(change)) {
                BattleLog.add("But %s's %s won't go any %s!", p, s, (change > 0) ? "higher" : "lower");
                data.statFailed = true;
                continue;
            }
            p.getStats()[i].changeStage(change);
            BattleLog.add("%s's %s %s%s!", p, s, (change > 0) ? "rose" : "fell", PokemonStat.sizeOfChange(change));
        }  
    }

    public static void changeStats(EventManager eventManager, int[] stats, double chance) {
        EventData data  = eventManager.eventData;
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
        for (PokemonStat s : p.getStats()) s.setStage(0);
        BattleLog.add("%s stat changes were cleared...", p);
    }

}
