package move;
import battle.BattleField;
import battle.BattleLog;
import battle.MoveInterruptedException;
import battle.PokemonFaintedException;
import battle.Weather;
import java.util.Random;
import pokemon.Pokemon;
import pokemon.PokemonType;
import stats.GameType;
import stats.Stat;
import stats.StatusAction;
import stats.StatusCondition;

@FunctionalInterface
public interface MoveAction {
    // Public Variables
    public static final MoveAction DEFAULT_ACTION = (a, d, m) -> MoveAction.dealDamage(a, d, m);

    // Function
    void useMove(Pokemon attacker, Pokemon defender, Move move);

    // Accuracy Methods
    public static void moveHits(Pokemon attacker, Pokemon defender, Move move) {
        if (move.accuracy() == Move.INF || defender.immobilized()) return;
		
        double modifiedAccuracy = 0.01 * move.accuracy() * ((double) attacker.accuracy().power() / (double) defender.evasion().power());
        if (new Random().nextDouble() > modifiedAccuracy) throw new MoveInterruptedException(String.format("But %s avoided the attack!", defender));   
    } 

    // Damaging Methods
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
 
    private static String isSuperEffective(double effect) {
        return (effect > 1.0) 
        ? "It's super effective!" 
        : (effect < 1.0) ? "It's not very effective..." : "";
    }

    private static boolean criticalHit(double rate) {
		return new Random().nextDouble() <= rate;
    }

    private static double random() {
        return 0.85 + (1 - 0.85) * new Random().nextDouble();
    }

    private static double calculateAttack(Stat atkStat, boolean crit) {
        return crit && atkStat.stage() < 0 ? atkStat.base() : atkStat.power();
    }

    private static double calculateDefense(Stat defStat, boolean crit) {
        return crit && defStat.stage() > 0 ? defStat.base() : defStat.power();
    }

    private static double sameTypeAttackBonus(Pokemon p, Move m){
        return (m.isType(p.pokemonType())) ? 1.5 : 1.0;
    }

    private static double weatherBonus(Move m) {
        return (BattleField.currentWeather == Weather.SUNNY) 
        ? (m.isType(GameType.FIRE)) ? 1.5 : (m.isType(GameType.WATER)) ? 0.5 : 1.0
        :  (BattleField.currentWeather == Weather.RAIN) 
            ? (m.isType(GameType.WATER)) ? 1.5 : (m.isType(GameType.FIRE)) ? 0.5 : 1.0
            : 1.0
        ;
    }

    private static double physicalMoveBonus(Pokemon p) {
        return p.hasPrimaryCondition(StatusCondition.BURN) ? 0.5 : 1.0;
    }

    private static double specialMoveBonus(Pokemon p) {
        return 1.0;
    }

    private static int calculateDamage(Pokemon attacker, Pokemon defender, Move move, double effectiveness, boolean isCritical) {
        double stab = sameTypeAttackBonus(attacker, move);
        double crit = isCritical ? 1.5 : 1.0;
        double random = random();
        double weather = weatherBonus(move);

        double attack = move.category().equals(Move.SPECIAL) ? calculateAttack(attacker.specialAttack(), isCritical) : calculateAttack(attacker.attack(), isCritical);
        double defense = move.category().equals(Move.SPECIAL) ? calculateDefense(defender.specialDefense(), isCritical) : calculateDefense(defender.defense(), isCritical);

        double additional = move.category().equals(Move.SPECIAL) ? specialMoveBonus(attacker) : physicalMoveBonus(attacker);
        
        return (int) (((((2 * Pokemon.DEFAULT_LEVEL) / 5.0 + 2) * move.power() * (attack / defense)) / 50.0 + 2) 
        * stab * crit * effectiveness * random * weather * additional);      
    }


    private static void recoilDamage(Pokemon p, double recoil) {
        if (p.damageDealt() == 0) return;

        int damage = (int) (0.01 * recoil * p.damageDealt()); 
        BattleLog.add(String.format("%s took %d from the recoil!", p, damage));
        p.takeDamage(damage);
    }


    public static void dealDamage(Pokemon attacker, Pokemon defender, Move move) {
        double effectiveness = typeEffectiveness(move.moveType(), defender.pokemonType());
        
        if (effectiveness == 0) throw new MoveInterruptedException(String.format("But it doesn't affect %s...", defender)); 
        moveHits(attacker, defender, move);
        
        boolean isCritical = criticalHit(move.critRate());
        int damage = calculateDamage(attacker, defender, move, effectiveness, isCritical);
      
        BattleLog.add(String.format("%s took %d damage!", defender, damage));
        BattleLog.add(isSuperEffective(effectiveness));
        BattleLog.add((isCritical) ? "Critical hit!" : "");

        attacker.addDealtDamage(damage);
        defender.takeDamage(damage);
    }

    public static void dealDamageRecoil(Pokemon attacker, Pokemon defender, Move move, double percent) {
        try {
            dealDamage(attacker, defender, move);
        } catch (MoveInterruptedException | PokemonFaintedException e) {
            BattleLog.add(e.getMessage());
        }
        recoilDamage(attacker, percent);
    }

    // Restore HP Methods
    public static void restoreHp(Pokemon p, double percent) {
        int heal = (int) (0.01 * percent * p.hp().max());
        BattleLog.add(String.format("%s restored %d HP", p, heal));
        p.healDamage(heal);
    }

    // Charge Methods
    public static void chargeMove(Pokemon p1, Pokemon p2, Move move ) {
        if (!p1.charged()) {
            move.pp().increment(); // Done bc pp is decremented every move call
            p1.setCharge(true);       
            BattleLog.add(String.format("%s begins charging!", p1));
        }
        else {
            p1.setCharge(false);
            dealDamage(p1, p2, move);
        }
    }

    // Weather Changes
    public static void changeWeather(int c) {
        if (BattleField.currentWeather == c){
            BattleLog.add("But it failed!");
            return;
        }
        Weather.change(c);
    }

    // Stat Changes
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

    public static void attackStat(Pokemon p, int change) {
        changeStat(p, change, Stat.ATTACK, 100);
    }

    public static void attackStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.ATTACK, chance);
    }

    public static void defenseStat(Pokemon p, int change) {
        changeStat(p, change, Stat.DEFENSE, 100);
    }

    public static void defenseStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.DEFENSE, chance);
    }

    public static void spAttackStat(Pokemon p, int change) {
        changeStat(p, change, Stat.SPECIAL_ATTACK, 100);
    }
    
    public static void spAttackStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.SPECIAL_ATTACK, chance);
    }

    public static void spDefenseStat(Pokemon p, int change) {
        changeStat(p, change, Stat.SPECIAL_DEFENSE, 100);
    }

    public static void spDefenseStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.SPECIAL_DEFENSE, chance);
    }

    public static void speedStat(Pokemon p, int change) {
        changeStat(p, change, Stat.SPEED, 100);
    }

    public static void speedStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.SPEED, 100);
    }

    public static void accuracyStat(Pokemon p, int change) {
        changeStat(p, change, Stat.ACCURACY, 100);
    }

    public static void accuracyStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.ACCURACY, chance);
    }

    public static void evasionStat(Pokemon p, int change) {
        changeStat(p, change, Stat.EVASION, 100);
    }

    public static void evasionStat(Pokemon p, int change, double chance) {
        changeStat(p, change, Stat.EVASION, chance);
    }

    // Status Conditions 
    private static void applyCondition(Pokemon p, double chance, StatusCondition condition, String message) {
        if (new Random().nextDouble() > chance * 0.01) return;

        p.setPrimaryCondition(condition);
        BattleLog.add(message);
    }

    private static void canBeApplied(Pokemon p, int id) {
        if (p.hasPrimaryCondition()) throw new MoveInterruptedException(String.format(p.hasPrimaryCondition(id) ? String.format("But %s is already %s!", p, StatusCondition.failMessage(id)) : Move.FAILED));
    }

    private static void applyBurn(Pokemon p, double chance) {
        applyCondition(p, chance, StatusAction.burn(), p + " was burned!");
    }

    private static void applyFreeze(Pokemon p, double chance) {
        applyCondition(p, chance, StatusAction.freeze(), p + " froze!");
        p.setCharge(false);
    }

    private static void applyParalysis(Pokemon p, double chance) {
        applyCondition(p, chance, StatusAction.paralysis(), p + " was paralyzed!");
    }

    private static void applyPoison(Pokemon p, double chance) {
        applyCondition(p, chance, StatusAction.poison(), p + " was poisoned!");
    }

    private static void applySleep(Pokemon p, int turns) {
        applyCondition(p, 100, StatusAction.sleep(turns), p + " fell asleep!");
    }

    public static void canApplyEffect(Pokemon p, int statusId) {
        canBeApplied(p, statusId);
        statusEffect(p, statusId, 100);
    }

    public static void canApplyEffect(Pokemon p, int statusId, int duration) {
        canBeApplied(p, statusId);
        
        switch (statusId) {
            case StatusCondition.SLEEP ->  applySleep(p, duration);
            default -> throw new IllegalArgumentException("Invalid condition id");
        }
    }

    public static void statusEffect(Pokemon p, int statusId, double chance) {
        if (p.hasPrimaryCondition()) throw new MoveInterruptedException();

        switch (statusId) {
            case StatusCondition.BURN -> applyBurn(p, chance);  
            case StatusCondition.FREEZE -> applyFreeze(p, chance);  
            case StatusCondition.PARALYSIS -> applyParalysis(p, chance);   
            case StatusCondition.POISON -> applyPoison(p, chance); 
            default -> throw new IllegalArgumentException("Invalid condition id");
        }
    }
}
