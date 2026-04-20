package project.game.move.movelist;

import project.game.battle.BattleLog;
import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.move.Move;
import project.game.move.Movedex;
import project.game.move.moveactions.MoveActionAttack;
import project.game.move.moveactions.MoveActionBracing;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.processors.AdditionalEffectsProcessor;
import project.game.utility.RandomValues;

public class NormalMoveList {


    public static int endure(EventManager e) {
        MoveActionBracing.pokemonProtects(e, e.data.user.getConditions().getEndure(), e.data.user + " braced itself!");
        return 0;
    }

    public static int facade(EventManager e) {
        Pokemon a = e.data.user;
        if (a.getConditions().hasKey(StatusConditionID.Burn) ||
            a.getConditions().hasKey(StatusConditionID.Paralysis) ||
            a.getConditions().hasKey(StatusConditionID.Poison)) {
            e.data.moveUsed.doublePower();
        }
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int falseSwipe(EventManager e) {
        e.data.attackTarget.getConditions().getEndure().setActive(true);
        MoveActionAttack.attackTarget(e);
        return 0;
    }

    public static int growth(EventManager e) {
        AdditionalEffectsProcessor aep = new AdditionalEffectsProcessor(e);

        // Doubles Attack and Special-Attack in Sunny weather
        if (e.data.battleData.isCurrentWeather(WeatherEffect.Sunny)) {
            aep.prodStatMods(2, 0, 2, 0, 0, 0, 0);
        } 
        
        aep.process();
        return 0;
    }


    public static int protect(EventManager e) {
        MoveActionBracing.pokemonProtects(e, e.data.user.getConditions().getProtect(), e.data.user + " protected itself!");
        return 0;
    }


    // TODO: Needs fixing
    public static int sleepTalk(EventManager e) {
        Pokemon a = e.data.user;
        Move m = e.data.moveUsed;
        if (!a.getConditions().hasKey(StatusConditionID.Sleep)) {
            BattleLog.add(Move.FAILED);
            return 0;
        }

        Move randomMove = m;
        while (randomMove.equals(m)) {
            int i = RandomValues.generateInt(0, a.getMoves().size() - 1);
            randomMove = a.getMoves().get(i);
        }

        // Sets new move
        a.setMoveSelected(randomMove);

        BattleLog.add("%s used %s!", a, randomMove);
        
        EventManager newE = new EventManager(e.data.battleData, a);
        Movedex.processMove(newE);
        return 0;
    }

}
