package move;
import battle.BattleField;
import battle.BattleLog;
import battle.Weather;
import java.util.Random;
import pokemon.Pokemon;
import pokemon.PokemonType;
import stats.Stat;
import stats.Type;

@FunctionalInterface
public interface MoveAction {
    // Function
    void useMove(Pokemon attacker, Pokemon defender, Move move);

    // Private Methods
    private static void changeStat(Pokemon p, int change, int id) {
        Stat s = p.stats()[id];
        if (s.isAtHighestOrLowestStage(change)) {
            BattleLog.add(String.format("%s's %s won't go any %s!", p, s, (change > 0) ? "higher" : "lower"));
            return;
        }
        p.stats()[id].changeStage(change);
        BattleLog.add(String.format("%s's %s %s%s!", p, s, (change > 0) ? "rose" : "fell", Stat.sizeOfChange(change)));
    }


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

    private static double calculateAttack(Stat stat, boolean crit) {
        return crit && stat.stage() < 0 ? stat.base() : stat.power();
    }

    private static double calculateDefense(Stat stat, boolean crit) {
        return crit && stat.stage() > 0 ? stat.base() : stat.power();
    }

    private static double sameTypeAttackBonus(Pokemon p, Move m){
        return (m.isType(p.pokemonType())) ? 1.5 : 1.0;
    }

    private static double weatherBonus(Move m) {
        return (BattleField.currentWeather == Weather.SUNNY) 
        ? (m.isType(Type.FIRE)) ? 1.5 : (m.isType(Type.WATER)) ? 0.5 : 1.0
        :  (BattleField.currentWeather == Weather.RAIN) 
            ? (m.isType(Type.WATER)) ? 1.5 : (m.isType(Type.FIRE)) ? 0.5 : 1.0
            : 1.0
        ;
    }

    private static int calculateDamage(Pokemon attacker, Pokemon defender, Move move, double effectiveness, boolean isCritical){
        double stab = sameTypeAttackBonus(attacker, move);
        double crit = isCritical ? 1.5 : 1.0;
        double random = random();
        double weather = weatherBonus(move);

        double attack = (move.category().equals(Move.SPECIAL) ? calculateAttack(attacker.specialAttack(), isCritical) : calculateAttack(attacker.attack(), isCritical));
        double defense = (move.category().equals(Move.SPECIAL) ? calculateDefense(defender.specialDefense(), isCritical) : calculateDefense(defender.defense(), isCritical));
        
        return (int) (((((2 * Pokemon.DEFAULT_LEVEL) / 5.0 + 2) * move.power() * (attack / defense)) / 50.0 + 2) 
        * stab * crit * effectiveness * random * weather) + 1;      
    }

    // Methods for MoveList
    public static boolean moveHits(Pokemon attacker, Pokemon defender, Move move) {
        if (move.accuracy() == Move.INF || defender.immobilized()) return true;
		double modifiedAccuracy = 0.01 * move.accuracy() * ((double) attacker.accuracy().power() / (double) defender.evasion().power());

		boolean hit = new Random().nextDouble() <= modifiedAccuracy;

        if (!hit) {
            BattleLog.add(String.format("But %s avoided the attack!", defender));
            return false;
        }
        else return true;
    } 

    public static void dealDamage(Pokemon attacker, Pokemon defender, Move move) {
        double effectiveness = typeEffectiveness(move.moveType(), defender.pokemonType());

        if (effectiveness == 0) {
            BattleLog.add(String.format("But it doesn't affect %s...", defender));
            return;
        }

        if (!moveHits(attacker, defender, move)) return;
        
        boolean isCritical = criticalHit(move.critRate());

        int damage = calculateDamage(attacker, defender, move, effectiveness, isCritical);
        defender.takeDamage(damage);

        BattleLog.add(String.format("%s took %d damage!", defender, damage));
        BattleLog.add(isSuperEffective(effectiveness));
        BattleLog.add((isCritical) ? "Critical hit!" : "");
    }

    public static void changeMove(Pokemon p1, Pokemon p2, Move move ) {
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

    // Stat Changes
    public static void attackStat(Pokemon p, int change) {
        changeStat(p, change, Stat.ATTACK);
    }

    public static void defenseStat(Pokemon p, int change) {
        changeStat(p, change, Stat.DEFENSE);
    }

    public static void spAttackStat(Pokemon p, int change) {
        changeStat(p, change, Stat.SPECIAL_ATTACK);
    }

    public static void spDefenseStat(Pokemon p, int change) {
        changeStat(p, change, Stat.SPECIAL_DEFENSE);
    }

    public static void speedStat(Pokemon p, int change) {
        changeStat(p, change, Stat.SPEED);
    }

    public static void accuracyStat(Pokemon p, int change) {
        changeStat(p, change, Stat.ACCURACY);
    }

    public static void evasionStat(Pokemon p, int change) {
        changeStat(p, change, Stat.EVASION);
    }
}
