package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;

public class DragonMoveList {

    public enum MoveName {

        Breaking_Swipe(DragonMoveList::breakingSwipe),
        Dragon_Breath(DragonMoveList::dragonBreath),
        Dragon_Claw(DragonMoveList::dragonClaw),
        Dragon_Dance(DragonMoveList::dragonDance),
        Dragon_Pulse(DragonMoveList::dragonPulse),
        Dragon_Tail(DragonMoveList::dragonTail),
        Outrage(DragonMoveList::outrage);
        
        private final Function<EventManager, Integer> func;

        MoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int breakingSwipe(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(-1, 0, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int dragonBreath(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.PARALYSIS_ID, 30);
        return 0;
    }

    public static int dragonClaw(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int dragonDance(EventManager e) {
        MoveListHelperFunctions.targetsUser(e.eventData);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(1, 0, 0, 0, 1, 0, 0));
        return 0;
    }

    public static int dragonPulse(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int dragonTail(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int outrage(EventManager e) {
        MoveActionCharge.rampageMove(e);
        return 0;
    }

}
