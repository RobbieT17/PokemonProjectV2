package project.move.moveactions;

import project.event.EventData;
import project.exceptions.MoveInterruptedException;
import project.pokemon.Pokemon;

public interface MoveActionHealthRestore extends MoveAction {
        // Restores a percentage of a Pokemon's maximum HP
    public static void restoreHp(EventData data, double percent) {
        Pokemon p = data.attackTarget;
        data.healPercent = percent;
        if (p.getHp().atFullHP()) throw new MoveInterruptedException("But %s is already at full health!", p);
        p.restoreHpPercentMaxHP(percent, "");
    }
}
