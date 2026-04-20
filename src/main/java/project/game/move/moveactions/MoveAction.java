package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventManager;

public interface MoveAction {

    public static int[] stats(int atk, int def, int spAtk, int spDef, int spd, int acc, int eva) {
        return new int[] {atk, def, spAtk, spDef, spd, acc, eva};
    }

    public static void displayFailMessage(EventManager e) {
        BattleLog.add(e.data.message);
    }


}
