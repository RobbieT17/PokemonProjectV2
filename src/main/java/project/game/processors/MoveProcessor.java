package project.game.processors;

import project.game.event.EventManager;
import project.game.move.Move;
import project.game.move.Move.MoveTarget;
import project.game.pokemon.Pokemon;

public class MoveProcessor {
    
    private final EventManager eventManager;
    private final Pokemon user;
    private final Pokemon target;
    private final Move moveUsed;

    public MoveProcessor(EventManager e) {
        this.eventManager = e;
        this.user = e.eventData.user;
        this.target = e.eventData.attackTarget;
        this.moveUsed = e.eventData.moveUsed;
    }

    /**
     * Constructs the correct move target
     */
    public void findMoveTargets() {
        MoveTarget mt = this.moveUsed.getMoveTarget();
        
    }

    public void processMove() {

    }
}
