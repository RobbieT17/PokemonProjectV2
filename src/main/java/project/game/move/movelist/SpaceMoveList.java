package project.game.move.movelist;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeStat;

public class SpaceMoveList {

    public static int swift(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    // Cosmic Punch: a heavy cosmic-infused punch that deals damage and lowers the target's Special Defense by 1 stage.
    // Type: Space, Category: Physical, Power: 80, PP: 15, Accuracy: 95
    public static int cosmicPunch(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, -1, 0, 0));
        return 0;
    }

    // Gravity Well: creates an intense gravity field that slows opponents, prevents switching, and grounds airborne targets.
    // Type: Space, Category: Status, Power: —, PP: 10, Accuracy: 100
    public static int gravitySwitch(EventManager e) {
        // TODO: Implement gravity field effect
        return 0;
    }

    // Stellar Beam: fires a concentrated beam of stellar energy that deals heavy special damage.
    // Type: Space, Category: Special, Power: 100, PP: 10, Accuracy: 90
    public static int stellarBeam(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    // Orbit Strike: attacks in a fast orbital arc, dealing damage and lowering the target's Special Attack by 1 stage.
    // Type: Space, Category: Physical, Power: 85, PP: 15, Accuracy: 95
    public static int orbitStrike(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, -1, 0, 0, 0));
        return 0;
    }

}

