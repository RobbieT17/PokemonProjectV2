package project.game.move.moveactions;

import project.game.battle.BattleLog;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.utility.Protection;

public interface  MoveActionBracing extends MoveAction {
     public static void pokemonProtects(EventManager eventManager, Protection b, String success) {
        EventData data  = eventManager.data;
        Pokemon p = data.user;
        data.protectionType = b;
        data.message = success;

        if (p.getMoveSelected().equals(p.getLastMove())) b.set();
        else {
            b.reset();
            b.set();   
        }
        BattleLog.add(b.isActive() ? data.message : Move.FAILED);
    }
}
