package project.move.calculations;

import java.util.Random;

import project.battle.BattleField;
import project.battle.Weather;
import project.event.EventData;
import project.event.GameEvent;
import project.move.Move;
import project.pokemon.Pokemon;
import project.pokemon.PokemonStat;
import project.stats.Type;

public interface AttackMoveCalculations {
     /*
     * Attacking Pokemon gets a 50% damage boost if the 
     * move it uses matches one of its types.
     */
    private static double sameTypeAttackBonus(Pokemon p, Move m){
        return (m.isType(p.pokemonType())) ? 1.5 : 1.0;
    }

    /*
     * Sunny/Rainy weather affect the damage output of Fire and Water type moves
     * When Sunny: Fire-Type Moves (50% damage boost), Water-Type Moves (50% damage drop)
     * When Rainy: Fire-Type Moves (50% damage drop), Water-Type Moves (50% damage boost)
     */
    private static double weatherBonus(Move m) {
        return (BattleField.currentWeather == Weather.SUNNY) 
        ? (m.isType(Type.FIRE)) ? 1.5 : (m.isType(Type.WATER)) ? 0.5 : 1.0
        :  (BattleField.currentWeather == Weather.RAIN) 
            ? (m.isType(Type.WATER)) ? 1.5 : (m.isType(Type.FIRE)) ? 0.5 : 1.0
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
    private static double calculateAttack(PokemonStat atkStat, boolean crit) {
        return crit && atkStat.stage() < 0 ? atkStat.base() : atkStat.power();
    }

    /**
     * Calculates the defense power from the defender Pokemon
     * Critical Hits ignore any defense stage boosts.
     * 
     * @param defStat defense stat 
     * @param crit if the move was a critical hit
     */
    private static double calculateDefense(PokemonStat defStat, boolean crit) {
        return crit && defStat.stage() > 0 ? defStat.base() : defStat.power();
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
    public static int calculateDamage(EventData data) { 
        criticalHit(data.moveUsed.critRate()); // Rolls for a critical hit
        data.notifyEvent(GameEvent.DAMAGE_MULTIPLIER);
        
        Pokemon attacker = data.user;
        Pokemon defender = data.attackTarget;
        Move move = data.moveUsed;
        boolean isCritical = data.criticalHit;
        double effectiveness = data.moveEffectiveness;  

        double stab = sameTypeAttackBonus(attacker, move);
        double weather = weatherBonus(move);
        double crit = isCritical ? 1.5 : 1.0;
        double random = random();
        double addition = data.otherMoveMods;

        double attack = move.category().equals(Move.SPECIAL) 
        ? calculateAttack(attacker.specialAttack(), isCritical) 
        : calculateAttack(attacker.attack(), isCritical);

        double defense = move.category().equals(Move.SPECIAL) 
        ? calculateDefense(defender.specialDefense(), isCritical) 
        : calculateDefense(defender.defense(), isCritical);
        
        return (int) (((((2 * attacker.level()) / 5.0 + 2) * move.power() * (attack / defense)) / 50.0 + 2) 
        * stab * crit * effectiveness * random * weather * addition);
    }

}
