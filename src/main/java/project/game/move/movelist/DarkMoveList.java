package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class DarkMoveList {

    public enum MoveName {
        Bite(DarkMoveList::bite),
        Crunch(DarkMoveList::crunch),
        Dark_Pulse(DarkMoveList::darkPulse),
        Fake_Out(DarkMoveList::fakeOut);

        private final Function<EventManager, Integer> func;

        MoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int bite(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e ,StatusConditionID.FLINCH_ID,30);
        return 0;
    }

    public static int crunch(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, -1, 0, 0, 0, 0, 0), 20);
        return 0;
    }

    public static int darkPulse(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH_ID, 20);
        return 0;
    }

    public static int fakeOut(EventManager e) {
        if (!e.eventData.user.firstRound()) {
            throw new MoveInterruptedException(Move.FAILED);
        }

        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.FLINCH_ID);

        return 0;
    }
}
