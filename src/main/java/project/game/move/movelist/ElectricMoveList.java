package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class ElectricMoveList {

    public enum MoveName {

        Thunder_Punch(ElectricMoveList::thunderPunch);

        private final Function<EventManager, Integer> func;

        MoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int thunderPunch(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.PARALYSIS_ID, 10);
        return 0;
    }

}
