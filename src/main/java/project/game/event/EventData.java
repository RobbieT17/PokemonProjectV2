package project.game.event;

import java.util.ArrayList;

import project.game.battle.BattleData;
import project.game.battle.Weather.WeatherEffect;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.utility.Protection;


public class EventData {
    // Turn Info: Relevant to a singular Pokemon turn, this data is reset after each turn
    public final BattleData battleData; // Contains trainer info
    
    public final ArrayList<Pokemon> pokemonList; // List of Pokemon included in the event
    public final Pokemon user; // User of the move
    public final Move moveUsed; // Move Used

    public Pokemon attackTarget; // Target of the move (defaults to opponent but may well be the same as user)
    public Pokemon effectTarget; // The target of any additional effects

    public int[] statChanges; // Stat Changes a Pokemon received
    public double statProb; // Probability of stat change
    public boolean statFailed; // Stats failed to change (either reached highest stage or low prob roll)

    public double statusProb; // Probability of status change
    public boolean statusFailed; // Status failed to change (immunity/low prob roll/etc.)

    public StatusConditionID statusChange; // Status Condition a Pokemon received
    public StatusConditionID immuneStateChange; // The immunity state a Pokemon changed to

    public WeatherEffect weatherChange; // Change in weather
    
    public Protection protectionType; // The kind of damage nullifier

    public int damageDealt; // Damage dealt by attacker
 
    public double moveEffectiveness; // Effectiveness of the move on the target
    public double otherMoveMods; // Other move mod multipliers (From effects)
    public boolean moveHits; // True if the move connects
    public boolean criticalHit; // True if the move was a critical hit
    public boolean moveFailed; // Move failed to execute
    public boolean moveInterrupted; // Move was interrupted
    public int hitCount; // Number of times move connects (multi-hit moves)

    public double recoilPercent; // Percentage of damage dealt applied to recoil
    public double drainPercent; // Percentage of damage dealt applied to heal
    public double healPercent; // Percentage of max HP restored

    public String message = ""; // A message produced 
   

    public EventData(BattleData data, Pokemon a, Pokemon b, Move m) {
        this.battleData = data;
        this.user = a;
        this.moveUsed = m;
        this.pokemonList = buildPokemonList(a, b);

        this.attackTarget = b;
        this.effectTarget = b; 
        this.otherMoveMods = 1.0;
    }

    private ArrayList<Pokemon> buildPokemonList(Pokemon a, Pokemon b) {
        ArrayList<Pokemon> list = new ArrayList<>();

        list.add(a);
        list.add(b);

        return list;
    }

    // Methods

    /**
     * Checks if the Pokemon is the user of the move this turn
     * @param p Pokemon
     * @param e event data
     * @return True if the Pokeon is the users
     */
    public static boolean isUser(Pokemon p, EventData e) {
        return p == e.user;
    }

    public static boolean isTarget(Pokemon p, EventData e) {
        return p == e.attackTarget;
    }

}
