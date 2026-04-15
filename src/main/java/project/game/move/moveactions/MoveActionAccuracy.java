package project.game.move.moveactions;

import java.util.Random;

import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;
import project.game.pokemon.Pokemon;

public interface MoveActionAccuracy {
    // Protection Functions
private static void defenderProtects(Pokemon p) {
    if (!p.getConditions().getProtect().isActive()) {
        return;
    }
        
    p.getConditions().getProtect().setActive(false);
    throw new MoveInterruptedException("But %s protected itself!", p);
}

// Accuracy Function
    public static void rollForAccuracy(EventManager eventManager) {
        EventData data = eventManager.data;

        Pokemon attacker = data.user;
        Pokemon defender = data.attackTarget;
        Move move = data.moveUsed;

        eventManager.notifyAllPokemon(EventID.MOVE_ACCURACY);
        defenderProtects(defender);

        int accuracy = move.getAccuracy();

        if (accuracy == Move.ALWAYS_HITS || defender.getConditions().isImmobilized()) {
            data.moveHits = true;
            eventManager.notifyAllPokemon(EventID.MOVE_HITS);
            return;
        }
		
        double modifiedAccuracy = 0.01 * accuracy
        * ((double) attacker.getAccuracy().getPower() / (double) defender.getEvasion().getPower());

        if (new Random().nextDouble() > modifiedAccuracy) {
            data.moveHits = false;
            throw new MoveInterruptedException("But %s avoided the attack!", defender); 
        }
            
        data.moveHits = true;
        eventManager.notifyAllPokemon(EventID.MOVE_HITS);
    }
}
