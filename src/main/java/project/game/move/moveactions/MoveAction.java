package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventManager;


@FunctionalInterface
public interface MoveAction {
// Public Variables
    public static final MoveAction DEFAULT_ACTION = e -> MoveActionAttackDamage.dealDamage(e);

// Object Method
    void act(EventManager e);
    
    public static void displayFailMessage(EventManager e) {
        BattleLog.add(e.eventData.message);
    }

}
