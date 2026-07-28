package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.pokemon.Pokemon;

public class MoveActionDamagePct {

    private final EventManager eventManager;

    public MoveActionDamagePct(EventManager e) {
        this.eventManager = e;
    }
    
    // Pokemon takes damage based on some percent of the damage dealt
    public void takeRecoilDamage(double percent) {
        EventData data  = this.eventManager.data;
        Pokemon p = data.attackUser;

        data.recoilPercent = percent;

        if (p.getDamageDealt() == 0) {
            return;
        }

        int damage = (int) (0.01 * data.recoilPercent * data.percentMod  * p.getDamageDealt()); 
        BattleLog.add("%s took %d damage from the recoil!", p, damage);
        p.addDamageReceived(damage);
        p.takeDamage(damage);
    }

    public void drainHP(double percent) {
        EventData data  = this.eventManager.data;
        Pokemon p = data.attackUser;
        data.drainPercent = percent;

        if (p.getDamageDealt() == 0) {
            return;
        }

        int heal = (int) (0.01 * data.drainPercent * data.percentMod * p.getDamageDealt()); 
        BattleLog.add("%s restored %d HP!", p, heal);
        p.restoreHP(heal);
    }

}
