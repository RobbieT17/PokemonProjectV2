package project.game.move.calculations;

import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.MoveInterruptedException;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Type;
import project.game.pokemon.stats.TypeName;

public interface MoveEffectiveCalculations {

    private static String noEffectOn(Pokemon p) {
        return String.format("But it doesn't affect %s...", p);
    }

     /**
     * Calculates the effectiveness of a move on a Pokemon
     * @param type the move's type
     * @param p the pokemon's types
     */
    public static double typeEffectiveness(Type type, Pokemon p){
        double effect = 1.0; // Default type effectiveness

        for (TypeName t : p.getPokemonType().getTypeResistances()) // Pokemon resists type, halves damage
            if (t == type.toTypeName()) effect *= 0.5;
        
        for (TypeName t : p.getPokemonType().getTypeWeaknesses()) // Pokemon weak to type, doubles damage
            if (t == type.toTypeName()) effect *= 2;

        for (TypeName t : p.getPokemonType().getTypeImmunities()) { // Pokemon immune to type, nullifies damage
            // if (immunityExceptionFound(type, p)) break;
            if (t == type.toTypeName()) effect = 0;
        } 
        
        return effect;
    }

    /*
    * Calculates effectiveness of the move on the defender
    * No damage is dealt if defending is immune to the attack
    */
    public static void calculateMoveEffectiveness(EventManager eventManager) {
        EventData data = eventManager.data;
        Pokemon p = data.attackTarget;
        double effectiveness = typeEffectiveness(data.moveUsed.getMoveType(), p);

        data.moveEffectiveness = effectiveness;

        eventManager.notifyAllPokemon(EventID.MOVE_EFFECTIVENESS);

        if (effectiveness == 0) throw new MoveInterruptedException(MoveEffectiveCalculations.noEffectOn(p));     
    }
 
    // A message to be displayed indicating if the move was super effective or not
    public static String isSuperEffective(double effect) {
        return (effect > 1.0) 
        ? "It's super effective!" 
        : (effect < 1.0) ? "It's not very effective..." : "";
    }
}
