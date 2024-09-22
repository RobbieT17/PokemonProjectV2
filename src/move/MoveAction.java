package move;

import battle.BattleField;
import battle.BattleLog;
import battle.Weather;
import event.EventData;
import event.GameEvent;
import exceptions.MoveInterruptedException;
import java.util.Random;
import pokemon.Pokemon;
import stats.Stat;
import stats.StatusAction;
import stats.StatusCondition;
import stats.Type;
import utility.Protection;
import utility.RandomValues;

@FunctionalInterface
public interface MoveAction {
// Public Variables
    public static final MoveAction DEFAULT_ACTION = e -> MoveAction.dealDamage(e);

// Object Method
    void act(EventData e);

// Accuracy Function

    /* 
     * Some moves can hit opponents through their 
     * semi-invulnerable state.
     * 
     * Exceptions
     * FLY: Smack Down, Hurricane
     * DIG: Earthquake
     * DIVE: Surf, Whirlpool
     */
    public static void checkInvulnerabilityExceptions(Pokemon p, Move m) {
        if (!p.conditions().inImmuneState()) return;

        boolean exceptionFound = false;
        switch (p.conditions().immuneState()) {
            case StatusCondition.FLY -> exceptionFound = m.moveID() == 479 ||  m.moveID() == 542; // Smack Down, Hurricane   
            case StatusCondition.DIG -> exceptionFound = m.moveID() == 89; // Earthquake
            case StatusCondition.DIVE -> exceptionFound = m.moveID() == 57 || m.moveID() == 250; // Surf, Whirlpool    
        }

        if (!exceptionFound) throw new MoveInterruptedException("But %s is out of sight!", p);
    }

    public static void moveHits(EventData data) {
        Pokemon attacker = data.user();
        Pokemon defender = data.target();
        Move move = data.moveUsed();

        checkInvulnerabilityExceptions(defender, move);
        defenderProtects(defender);

        int accuracy = move.accuracy();

        if (accuracy == Move.ALWAYS_HITS || defender.conditions().immobilized()) {
            data.setMoveHits(true);
            return;
        }
		
        double modifiedAccuracy = 0.01 * accuracy
        * ((double) attacker.accuracy().power() / (double) defender.evasion().power());

        if (new Random().nextDouble() > modifiedAccuracy) 
            throw new MoveInterruptedException("But %s avoided the attack!", defender); 
        
        data.setMoveHits(true);
    } 

// Protection Functions
    public static void defenderProtects(Pokemon p) {
        if (!p.conditions().protect().active()) return;
            
        p.conditions().protect().setActive(false);
        throw new MoveInterruptedException("But %s protected itself!", p);
    }

    public static void defenderTakesHit(Pokemon p, int damage) {
        if (p.conditions().endured().active()) {
            p.takeDamageEndure(damage);
            p.conditions().endured().setActive(false);
        }
        else p.takeDamage(damage);     
    }

// Damaging Functions

    // Grounded Pokemon are vulnerable to Ground-Type moves
    private static boolean immunityExceptionFound(String type, Pokemon p) {
        return p.conditions().grounded() && type.equals(Type.GROUND);
    }

    /**
     * Calculates the effectiveness of a move on a Pokemon
     * @param type the move's type
     * @param p the pokemon's types
     */
    private static double typeEffectiveness(String type, Pokemon p){
        double effect = 1.0; // Default type effectiveness

        for (String t : p.pokemonType().typeResistances()) // Pokemon resists type, halves damage
            if (t.equals(type)) effect *= 0.5;
        
        for (String t : p.pokemonType().typeWeaknesses()) // Pokemon weak to type, doubles damage
            if (t.equals(type)) effect *= 2;

        for (String t : p.pokemonType().typeImmunities()) { // Pokemon immune to type, nullifies damage
            if (immunityExceptionFound(type, p)) break;
            if (t.equals(type)) effect = 0;
        } 
        
        return effect;
    }

    /*
    * Calculates effectiveness of the move on the defender
    * No damage is dealt if defending is immune to the attack
    */
    private static double moveEffectiveness(EventData data) {
        GameEvent.onMoveEffectiveness.update(data);

        Pokemon p = data.target();
        double effectiveness = typeEffectiveness(data.moveUsed().moveType(), p);
        if (effectiveness == 0) throw new MoveInterruptedException("But it doesn't affect %s...", p);     
        return effectiveness;
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
        ? (m.isType(Type.FIRE)) ? 1.5 : (m.isType(Type.WATER)) ? 0.5 : 1.0
        :  (BattleField.currentWeather == Weather.RAIN) 
            ? (m.isType(Type.WATER)) ? 1.5 : (m.isType(Type.FIRE)) ? 0.5 : 1.0
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
     * Number of times a multi-hit damages the opponent
     * 
     * 2 turns: 35% chance
     * 3 turns: 35% chance
     * 4 turns: 15% chance
     * 5 turns: 15% chance
     * 
     * @return # of hits
     */
    private static int randomHits() {
        double value = new Random().nextDouble();
        return value <= 0.35 
        ? 2
        : value <= 0.7
            ? 3
            : value <= 0.85
                ? 4
                : 5;
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
    private static int calculateDamage(EventData data) { 
        GameEvent.onDamageMultiplier.update(data);

        Pokemon attacker = data.user();
        Pokemon defender = data.target();
        Move move = data.moveUsed();
        boolean isCritical = data.criticalHit();
        double effectiveness = data.moveEffectiveness();  

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
    private static void recoilDamage(EventData data) {
        Pokemon p = data.user();
        if (p.damageDealt() == 0) return;

        int damage = (int) (0.01 * data.percent() * p.damageDealt()); 
        BattleLog.add("%s took %d damage from the recoil!", p, damage);
        p.takeDamage(damage);
    }

    private static void drainHP(EventData data) {
        Pokemon p = data.user();
        if (p.damageDealt() == 0) return;

        int heal = (int) (0.01 * data.percent() * p.damageDealt()); 
        BattleLog.add("%s restored %d HP!", p, heal);
        p.healDamage(heal);
    }

    // Pokemon takes damage from a multi-hit move
    private static void dealMultiDamage(EventData data) {
        Pokemon attacker = data.user();
        Pokemon defender = data.target();

        boolean isCritical = criticalHit(data.moveUsed().critRate());
        int damage = calculateDamage(data);

        BattleLog.add("%s took %d damage!", defender, damage);
        BattleLog.add(isCritical ? "Critical hit!" : "");

        attacker.addDealtDamage(damage);
        defender.addDamageReceived(damage);

        if (defender.conditions().endured().active()) defenderTakesHit(defender, damage);
        else defender.takeDamage(damage);
    }
    

    /**
     * Deals damage to an opponent.
     * @param attacker Attacking Pokemon
     * @param defender Defending Pokemon
     * @param move Move used by the attacker
     */
    public static void dealDamage(EventData data) {  
        Pokemon attacker = data.user();
        Pokemon defender = data.target();
        
        data.setMoveEffectiveness(moveEffectiveness(data)); // Returns if effect is 0  
        data.setCriticalHit(criticalHit(data.moveUsed().critRate())); // Rolls for a critical hit
        
        int damage = calculateDamage(data);
        data.addDamage(damage);
        
 
        BattleLog.add("%s took %d damage!", defender, damage);
        BattleLog.add(isSuperEffective(data.moveEffectiveness()));
        BattleLog.add(data.criticalHit() ? "Critical hit!" : "");

        attacker.addDealtDamage(damage); 
        defender.addDamageReceived(damage);

        if (defender.conditions().endured().active()) defenderTakesHit(defender, damage);
        else defender.takeDamage(damage);

        // Defender loses focus
        if (defender.conditions().focused()) defender.conditions().setInterrupted(true);
    }

    // Deals multiple hits of damage
    public static void multiHit(EventData data) {
        data.setMoveEffectiveness(moveEffectiveness(data));
        Pokemon defender = data.target();

        moveHits(data);

        int hits = randomHits();
        for (int i = 0; i < hits; i++) {
            dealMultiDamage(data);
            if (defender.conditions().fainted()) {
                hits = i + 1;
                break;
            }
        }
        BattleLog.add("It hit %d times!", hits);
        BattleLog.add(isSuperEffective(data.moveEffectiveness()));

        if (defender.conditions().focused()) defender.conditions().setInterrupted(true);
    }

    // Deals damage, attacking Pokemon receives a percentage of the damage dealt
    public static void dealDamageRecoil(EventData data, double percent) {
        data.setPercent(percent);
        dealDamage(data);
        recoilDamage(data);
    }

    // Deals damage, attacking Pokemon heals from percentage of the damage dealt
    public static void dealDamageDrain(EventData data, double percent) {
        data.setPercent(percent);
        dealDamage(data);
        drainHP(data);
    }

    /**
     * Confusion damage is calculated as if it were a typeless physical move with a power of 40
     * It cannot score a critical hit, and does not receive STAB
     */
    public static void takeConfusionDamage(Pokemon p) {
        int damage = (int) (((((2 * p.level()) / 5.0 + 2) * 40 * (p.attack().power() / (double) p.defense().power())) / 50.0 + 2)); 
        BattleLog.add("%s took %d damage from their own confusion!", p, damage);
        p.takeDamage(damage); 
    }

    

// Restore HP Functions

    // Restores a percentage of a Pokemon's maximum HP
    public static void restoreHp(EventData data, boolean self, double percent) {
        Pokemon p = self ? data.user() : data.target();
        if (p.hp().atFullHP()) throw new MoveInterruptedException("But %s is already at full health!", p);

        int heal = (int) (0.01 * percent * p.hp().max());
        BattleLog.add("%s restored %d HP!", p, heal);
        p.healDamage(heal);
    }

// Bracing Functions
    public static void pokemonProtects(EventData data, Protection b, String success) {
        Pokemon p = data.user();
        data.setProtect(b);
        data.setMessage(success);

        if (p.moveSelected().equals(p.lastMove())) b.set();
        else {
            b.reset();
            b.set();   
        }
        BattleLog.add(b.active() ? success : Move.FAILED);
    }

// Charge Functions

    /*
     * Charges move first round, then unleashes it on the second
     * Can be interrupted by status effects
     */
    public static void chargeMove(EventData data) {
        Pokemon attacker = data.user();
        if (!attacker.conditions().forcedMove()) {
            attacker.conditions().setForcedMove(true);       
            BattleLog.add("%s begins charging!", attacker);
        }
        else {
            attacker.conditions().setForcedMove(false);
            dealDamage(data);
        }
    }


    public static void focusMove(EventData data) {
        Pokemon attacker = data.user();
        if (!attacker.conditions().focused()){
            attacker.conditions().setFocused(true);
            attacker.conditions().setForcedMove(true);
            BattleLog.add("%s concentrates its energy!", attacker);
            return;
        }
        attacker.conditions().setFocused(false);
        attacker.conditions().setForcedMove(false);

        if (attacker.conditions().interrupted()) 
            throw new MoveInterruptedException("%s lost its focus and couldn't move!", attacker);

        dealDamage(data);
    }

    public static void rechargeMove(EventData data) {
        data.user().conditions().setRecharging(true);
    }

    /**
     * Forces Pokemon to use the same move for 2-3 turns
     * Rampage is disrupt if the move misses or the Pokemon
     * cannot act due to a status condition
     */
    public static void rampageMove(EventData data) {
        Pokemon attacker = data.user();
        // Starts rampage
        if (!attacker.conditions().onRampage()) 
            attacker.conditions().startRampage(RandomValues.generateInt(1, 2));
        
        // On rampage
        attacker.conditions().rampage().inc();
        if (attacker.conditions().rampage().terminated()) { // After rampage ends, user becomes confused
            dealDamage(data);
            attacker.conditions().stopRampage();
            attacker.conditions().setForcedMove(false);

            BattleLog.add("%s's rampage ended!", attacker);
            volatileStatusEffect(attacker, StatusCondition.CONFUSION, 100);
            return;
        }

        attacker.conditions().setForcedMove(true);
        dealDamage(data);
    }

// Weather Change Functions

    // Changes current weather
    public static void changeWeather(EventData data, int c) {
        data.setWeather(c);
        if (BattleField.currentWeather == c) throw new MoveInterruptedException(Move.FAILED);
        Weather.change(c);
    }

// Semi-Immune State Function

    /*
     * Pokemon enters a semi-invulnerable state the first turn
     * Pokemon leaves the state and attacks on the second turn
     */
    public static void enterImmuneState(EventData data, int state, String message) {
        Pokemon attacker = data.user();
        data.setImmuneState(state);
        data.setMessage(message);

        if (!attacker.conditions().inImmuneState()) {
            attacker.conditions().setImmuneState(state);
            attacker.conditions().setForcedMove(true);
            BattleLog.add(message);
            return;
        }

        attacker.conditions().setImmuneState(StatusCondition.NO_INVUL);
        attacker.conditions().setForcedMove(false);
        dealDamage(data);
    }

    /*
     * Pokemon is knocked out of their semi-invulnerable state, interrupted
     */
    public static void leaveImmuneState(EventData data, int state, String message) {
        Pokemon p = data.target();
        data.setImmuneState(state * -1); // Negative indicates removal
        data.setMessage(message);

        if (p.conditions().fainted() || !p.conditions().hasImmuneState(state)) return;
        p.conditions().setImmuneState(StatusCondition.NO_INVUL);
        p.conditions().setForcedMove(false);
        p.conditions().setInterrupted(true);
        p.resetMove();
        BattleLog.add(message);
    }


// Stat Change Functions

    /**
     * Changes one of a Pokemon's stats.
     * @param p Pokemon whose stat will be changed
     * @param change number of stages stat will increase/decrease
     * @param id Stat ID
     * @param chance The stat change success rate
     */
    private static void changeEachStat(EventData data, int[] stats) {
        Pokemon p = stats[7] == 0 ? data.user() : data.target();
        data.setStatChanges(stats);
        
        for (int i = 0; i < stats.length - 1; i++) {
            int change = stats[i];
            if (change == 0) continue;
        
            Stat s = p.stats()[i];
            if (s.isAtHighestOrLowestStage(change)) {
                BattleLog.add("But %s's %s won't go any %s!", p, s, (change > 0) ? "higher" : "lower");
                continue;
            }
            p.stats()[i].changeStage(change);
            BattleLog.add("%s's %s %s%s!", p, s, (change > 0) ? "rose" : "fell", Stat.sizeOfChange(change));
        }  
    }

    public static void changeStats(EventData data, int[] stats) {
        if (data.user().conditions().fainted()) return;
        changeEachStat(data, stats);
    }

    public static void changeStats(EventData data, int[] stats, double chance) {
        if (data.user().conditions().fainted() || new Random().nextDouble() > chance * 0.01) return;
        changeEachStat(data, stats);
    }

    // Resets all stat changes back to neutral
    public static void resetStats(EventData data, Pokemon p) {
        for (Stat s : p.stats()) s.setStage(0);
        BattleLog.add("%s stat changes were cleared...", p);
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
            case StatusCondition.BURN -> p.isType(Type.FIRE);
            case StatusCondition.FREEZE -> p.isType(Type.ICE);
            case StatusCondition.PARALYSIS -> p.isType(Type.ELECTRIC);
            case StatusCondition.POISON -> p.isType(Type.POISON) || p.isType(Type.STEEL);
            case StatusCondition.SEEDED -> p.isType(Type.GRASS);
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
        defenderProtects(p);
        if (typeImmunity(p, id)) throw new MoveInterruptedException("But it doesn't affect %s...", p);
        
        if (p.hasPrimaryCondition()) throw new MoveInterruptedException(p.hasPrimaryCondition(id)  
        ? String.format("But %s is already %s!", p, StatusCondition.failMessage(id)) 
        : Move.FAILED);
    }

    private static void canBeAppliedV(Pokemon p, int id) {
        defenderProtects(p);
        if (typeImmunity(p, id)) throw new MoveInterruptedException("But it doesn't affect %s...", p);
        if (p.hasCondition(id)) throw new MoveInterruptedException(Move.FAILED);
    }

    // Applies Burn Condition
    private static void applyBurn(Pokemon p, double chance) { 
        applyCondition(p, chance, StatusAction.burn(), p + " was burned!");
    }

    // Applies Freeze Condition
    private static void applyFreeze(Pokemon p, double chance) {
        applyCondition(p, chance, StatusAction.freeze(), p + " froze!");
        p.conditions().setForcedMove(false);
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
    private static void applyBound(Pokemon p, double chance) {
        applyVolatileCondition(p, chance, StatusAction.bound(), p + " was trapped in a bound!");
    }

    private static void applyConfusion(Pokemon p, double chance) {
        applyVolatileCondition(p, chance, StatusAction.confusion(), p + " became confused!");
    }


    // Check if a condition can be applied. Displays a message if it cannot
    public static void canApplyEffect(Pokemon p, int statusId) {
        canBeApplied(p, statusId);
        statusEffect(p, statusId, 100);
    }

    public static void canApplyVolatileEffect(Pokemon p, int statusId) {
        canBeAppliedV(p, statusId);
        volatileStatusEffect(p, statusId, 100);
    }

    // Checks if a non-volatile condition can be applied. 
    public static void statusEffect(Pokemon p, int statusId, double chance) {
        if (p.conditions().fainted() | p.hasPrimaryCondition() | typeImmunity(p, statusId)) return;

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
        // TODO: Code Duplication, unneeded condition check 
        if (p.conditions().fainted() | p.hasCondition(statusId) | typeImmunity(p, statusId)) return;
    
        switch (statusId) {
            case StatusCondition.BOUND -> applyBound(p, chance);
            case StatusCondition.CONFUSION -> applyConfusion(p, chance);
            default -> throw new IllegalArgumentException("Invalid condition id");
        }
    }

    // Pokemon flinches if it hasn't moved yet
    public static void applyFlinch(Pokemon p, double chance) {
        if (p.conditions().fainted() || p.conditions().hasMoved() || new Random().nextDouble() > chance * 0.01) return;
        p.conditions().setFlinched(true);
    }

    public static void applySeeded(Pokemon receiver, Pokemon seeded) {
        if (typeImmunity(seeded, StatusCondition.SEEDED)) throw new MoveInterruptedException(Move.FAILED);
        seeded.conditions().add(StatusAction.seeded(receiver));
        BattleLog.add("%s was grew sprouts!", seeded);
    }

    /*
     * Grounds a Pokemon, any Flying-Type Pokemon are vulnerable to Ground-Type moves
     */
    public static void groundedPokemon(Pokemon p) {
        if (p.conditions().fainted()) return;
        p.conditions().setGrounded(true);
        BattleLog.add("%s was grounded!", p);
    }

    // Pokemon removes status condition
    public static void removeStatusEffect(Pokemon p, int statusId) {
        if (!p.hasPrimaryCondition(statusId)) return;
        p.clearPrimaryCondition(statusId);
    }
}
