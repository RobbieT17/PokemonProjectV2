package project.game.move.movelist;

import project.game.battle.BattleField;
import project.game.battle.BattleLog;
import project.game.battle.Weather;
import project.game.event.EventManager;
import project.game.move.Move;
import project.game.move.Movedex;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionBracing;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.utility.RandomValues;

public class NormalMoveList {

    public static int bodySlam(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.PARALYSIS, 30);
        return 0;
    }

    public static int doubleEdge(EventManager e) {
        MoveActionAttackDamage.dealDamageRecoil(e, 33);
        return 0;
    }

    public static int endure(EventManager e) {
        MoveActionBracing.pokemonProtects(e, e.eventData.user.getConditions().getEndure(), e.eventData.user + " braced itself!");
        return 0;
    }

    public static int facade(EventManager e) {
        Pokemon a = e.eventData.user;
        if (a.getConditions().hasKey(StatusConditionID.BURN) ||
            a.getConditions().hasKey(StatusConditionID.PARALYSIS) ||
            a.getConditions().hasKey(StatusConditionID.POISON)) {
            e.eventData.moveUsed.doublePower();
        }
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int falseSwipe(EventManager e) {
        e.eventData.attackTarget.getConditions().getEndure().setActive(true);
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int furyAttack(EventManager e) {
        MoveActionAttackDamage.multiHit(e);
        return 0;
    }

    public static int gigaImpact(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionCharge.rechargeMove(e);
        return 0;
    }

    public static int growl(EventManager e) {
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, -1, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int growth(EventManager e) {
        MoveAction.targetsUser(e.eventData);
        if (BattleField.currentWeather == Weather.SUNNY) {
            MoveActionChangeStat.changeStats(e, MoveAction.stats(2, 0, 2, 0, 0, 0, 0));
        } else {
            MoveActionChangeStat.changeStats(e, MoveAction.stats(1, 0, 1, 0, 0, 0, 0));
        }
        return 0;
    }

    public static int hyperBeam(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionCharge.rechargeMove(e);
        return 0;
    }

    public static int protect(EventManager e) {
        MoveActionBracing.pokemonProtects(e, e.eventData.user.getConditions().getProtect(), e.eventData.user + " protected itself!");
        return 0;
    }

    public static int rapidSpin(EventManager e) {
        MoveAction.affectsUser(e.eventData);
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, 1, 0, 0));
        return 0;
    }

    public static int scaryFace(EventManager e) {
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, -2, 0, 0));
        return 0;
    }

    public static int scratch(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int shellSmash(EventManager e) {
        MoveAction.targetsUser(e.eventData);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(1, -1, 1, -1, 1, 0, 0));
        return 0;
    }

    public static int slash(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    // TODO: Needs fixing
    public static int sleepTalk(EventManager e) {
        Pokemon a = e.eventData.user;
        Move m = e.eventData.moveUsed;
        if (!a.getConditions().hasKey(StatusConditionID.SLEEP)) {
            BattleLog.add(Move.FAILED);
            return 0;
        }

        Move randomMove = m;
        while (randomMove.equals(m)) {
            int i = RandomValues.generateInt(0, a.getMoves().size() - 1);
            randomMove = a.getMoves().get(i);
        }

        BattleLog.add("%s used %s!", a, randomMove);
        
        EventManager newE = new EventManager(a, e.eventData.attackTarget, randomMove);
        Movedex.processMove(newE);
        return 0;
    }

    public static int smokescreen(EventManager e) {
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, 0, -1, 0));
        return 0;
    }

    public static int sweetScent(EventManager e) {
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, 0, 0, 0, 0, 0, -1));
        return 0;
    }

    public static int swordsDance(EventManager e) {
        MoveAction.targetsUser(e.eventData);
        MoveActionChangeStat.changeStats(e, MoveAction.stats(2, 0, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int tackle(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int tailWhip(EventManager e) {
        MoveActionChangeStat.changeStats(e, MoveAction.stats(0, -1, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int takeDown(EventManager e) {
        MoveActionAttackDamage.dealDamageRecoil(e, 25);
        return 0;
    }

    public static int struggle(EventManager e) {
        MoveActionAttackDamage.dealDamageRecoil(e, 25);
        return 0;
    }
}
