package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.Move;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionBracing;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.battle.BattleField;
import project.game.battle.Weather;
import project.game.battle.BattleLog;
import project.game.utility.RandomValues;

public class NormalMoveList {

    public enum NormalMoveName {

        Body_Slam(NormalMoveList::bodySlam),
        Double_Edge(NormalMoveList::doubleEdge),
        Endure(NormalMoveList::endure),
        Facade(NormalMoveList::facade),
        False_Swipe(NormalMoveList::falseSwipe),
        Fury_Attack(NormalMoveList::furyAttack),
        Giga_Impact(NormalMoveList::gigaImpact),
        Growl(NormalMoveList::growl),
        Growth(NormalMoveList::growth),
        Hyper_Beam(NormalMoveList::hyperBeam),
        Protect(NormalMoveList::protect),
        Rapid_Spin(NormalMoveList::rapidSpin),
        Scary_Face(NormalMoveList::scaryFace),
        Scratch(NormalMoveList::scratch),
        Shell_Smash(NormalMoveList::shellSmash),
        Slash(NormalMoveList::slash),
        Sleep_Talk(NormalMoveList::sleepTalk),
        Smokescreen(NormalMoveList::smokescreen),
        Sweet_Scent(NormalMoveList::sweetScent),
        Swords_Dance(NormalMoveList::swordsDance),
        Tackle(NormalMoveList::tackle),
        Tail_Whip(NormalMoveList::tailWhip),
        Take_Down(NormalMoveList::takeDown),
        Struggle(NormalMoveList::struggle);

        private final Function<EventManager, Integer> func;

        NormalMoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int bodySlam(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.PARALYSIS_ID, 30);
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
        if (a.getConditions().hasKey(StatusConditionID.BURN_ID) ||
            a.getConditions().hasKey(StatusConditionID.PARALYSIS_ID) ||
            a.getConditions().hasKey(StatusConditionID.POISON_ID)) {
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
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, -1, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int growth(EventManager e) {
        MoveListHelperFunctions.targetsUser(e.eventData);
        if (BattleField.currentWeather == Weather.SUNNY) {
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(2, 0, 2, 0, 0, 0, 0));
        } else {
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(1, 0, 1, 0, 0, 0, 0));
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
        MoveListHelperFunctions.affectsUser(e.eventData);
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, 1, 0, 0));
        return 0;
    }

    public static int scaryFace(EventManager e) {
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, -2, 0, 0));
        return 0;
    }

    public static int scratch(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int shellSmash(EventManager e) {
        MoveListHelperFunctions.targetsUser(e.eventData);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(1, -1, 1, -1, 1, 0, 0));
        return 0;
    }

    public static int slash(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int sleepTalk(EventManager e) {
        Pokemon a = e.eventData.user;
        Move m = e.eventData.moveUsed;
        if (!a.getConditions().hasKey(StatusConditionID.SLEEP_ID)) {
            BattleLog.add(Move.FAILED);
            return 0;
        }

        Move randomMove = m;
        while (randomMove.equals(m)) {
            int i = RandomValues.generateInt(0, a.getMoves().length - 1);
            randomMove = a.getMoves()[i];
        }

        BattleLog.add("%s used %s!", a, randomMove);
        randomMove.getAction().act(new EventManager(e.eventData.user, e.eventData.attackTarget));
        return 0;
    }

    public static int smokescreen(EventManager e) {
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, 0, -1, 0));
        return 0;
    }

    public static int sweetScent(EventManager e) {
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, 0, 0, -1));
        return 0;
    }

    public static int swordsDance(EventManager e) {
        MoveListHelperFunctions.targetsUser(e.eventData);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(2, 0, 0, 0, 0, 0, 0));
        return 0;
    }

    public static int tackle(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int tailWhip(EventManager e) {
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, -1, 0, 0, 0, 0, 0));
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
