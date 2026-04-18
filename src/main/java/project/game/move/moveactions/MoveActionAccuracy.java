package project.game.move.moveactions;

import java.util.Random;

import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.utility.RandomValues;

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

        eventManager.notifyUserPokemon(EventID.ATK_MOVE_ACCURACY);
        eventManager.notifyAttackTargetPokemon(EventID.DEF_MOVE_ACCURACY);

        defenderProtects(defender);

        int accuracy = move.getAccuracy();

        // Move Guarenteed to hit if has perfect accuracy or the target is immobilized
        if (accuracy == Move.ALWAYS_HITS || defender.getConditions().isImmobilized()) {
            data.moveHits = true;

            eventManager.notifyUserPokemon(EventID.ATK_MOVE_HITS);
            eventManager.notifyAttackTargetPokemon(EventID.DEF_MOVE_HITS);
            return;
        }
		
        // Calcuates hit rate
        double modifiedAccuracy = accuracy * ((double) attacker.getAccuracy().getPower() 
        / (double) defender.getEvasion().getPower());

        if (RandomValues.chance(modifiedAccuracy)) { // Move hits
            data.moveHits = true;
            eventManager.notifyUserPokemon(EventID.ATK_MOVE_HITS);
            eventManager.notifyAttackTargetPokemon(EventID.DEF_MOVE_HITS);
        }
        else { // Move misses
            data.moveHits = false;
            throw new MoveInterruptedException("But %s avoided the attack!", defender); 
        }      
        
    }
}
