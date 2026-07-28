package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;

public class MoveActionHealthRestore {

    private final EventManager eventManager;

    public MoveActionHealthRestore(EventManager e) {
        this.eventManager = e;
    }

    private  void restoreHp() {
        EventData data = this.eventManager.data;
        Pokemon p = data.attackTarget;

        this.eventManager.notifyAttackTargetPokemon(EventID.RESTORE_HEALTH);

        p.restoreHP((int) (data.healAmount * data.percentMod));
        BattleLog.add("%s restored %d HP%s", p, data.healAmount, data.message + "!");

    }

    // Restores a percentage of a Pokemon's maximum HP
    public void applyHpRestorePercent(double percent, String message, boolean checkValidation) {
        EventData data = this.eventManager.data;
        Pokemon p = data.attackTarget;
        
        if (checkValidation && p.getHp().atFullHP()) {
            BattleLog.add("But %s is already at full health!", p);
            return;
        }

        data.message = message;
        data.healPercent = percent;
        data.healAmount = p.getPercentMaxHp(data.healPercent);

        this.restoreHp();
    }

}
