package project.move.moveactions;

import project.battle.BattleLog;
import project.event.EventData;
import project.event.EventManager;
import project.move.Move;
import project.pokemon.Pokemon;
import project.utility.Protection;

public interface  MoveActionBracing extends MoveAction {
     public static void pokemonProtects(EventManager eventManager, Protection b, String success) {
        EventData data  = eventManager.eventData;
        Pokemon p = data.user;
        data.protectionType = b;
        data.message = success;

        if (p.getMoveSelected().equals(p.getLastMove())) b.set();
        else {
            b.reset();
            b.set();   
        }
        BattleLog.add(b.isActive() ? success : Move.FAILED);
    }
}
