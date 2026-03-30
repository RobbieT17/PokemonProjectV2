package project.move.moveactions;

import project.battle.BattleLog;
import project.event.EventData;
import project.move.Move;
import project.pokemon.Pokemon;
import project.utility.Protection;

public interface  MoveActionBracing extends MoveAction {
     public static void pokemonProtects(EventData data, Protection b, String success) {
        Pokemon p = data.user;
        data.protectionType = b;
        data.message = success;

        if (p.moveSelected().equals(p.lastMove())) b.set();
        else {
            b.reset();
            b.set();   
        }
        BattleLog.add(b.active() ? success : Move.FAILED);
    }
}
