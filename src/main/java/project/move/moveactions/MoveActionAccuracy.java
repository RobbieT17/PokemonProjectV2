package project.move.moveactions;

import java.util.Random;

import project.event.EventData;
import project.event.GameEvent;
import project.exceptions.MoveInterruptedException;
import project.move.Move;
import project.pokemon.Pokemon;

public interface MoveActionAccuracy {
    // Protection Functions
private static void defenderProtects(Pokemon p) {
    if (!p.conditions().protect().active()) {
        return;
    }
        
    p.conditions().protect().setActive(false);
    throw new MoveInterruptedException("But %s protected itself!", p);
}

// Accuracy Function
    public static void moveHits(EventData data) {
        Pokemon attacker = data.user;
        Pokemon defender = data.attackTarget;
        Move move = data.moveUsed;

        data.notifyEvent(GameEvent.MOVE_ACCURACY);
        defenderProtects(defender);

        int accuracy = move.accuracy();

        if (accuracy == Move.ALWAYS_HITS || defender.conditions().immobilized()) {
            data.moveHits = true;
            data.notifyEvent(GameEvent.MOVE_HITS);
            return;
        }
		
        double modifiedAccuracy = 0.01 * accuracy
        * ((double) attacker.accuracy().power() / (double) defender.evasion().power());

        if (new Random().nextDouble() > modifiedAccuracy) {
            data.moveHits = false;
            throw new MoveInterruptedException("But %s avoided the attack!", defender); 
        }
            
        data.moveHits = true;
        data.notifyEvent(GameEvent.MOVE_HITS);
    }
}
