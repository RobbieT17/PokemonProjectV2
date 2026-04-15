package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.pokemon.Pokemon;

public interface MoveActionDamagePct {
    

    // Pokemon takes damage based on some percent of the damage dealt
    public static void recoilDamage(EventManager eventManager, double percent) {
        EventData data  = eventManager.data;
        Pokemon p = data.user;

        data.recoilPercent = percent;

        if (p.getDamageDealt() == 0) {
            return;
        }

        int damage = (int) (0.01 * percent * p.getDamageDealt()); 
        BattleLog.add("%s took %d damage from the recoil!", p, damage);
        p.addDamageReceived(damage);
        p.takeDamage(damage);
    }

    public static void drainHP(EventManager eventManager, double percent) {
        EventData data  = eventManager.data;
        Pokemon p = data.user;

        data.drainPercent = percent;

        if (p.getDamageDealt() == 0) {
            return;
        }

        int heal = (int) (0.01 * percent * p.getDamageDealt()); 
        BattleLog.add("%s restored %d HP!", p, heal);
        p.restoreHP(heal);
    }

}
