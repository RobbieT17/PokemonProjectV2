package project.move;

import java.util.Random;

import project.battle.BattleField;
import project.battle.BattleLog;
import project.battle.Weather;
import project.event.EventData;
import project.event.GameEvent;
import project.exceptions.MoveInterruptedException;
import project.pokemon.Pokemon;
import project.pokemon.PokemonStat;
import project.stats.StatusCondition;
import project.stats.Type;
import project.utility.Protection;

@FunctionalInterface
public interface MoveAction {
// Public Variables
    public static final MoveAction DEFAULT_ACTION = e -> MoveAction.dealDamage(e);

// Object Method
    void act(EventData e);

// Protection Functions
private static void defenderProtects(Pokemon p) {
    if (!p.conditions().protect().active()) return;
        
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

// Damaging Functions

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
            // if (immunityExceptionFound(type, p)) break;
            if (t.equals(type)) effect = 0;
        } 
        
        return effect;
    }

    /*
    * Calculates effectiveness of the move on the defender
    * No damage is dealt if defending is immune to the attack
    */
    private static void moveEffectiveness(EventData data) {
        Pokemon p = data.attackTarget;
        double effectiveness = typeEffectiveness(data.moveUsed.moveType(), p);

        data.moveEffectiveness = effectiveness;

        data.notifyEvent(GameEvent.MOVE_EFFECTIVENESS);

        if (effectiveness == 0) throw new MoveInterruptedException(Move.noEffectOn(p));     
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
        criticalHit(data.moveUsed.critRate()); // Rolls for a critical hit
        data.notifyEvent(GameEvent.DAMAGE_MULTIPLIER);
        
        Pokemon attacker = data.user;
        Pokemon defender = data.attackTarget;
        Move move = data.moveUsed;
        boolean isCritical = data.criticalHit;
        double effectiveness = data.moveEffectiveness;  

        double stab = sameTypeAttackBonus(attacker, move);
        double crit = isCritical ? 1.5 : 1.0;
        double random = random();
        double weather = weatherBonus(move);
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

    private static void damageTaken(EventData data, boolean multiHit) {
        Pokemon attacker = data.user;
        Pokemon defender = data.attackTarget;

        int damage = calculateDamage(data);
        data.damageDealt += damage;
    
        BattleLog.add("%s took %d damage!", defender, damage);
        BattleLog.add(!multiHit ? isSuperEffective(data.moveEffectiveness) : "");
        BattleLog.add(data.criticalHit ? "Critical hit!" : "");

        attacker.addDealtDamage(damage);
        defender.addDamageReceived(damage);
        defender.takeDamage(damage);    

        if (data.moveUsed.makesContact()) data.notifyEvent(GameEvent.MOVE_MAKES_CONTACT);
    }

    // Pokemon takes damage based on some percent of the damage dealt
    private static void recoilDamage(EventData data) {
        Pokemon p = data.user;
        if (p.damageDealt() == 0) return;

        int damage = (int) (0.01 * data.recoilPercent * p.damageDealt()); 
        BattleLog.add("%s took %d damage from the recoil!", p, damage);
        p.addDamageReceived(damage);
        p.takeDamage(damage);
    }

    private static void drainHP(EventData data) {
        Pokemon p = data.user;
        if (p.damageDealt() == 0) return;

        int heal = (int) (0.01 * data.drainPercent * p.damageDealt()); 
        BattleLog.add("%s restored %d HP!", p, heal);
        p.restoreHP(heal);
    }

    // Deals damage to the target
    public static void dealDamage(EventData data) {       
        moveEffectiveness(data); 
        moveHits(data); 
        damageTaken(data, false);
    }

    // Deals multiple hits of damage
    public static void multiHit(EventData data) {
        moveEffectiveness(data);
        moveHits(data);

        Pokemon defender = data.attackTarget;
        data.hitCount = randomHits();

        for (int i = 0; i < data.hitCount; i++) {
            damageTaken(data, true);
            if (defender.conditions().fainted()) {
                data.hitCount = i;
                break;
            }
        }
        BattleLog.add("It hit %d times!", data.hitCount);
        BattleLog.add(isSuperEffective(data.moveEffectiveness));
    }

    // Deals damage, attacking Pokemon receives a percentage of the damage dealt
    public static void dealDamageRecoil(EventData data, double percent) {
        data.recoilPercent = percent;
        dealDamage(data);
        recoilDamage(data);
    }

    // Deals damage, attacking Pokemon heals from percentage of the damage dealt
    public static void dealDamageDrain(EventData data, double percent) {
        data.drainPercent = percent;
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
        p.addDamageReceived(damage);
        p.takeDamage(damage); 
    }

    

// Restore HP Functions

    // Restores a percentage of a Pokemon's maximum HP
    public static void restoreHp(EventData data, double percent) {
        Pokemon p = data.attackTarget;
        data.healPercent = percent;
        if (p.hp().atFullHP()) throw new MoveInterruptedException("But %s is already at full health!", p);
        p.restoreHpPercentMaxHP(percent, "");
    }

// Bracing Functions
    public static void pokemonProtects(EventData data, Protection b, String success) {
        Pokemon p = data.user;
        data.protectionType = b;
        data.message = success;

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
        Pokemon attacker = data.user;
        if (!attacker.conditions().hasKey(StatusCondition.CHARGE_MOVE)) {
            attacker.conditions().addCondition(StatusCondition.chargeMove(attacker, data.moveUsed));    
            BattleLog.add("%s begins charging!", attacker);
            return;
        }
        
        attacker.conditions().removeCondition(StatusCondition.CHARGE_MOVE);
        dealDamage(data); 
    }


    public static void focusMove(EventData data) {
        BattleLog.add("<NOT IMPLEMENTED>");
    }

    public static void rechargeMove(EventData data) {
        data.user.conditions().setRecharge(true);
    }

    /**
     * Forces Pokemon to use the same move for 2-3 turns
     * Rampage is disrupt if the move misses or the Pokemon
     * cannot act due to a status condition
     */
    public static void rampageMove(EventData data) {
        Pokemon attacker = data.user;
        // Starts rampage
        if (!attacker.conditions().hasKey(StatusCondition.RAMPAGE_ID)) {
            attacker.conditions().addCondition(StatusCondition.rampage(attacker, data.moveUsed));
        }
       
        dealDamage(data);       
    }

// Weather Change Functions

    // Changes current weather
    public static void changeWeather(EventData data, int c) {
        data.weatherChange = c;
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
    private static void changeEachStat(EventData data, int[] stats) {
        Pokemon p = data.effectTarget;
        data.statChanges = stats;

        data.notifyEvent(GameEvent.STAT_CHANGE);
        
        for (int i = 0; i < stats.length; i++) {
            int change = stats[i];
            if (change == 0) continue;
        
            PokemonStat s = p.stats()[i];
            if (s.isAtHighestOrLowestStage(change)) {
                BattleLog.add("But %s's %s won't go any %s!", p, s, (change > 0) ? "higher" : "lower");
                data.statFailed = true;
                continue;
            }
            p.stats()[i].changeStage(change);
            BattleLog.add("%s's %s %s%s!", p, s, (change > 0) ? "rose" : "fell", PokemonStat.sizeOfChange(change));
        }  
    }

    public static void changeStats(EventData data, int[] stats, double chance) {
        data.statProb = chance;
        if (data.user.conditions().fainted() || new Random().nextDouble() > chance * 0.01) {
            data.statFailed = true;
            return;
        }
        changeEachStat(data, stats);
    }

    public static void changeStats(EventData data, int[] stats) {
        changeStats(data, stats, 100);
    }

    // Resets all stat changes back to neutral
    public static void resetStats(EventData data, Pokemon p) {
        for (PokemonStat s : p.stats()) s.setStage(0);
        BattleLog.add("%s stat changes were cleared...", p);
    }

// Status Conditions Functions

    // Applies Burn Condition
    private static void applyBurn(Pokemon p) { 
        p.conditions().setPrimaryCondition(StatusCondition.burn(p));
    }

    // Applies Freeze Condition
    private static void applyFreeze(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.freeze(p));
    }

    // Applies Infect Condition
    private static void applyInfect(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.infect(p));
    }

    // Applies Paralysis Condition
    private static void applyParalysis(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.paralysis(p));
    }

    // Applies Poison Condition
    private static void applyPoison(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.poisoned(p));
    }

    // Applies Badly Poison Condition
    private static void applyBadlyPoison(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.badlyPoisoned(p));
    }

    // Applies Sleep Condition
    private static void applySleep(Pokemon p) {
        p.conditions().setPrimaryCondition(StatusCondition.sleep(p));
    }

    private static void flyState(EventData data) {
        Pokemon p = data.user;
        p.conditions().addCondition(StatusCondition.fly(p, data.moveUsed));
    }

    private static void digState(EventData data) {
        Pokemon p = data.user;
        p.conditions().addCondition(StatusCondition.dig(p, data.moveUsed));
    }

    private static void diveState(EventData data) {
        Pokemon p = data.user;
        p.conditions().addCondition(StatusCondition.dive(p, data.moveUsed));
    }

    private static void applyFlinch(Pokemon p) {
        p.conditions().addCondition(StatusCondition.flinch(p));
    }

    // Applies Confusion Condition
    private static void applyBound(Pokemon p) {
        p.conditions().addCondition(StatusCondition.bound(p));
    }

    private static void applyConfusion(Pokemon p) {
        p.conditions().addCondition(StatusCondition.confusion(p));
    }

    private static void applySeeded(Pokemon p, Pokemon r) {
        p.conditions().addCondition(StatusCondition.seeded(p, r));
    }

    

    private static boolean cannotApplyCondition(Pokemon p, String id) {
        return switch (id) {
            case StatusCondition.BURN_ID -> p.isType(Type.FIRE) || p.conditions().hasPrimary();
            case StatusCondition.FREEZE_ID -> p.isType(Type.ICE) || p.conditions().hasPrimary();
            case StatusCondition.INFECT_ID -> p.conditions().hasPrimary();
            case StatusCondition.PARALYSIS_ID -> p.isType(Type.ELECTRIC) || p.conditions().hasPrimary();
            case 
                StatusCondition.POISON_ID, 
                StatusCondition.BAD_POISON_ID -> p.isType(Type.POISON) || p.isType(Type.STEEL) || p.conditions().hasPrimary();
            case StatusCondition.SLEEP_ID -> p.isType(Type.DIGITAL) || p.conditions().hasPrimary();
            case StatusCondition.FLINCH_ID -> p.conditions().hasMoved();
            case 
                StatusCondition.BOUND_ID,
                StatusCondition.CONFUSION_ID -> p.conditions().hasKey(id);
            case StatusCondition.SEEDED_ID -> p.isType(Type.GRASS) || p.conditions().hasKey(id);
            default -> false;
        };
    }

    public static void applyCondition(EventData data, String id, double chance) {
        Pokemon p = data.effectTarget;
        data.statusChange = id;
        data.statusProb = chance;
        
        if (cannotApplyCondition(p, id) || p.conditions().fainted()) {
            data.statusFailed = true;
            data.message = Move.FAILED;
            return;
        }

        if (new Random().nextDouble() > chance * 0.01) {
            data.statusFailed = true;
            return;
        }
            
        switch (id) {
            case StatusCondition.BURN_ID -> applyBurn(p);
            case StatusCondition.FREEZE_ID -> applyFreeze(p);
            case StatusCondition.INFECT_ID -> applyInfect(p);
            case StatusCondition.PARALYSIS_ID -> applyParalysis(p);
            case StatusCondition.POISON_ID -> applyPoison(p);
            case StatusCondition.BAD_POISON_ID -> applyBadlyPoison(p);
            case StatusCondition.SLEEP_ID -> applySleep(p);
            case StatusCondition.FLINCH_ID -> applyFlinch(p);
            case StatusCondition.BOUND_ID -> applyBound(p);
            case StatusCondition.CONFUSION_ID -> applyConfusion(p);
            case StatusCondition.SEEDED_ID -> applySeeded(data.attackTarget, data.user);
            case StatusCondition.FLY_ID -> flyState(data);
            case StatusCondition.DIG_ID -> digState(data);
            case StatusCondition.DIVE_ID -> diveState(data);
            default -> throw new IllegalArgumentException(StatusCondition.ID_ERR);
        }  
    }

    public static void applyCondition(EventData data, String id) {
        applyCondition(data, id, 100);
    }

    // Semi-Immune State Function

    /*
     * Pokemon enters a semi-invulnerable state the first turn
     * Pokemon leaves the state and attacks on the second turn
     */
    public static void enterImmuneState(EventData data, String state) {
        Pokemon attacker = data.user;
        data.immuneStateChange = state;

        // Enters state
        if (!attacker.conditions().inImmuneState()) {
            applyCondition(data, state, 100);
            return;
        }

        attacker.conditions().removeCondition(data.immuneStateChange);
        dealDamage(data);
    }

    /*
     * Pokemon is knocked out of their semi-invulnerable state, interrupted
     */
    public static void leaveImmuneState(EventData data, String state, String message) {
        Pokemon p = data.attackTarget;
        data.immuneStateChange = StatusCondition.NO_INVUL_ID;
    
        if (p.conditions().fainted() || !p.conditions().hasKey(state)) return;
        p.conditions().removeCondition(state);
        p.conditions().setInterrupted(true);
        p.resetMove();
        BattleLog.add(message);
    }

    public static void displayFailMessage(EventData data) {
        BattleLog.add(data.message);
    }

}
