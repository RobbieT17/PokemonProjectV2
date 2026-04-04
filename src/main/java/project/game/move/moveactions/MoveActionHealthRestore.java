package project.game.move.moveactions;

import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.MoveAction;
import project.game.pokemon.Pokemon;

public interface MoveActionHealthRestore extends MoveAction {
        // Restores a percentage of a Pokemon's maximum HP
    public static void restoreHp(EventManager eventManager, double percent) {
        EventData data  = eventManager.eventData;
        Pokemon p = data.attackTarget;

        data.healPercent = percent;
        if (p.getHp().atFullHP()) throw new MoveInterruptedException("But %s is already at full health!", p);
        p.restoreHpPercentMaxHP(percent, "");
    }
}
