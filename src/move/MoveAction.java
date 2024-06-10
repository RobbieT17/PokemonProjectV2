package move;

import battle.BattleField;
import battle.BattleLog;
import battle.Weather;
import exceptions.MoveInterruptedException;
import exceptions.PokemonFaintedException;
import java.util.Random;
import pokemon.Pokemon;
import pokemon.PokemonType;
import stats.GameType;
import stats.Stat;
import stats.StatusAction;
import stats.StatusCondition;
import utility.RandomValues;

@FunctionalInterface
public interface MoveAction {
// Public Variables
    public static final MoveAction DEFAULT_ACTION = (a, d, m) -> MoveAction.dealDamage(a, d, m);

// Object Method
    void act(Pokemon attacker, Pokemon defender, Move move);

// Accuracy Function
    public static void moveHits(Pokemon attacker, Pokemon defender, Move move) {
        if (move.accuracy() == Move.INF || defender.conditions().immobilized()) return;
		
        double modifiedAccuracy = 0.01 * move.accuracy() 
        * ((double) attacker.accuracy().power() / (double) defender.evasion().power());

        if (new Random().nextDouble() > modifiedAccuracy) 
            throw new MoveInterruptedException(String.format("But %s avoided the attack!", defender));   
    } 

// Damaging Functions

    /**
     * Calculates the effectiveness of a move on a Pokemon
     * @param type the move's type
     * @param p the pokemon's types
     */
    private static double typeEffectiveness(String type, PokemonType p){
        double effect = 1.0; // Default type effectiveness

        for (String t : p.typeResistances()) // Pokemon resists type, halves damage
            if (t.equals(type)) effect *= 0.5;
        
        for (String t : p.typeWeaknesses()) // Pokemon weak to type, doubles damage
            if (t.equals(type)) effect *= 2;

        for (String t : p.typeImmunities()) // Pokemon immune to type, nullifies damage
            if (t.equals(type)) effect = 0;

        return effect;
    }
 
    // A message to be displayed indicating if the move was super effective or not
    private static String isSuperEffective(double effect) {
        return (effect > 1.0) 
        ? "It's super effective!" 
        : (effect < 1.0) ? "It's not very effective..." : "";
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
    private static double calculateAttack(Stat atkStat, boolean crit) {
        return crit && atkStat.stage() < 0 ? atkStat.base() : atkStat.power();
    }

    /**
     * Calculates the defense power from the defender Pokemon
     * Critical Hits ignore any defense stage boosts.
     * 
     * @param defStat defense stat 
     * @param crit if the move was a critical hit
     */
    private static double calculateDefense(Stat defStat, boolean crit) {
        return crit && defStat.stage() > 0 ? defStat.base() : defStat.power();
    }

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
        ? (m.isType(GameType.FIRE)) ? 1.5 : (m.isType(GameType.WATER)) ? 0.5 : 1.0
        :  (BattleField.currentWeather == Weather.RAIN) 
            ? (m.isType(GameType.WATER)) ? 1.5 : (m.isType(GameType.FIRE)) ? 0.5 : 1.0
            : 1.0
        ;
    }

    /**
     * Additional Multipliers for physical moves.
     * 
     * 1) Burned Pokemon deal 50% less damage
     */
    private static double physicalMoveBonus(Pokemon p) {
        return p.hasPrimaryCondition(StatusCondition.BURN) ? 0.5 : 1.0;
    }

    /**
     * Additional Multipliers for special moves
     * NONE for right now
     */
    private static double specialMoveBonus(Pokemon p) {
        return 1.0;
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
    private static int calculateDamage(
        Pokemon attacker, 
        Pokemon defender, 
        Move move, 
        double effectiveness, 
        boolean isCritical
        ) {   

        double stab = sameTypeAttackBonus(attacker, move);
        double crit = isCritical ? 1.5 : 1.0;
        double random = random();
        double weather = weatherBonus(move);

        double attack = move.category().equals(Move.SPECIAL) 
        ? calculateAttack(attacker.specialAttack(), isCritical) 
        : calculateAttack(attacker.attack(), isCritical);

        double defense = move.category().equals(Move.SPECIAL) 
        ? calculateDefense(defender.specialDefense(), isCritical) 
        : calculateDefense(defender.defense(), isCritical);

        double additional = move.category().equals(Move.SPECIAL) ? specialMoveBonus(attacker) : physicalMoveBonus(attacker);
        
        return (int) (((((2 * attacker.level()) / 5.0 + 2) * move.power() * (attack / defense)) / 50.0 + 2) 
        * stab * crit * effectiveness * random * weather * additional);      
    }


    // Pokemon takes damage based on some percent of the damage dealt
    private static void recoilDamage(Pokemon p, double recoil) {
        if (p.damageDealt() == 0) return;

        int damage = (int) (0.01 * recoil * p.damageDealt()); 
        BattleLog.add(String.format("%s took %d from the recoil!", p, damage));
        p.takeDamage(damage);
    }


    /**
     * Deals damage to an opponent.
     * @param attacker Attacking Pokemon
     * @param defender Defending Pokemon
     * @param move Move used by the attacker
     */
    public static void dealDamage(Pokemon attacker, Pokemon defender, Move move) {  
        /*
         * Calculates effectiveness of the move on the defender
         * No damage is dealt if defending is immune to the attack
         */
        double effectiveness = typeEffectiveness(move.moveType(), defender.pokemonType());

        if (effectiveness == 0) 
            throw new MoveInterruptedException(String.format("But it doesn't affect %s...", defender)); 
        
        moveHits(attacker, defender, move); // Deals no damage if move misses
        
        boolean isCritical = criticalHit(move.critRate()); // Rolls for a critical hit
        int damage = calculateDamage(attacker, defender, move, effectiveness, isCritical); // Calculates damage
      
        BattleLog.add(String.format("%s took %d damage!", defender, damage));
        BattleLog.add(isSuperEffective(effectiveness));
        BattleLog.add((isCritical) ? "Critical hit!" : "");

        attacker.addDealtDamage(damage); 
        defender.takeDamage(damage);
    }

    // Deals damage, attacking Pokemon receives a percentage of the damage dealt
    public static void dealDamageRecoil(Pokemon attacker, Pokemon defender, Move move, double percent) {
        try {
            dealDamage(attacker, defender, move);
        } catch (MoveInterruptedException | PokemonFaintedException e) {
            BattleLog.add(e.getMessage());
        }
        recoilDamage(attacker, percent);
    }

    /**
     * Confusion damage is calculated as if it were a typeless physical move with a power of 40
     * It cannot score a critical hit, and does not receive STAB
     */
    public static void takeConfusionDamage(Pokemon p) {
        int damage = (int) (((((2 * p.level()) / 5.0 + 2) * 40 * (p.attack().power() / (double) p.defense().power())) / 50.0 + 2)); 
        BattleLog.add(String.format("%s took %d damage from their own confusion!", p, damage));
        p.takeDamage(damage);
    }

// Restore HP Functions

    // Restores a percentage of a Pokemon's maximum HP
    public static void restoreHp(Pokemon p, double percent) {
        if (p.hp().atFullHP()) throw new MoveInterruptedException(String.format("But %s is already at full health!", p));

        int heal = (int) (0.01 * percent * p.hp().max());
        BattleLog.add(String.format("%s restored %d HP!", p, heal));
        p.healDamage(heal);
    }

// Charge Functions

    /*
     * Charges move first round, then unleashes it on the second
     * Can be interrupted by status effects
     */
    public static void chargeMove(Pokemon p1, Pokemon p2, Move move ) {
        if (!p1.conditions().charged()) {
            move.pp().increment(); // Done bc pp is decremented every move call
            p1.conditions().setCharge(true);       
            BattleLog.add(String.format("%s begins charging!", p1));
        }
        else {
            p1.conditions().setCharge(false);
            dealDamage(p1, p2, move);
        }
    }

// Weather Change Functions

    // Changes current weather
    public static void changeWeather(int c) {
        if (BattleField.currentWeather == c) throw new MoveInterruptedException(Move.FAILED);
        Weather.change(c);
    }

// Stat Change Functions

    /**
     * Changes one of a Pokemon's stats.
     * @param p Pokemon whose stat will be changed
     * @param change number of stages stat will increase/decrease
     * @param id Stat ID
     * @param chance The stat change success rate
     */
    private static void changeStat(Pokemon p, int change, int id, double chance) {
        if (new Random().nextDouble() > chance * 0.01) return;

        Stat s = p.stats()[id];
        if (s.isAtHighestOrLowestStage(change)) {
            BattleLog.add(String.format("But %s's %s won't go any %s!", p, s, (change > 0) ? "higher" : "lower"));
            return;
        }
        p.stats()[id].changeStage(change);
        BattleLog.add(String.format("%s's %s %s%s!", p, s, (change > 0) ? "rose" : "fell", Stat.sizeOfChange(change)));
    }

    // Changes a Pokemon's Attack Stat 
    public static void attackStat(Pokemon p, int change) {
        changeStat(p, change, Stat.ATTACK, 100);
    }

    public static void attackStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.ATTACK, chance);
    }

    // Changes a Pokemon's Defense Stat 
    public static void defenseStat(Pokemon p, int change) {
        changeStat(p, change, Stat.DEFENSE, 100);
    }

    public static void defenseStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.DEFENSE, chance);
    }

    // Changes a Pokemon's Special-Attack Stat
    public static void spAttackStat(Pokemon p, int change) {
        changeStat(p, change, Stat.SPECIAL_ATTACK, 100);
    }
    
    public static void spAttackStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.SPECIAL_ATTACK, chance);
    }

    // Changes a Pokemon's Special-Defense Stat 
    public static void spDefenseStat(Pokemon p, int change) {
        changeStat(p, change, Stat.SPECIAL_DEFENSE, 100);
    }

    public static void spDefenseStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.SPECIAL_DEFENSE, chance);
    }

    // Changes a Pokemon's Speed Stat
    public static void speedStat(Pokemon p, int change) {
        changeStat(p, change, Stat.SPEED, 100);
    }

    public static void speedStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.SPEED, 100);
    }

    // Changes a Pokemon's Accuracy Stat
    public static void accuracyStat(Pokemon p, int change) {
        changeStat(p, change, Stat.ACCURACY, 100);
    }

    public static void accuracyStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.ACCURACY, chance);
    }

    // Changes a Pokemon's Evasion Stat
    public static void evasionStat(Pokemon p, int change) {
        changeStat(p, change, Stat.EVASION, 100);
    }

    public static void evasionStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.EVASION, chance);
    }

// Status Conditions Functions

    /**
     * Applies a status condition to a Pokemon
     * @param p Pokemon receiving the effect
     * @param chance Success rate of effect
     * @param condition which condition is applied
     * @param message application message
     */
    private static void applyCondition(Pokemon p, double chance, StatusCondition condition, String message) {
        if (!RandomValues.chance(chance)) return;

        p.conditions().setPrimaryCondition(condition);
        BattleLog.add(message);
    }

    private static void applyVolatileCondition(Pokemon p, double chance, StatusCondition condition, String message) {
        if (!RandomValues.chance(chance)) return;

        p.conditions().add(condition);
        BattleLog.add(message);
    }

    private static boolean typeImmunity(Pokemon p, int id) {
        return switch (id) {
            case StatusCondition.BURN -> p.isType(GameType.FIRE);
            case StatusCondition.FREEZE -> p.isType(GameType.ICE);
            case StatusCondition.PARALYSIS -> p.isType(GameType.ELECTRIC);
            case StatusCondition.POISON -> p.isType(GameType.POISON) || p.isType(GameType.STEEL);
            default -> false;
        };
    }

    /**
     * Checks if a status condition can be applied to a Pokemon
     * Cannot apply condition if one of the follow:
     * 
     * 1) The Pokemon's typing has an immunity
     * 2) The Pokemon already has the condition
     * @param id
     */
    private static void canBeApplied(Pokemon p, int id) {
        if (typeImmunity(p, id)) throw new MoveInterruptedException(String.format("But it doesn't affect %s...", p));
        
        if (p.hasPrimaryCondition()) throw new MoveInterruptedException(String.format(p.hasPrimaryCondition(id)  
        ? String.format("But %s is already %s!", p, StatusCondition.failMessage(id)) 
        : Move.FAILED));
    }

    // Applies Burn Condition
    private static void applyBurn(Pokemon p, double chance) { 
        applyCondition(p, chance, StatusAction.burn(), p + " was burned!");
    }

    // Applies Freeze Condition
    private static void applyFreeze(Pokemon p, double chance) {
        applyCondition(p, chance, StatusAction.freeze(), p + " froze!");
        p.conditions().setCharge(false);
    }

    // Applies Paralysis Condition
    private static void applyParalysis(Pokemon p, double chance) {
        applyCondition(p, chance, StatusAction.paralysis(), p + " was paralyzed!");
    }

    // Applies Poison Condition
    private static void applyPoison(Pokemon p, double chance) {
        applyCondition(p, chance, StatusAction.poison(), p + " was poisoned!");
    }

    // Applies Sleep Condition
    private static void applySleep(Pokemon p) {
        applyCondition(p, 100, StatusAction.sleep(), p + " fell asleep!");
    }

    // Applies Confusion Condition
    private static void applyConfusion(Pokemon p, double chance) {
        applyVolatileCondition(p, chance, StatusAction.confusion(), p + " became confused!");
    }

    // Check if a condition can be applied. Displays a message if it cannot
    public static void canApplyEffect(Pokemon p, int statusId) {
        canBeApplied(p, statusId);
        statusEffect(p, statusId, 100);
    }

    // Checks if a non-volatile condition can be applied. 
    public static void statusEffect(Pokemon p, int statusId, double chance) {
        if (p.hasPrimaryCondition()) throw new MoveInterruptedException();

        switch (statusId) {
            case StatusCondition.BURN -> applyBurn(p, chance);  
            case StatusCondition.FREEZE -> applyFreeze(p, chance);  
            case StatusCondition.PARALYSIS -> applyParalysis(p, chance);   
            case StatusCondition.POISON -> applyPoison(p, chance); 
            case StatusCondition.SLEEP -> applySleep(p);
            default -> throw new IllegalArgumentException("Invalid condition id");
        }
    }

    public static void volatileStatusEffect(Pokemon p, int statusId, double chance) {
        if (p.conditions().hasCondition(statusId)) throw new MoveInterruptedException();

        switch (statusId) {
            case StatusCondition.CONFUSION -> applyConfusion(p, chance);
            default -> throw new IllegalArgumentException("Invalid condition id");
        }
    }

    // Pokemon flinches if it hasn't moved yet
    public static void applyFlinch(Pokemon p, double chance) {
        if (p.conditions().hasMoved() || new Random().nextDouble() > chance * 0.01) return;
        p.conditions().setFlinched(true);
    }
}
