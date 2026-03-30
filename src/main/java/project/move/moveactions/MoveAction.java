package project.move.moveactions;

import project.battle.BattleLog;
import project.event.EventData;


@FunctionalInterface
public interface MoveAction {
// Public Variables
    public static final MoveAction DEFAULT_ACTION = e -> MoveActionAttackDamage.dealDamage(e);

// Object Method
    void act(EventData e);
    
    public static void displayFailMessage(EventData data) {
        BattleLog.add(data.message);
    }

}
