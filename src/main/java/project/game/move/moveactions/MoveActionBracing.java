package project.game.move.moveactions;

import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.utility.Protection;

public class MoveActionBracing {

    private final EventManager eventManager;

    public MoveActionBracing(EventManager e) {
        this.eventManager = e;
    }

    public void pokemonProtects(Protection b, String success) {
        EventData data  = this.eventManager.data;
        Pokemon p = data.attackUser;
        data.protectionType = b;

        if (p.getMoveSelected().equals(p.getLastMove())) b.set();
        else {
            b.reset();
            b.set();   
        }
        data.message = b.isActive() ? success : Move.FAILED;
    }
}
