package project.game.move.calculations;

import java.util.Random;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.StatPoint;
import project.game.pokemon.stats.Type;

public interface AttackMoveCalculations {
     /*
     * Attacking Pokemon gets a 50% damage boost if the 
     * move it uses matches one of its types.
     */
    private static double sameTypeAttackBonus(Pokemon p, Move m){
        return (m.isType(p.getPokemonType())) ? 1.5 : 1.0;
    }

    /*
     * Sunny/Rainy weather affect the damage output of Fire and Water type moves
     * When Sunny: Fire-Type Moves (50% damage boost), Water-Type Moves (50% damage drop)
     * When Rainy: Fire-Type Moves (50% damage drop), Water-Type Moves (50% damage boost)
     */
    private static double weatherBonus(EventData e) {
        Move m = e.moveUsed;
        return (e.battleData.isCurrentWeather(WeatherEffect.Sunny)) 
        ? (m.isType(Type.Fire)) ? 1.5 : (m.isType(Type.Water)) ? 0.5 : 1.0
        :  (e.battleData.isCurrentWeather(WeatherEffect.Rain)) 
            ? (m.isType(Type.Water)) ? 1.5 : (m.isType(Type.Fire)) ? 0.5 : 1.0
            : 1.0
        ;
    }

      // Generates random number for a critical hit
    private static boolean criticalHit(double rate) {
		return new Random().nextDouble() <= rate;
    }

    // Generates random number (between 0.85 and 1) so moves do slightly more/less damage each time
    private static double random() {
        return 0.85 + (1 - 0.85) * new Random().nextDouble();
    }

    /**
     * Calculates the attack power from the attacker Pokemon     
     * Critical Hits ignore any attack stage drops.
     * 
     * @param atkStat attack stat 
     * @param crit if the move was a critical hit
     */
    private static double calculateAttack(StatPoint atkStat, boolean crit) {
        return crit && atkStat.getStage() < 0 ? atkStat.getBase() : atkStat.getPower();
    }

    /**
     * Calculates the defense power from the defender Pokemon
     * Critical Hits ignore any defense stage boosts.
     * 
     * @param defStat defense stat 
     * @param crit if the move was a critical hit
     */
    private static double calculateDefense(StatPoint defStat, boolean crit) {
        return crit && defStat.getStage() > 0 ? defStat.getBase() : defStat.getPower();
    }

    /**
     * Calculates the amount of damage a Pokemon receives. 
     * Damage depends on many different factors such as:
     * 
     * 1) Attacking Pokemon's attack stat and level
     * 2) Defending Pokemon's defense stat
     * 3) Move Power
     * 4) Type effectiveness of the move
     * 5) STAB (Same Type Attack Bonus)
     * 6) Critical Hits (50% damage boost)
     * 7) Weather Effects
     * 8) Randomness
     * 9) Additional Effects
     * 
     * @param attacker Pokemon attacking with a move
     * @param defender Pokemon receiving the damage
     * @param move Move used by attack
     * @param effectiveness of the move
     * @param isCritical If the move is a critical hit
     * 
     * @return calculated damage
     */
    public static int calculateDamage(EventManager eventManager) {
        EventData data = eventManager.eventData; 
        criticalHit(data.moveUsed.getCritRate()); // Rolls for a critical hit

        eventManager.notifyAllPokemon(EventID.DAMAGE_MULTIPLIER);
        
        Pokemon attacker = data.user;
        Pokemon defender = data.attackTarget;
        Move move = data.moveUsed;
        boolean isCritical = data.criticalHit;
        double effectiveness = data.moveEffectiveness;  

        double stab = sameTypeAttackBonus(attacker, move);
        double weather = weatherBonus(data);
        double crit = isCritical ? 1.5 : 1.0;
        double random = random();
        double addition = data.otherMoveMods;

        double attack = move.getCategory().equals(Move.MoveCategory.Special) 
        ? calculateAttack(attacker.getSpecialAttack(), isCritical) 
        : calculateAttack(attacker.getAttack(), isCritical);

        double defense = move.getCategory().equals(Move.MoveCategory.Special) 
        ? calculateDefense(defender.getSpecialDefense(), isCritical) 
        : calculateDefense(defender.getDefense(), isCritical);
        
        return (int) (((((2 * attacker.getLevel()) / 5.0 + 2) * move.getPower() * (attack / defense)) / 50.0 + 2) 
        * stab * crit * effectiveness * random * weather * addition);
    }

}
