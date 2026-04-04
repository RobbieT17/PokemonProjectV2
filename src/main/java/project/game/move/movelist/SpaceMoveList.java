package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeStat;

public class SpaceMoveList {

    public static int swift(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int cosmicPunch(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, -1, 0, 0));
        return 0;
    }

    public static int gravityWell(EventManager e) {
        // TODO: Implement gravity field effect
        return 0;
    }

    public static int stellarBeam(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int orbitStrike(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, -1, 0, 0, 0));
        return 0;
    }
}

