package project.move.calculations;

import project.event.EventData;
import project.event.EventManager;
import project.event.GameEvents;
import project.exceptions.MoveInterruptedException;
import project.move.Move;
import project.pokemon.Pokemon;

public interface MoveEffectiveCalculations {
     /**
     * Calculates the effectiveness of a move on a Pokemon
     * @param type the move's type
     * @param p the pokemon's types
     */
    public static double typeEffectiveness(String type, Pokemon p){
        double effect = 1.0; // Default type effectiveness

        for (String t : p.getPokemonType().getTypeResistances()) // Pokemon resists type, halves damage
            if (t.equals(type)) effect *= 0.5;
        
        for (String t : p.getPokemonType().getTypeWeaknesses()) // Pokemon weak to type, doubles damage
            if (t.equals(type)) effect *= 2;

        for (String t : p.getPokemonType().getTypeImmunities()) { // Pokemon immune to type, nullifies damage
            // if (immunityExceptionFound(type, p)) break;
            if (t.equals(type)) effect = 0;
        } 
        
        return effect;
    }

    /*
    * Calculates effectiveness of the move on the defender
    * No damage is dealt if defending is immune to the attack
    */
    public static void moveEffectiveness(EventManager eventManager) {
        EventData data = eventManager.eventData;
        Pokemon p = data.attackTarget;
        double effectiveness = typeEffectiveness(data.moveUsed.getMoveType(), p);

        data.moveEffectiveness = effectiveness;

        eventManager.notifyPokemon(GameEvents.MOVE_EFFECTIVENESS);

        if (effectiveness == 0) throw new MoveInterruptedException(Move.noEffectOn(p));     
    }
 
    // A message to be displayed indicating if the move was super effective or not
    public static String isSuperEffective(double effect) {
        return (effect > 1.0) 
        ? "It's super effective!" 
        : (effect < 1.0) ? "It's not very effective..." : "";
    }
}
