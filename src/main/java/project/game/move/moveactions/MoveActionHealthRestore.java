package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;

public interface MoveActionHealthRestore extends MoveAction {

    private static void restoreHp(EventManager eventManager) {
        EventData data = eventManager.data;
        Pokemon p = data.attackTarget;

        eventManager.notifyAttackTargetPokemon(EventID.RESTORE_HEALTH);

        p.restoreHP((int) (data.healAmount * data.percentMod));
        BattleLog.add("%s restored %d HP%s", p, data.healAmount, data.message + "!");

    }

    // Restores a percentage of a Pokemon's maximum HP
    public static void applyHpRestorePercent(EventManager eventManager, double percent, String message, boolean checkValidation) {
        EventData data = eventManager.data;
        Pokemon p = data.attackTarget;
        
        if (checkValidation && p.getHp().atFullHP()) {
            BattleLog.add("But %s is already at full health!", p);
            return;
        }

        data.message = message;
        data.healPercent = percent;
        data.healAmount = p.getPercentMaxHp(data.healPercent);

        MoveActionHealthRestore.restoreHp(eventManager);
    }

    public static void applyHpRestorePercent(EventManager eventManager, double percent, String message) {
        MoveActionHealthRestore.applyHpRestorePercent(eventManager, percent, message, false);
    }


    
}
