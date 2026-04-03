package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeStat;

public class SpaceMoveList {

    public enum MoveName {
        Swift(SpaceMoveList::swift),
        Cosmic_Punch(SpaceMoveList::cosmicPunch),
        Gravity_Well(SpaceMoveList::gravityWell),
        Stellar_Beam(SpaceMoveList::stellarBeam),
        Orbit_Strike(SpaceMoveList::orbitStrike);

        private final Function<EventManager, Integer> func;

        MoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int swift(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int cosmicPunch(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, -1, 0, 0));
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
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, -1, 0, 0, 0));
        return 0;
    }
}

