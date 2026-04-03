package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class PoisonMoveList {

    public enum MoveName {

        Acid_Spray(PoisonMoveList::acidSpray),
        Poison_Jab(PoisonMoveList::poisonJab),
        Poison_Powder(PoisonMoveList::poisonPowder),
        Toxic(PoisonMoveList::toxic),
        Venoshock(PoisonMoveList::venoshock);

        private final Function<EventManager, Integer> func;

        MoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int acidSpray(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, -2, 0, 0, 0));
        return 0;
    }

    public static int poisonJab(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.POISON_ID, 30);
        return 0;
    }

    public static int poisonPowder(EventManager e) {
        MoveActionAccuracy.moveHits(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.POISON_ID);
        return 0;
    }

    public static int toxic(EventManager e) {
        MoveActionAccuracy.moveHits(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.BAD_POISON_ID);
        return 0;
    }

    public static int venoshock(EventManager e) {
        if (e.eventData.attackTarget.getConditions().hasKey(StatusConditionID.POISON_ID)) {
            e.eventData.moveUsed.doublePower();
        }
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }
}
