package project.event;

import project.move.Move;
import project.pokemon.Pokemon;
import project.utility.Protection;


public class EventData {
    // Turn Info: Relevant to a singular Pokemon turn, this data is reset after each turn
    public final Pokemon user; // User of the move
    public final Move moveUsed; // Move Used

    public Pokemon attackTarget; // Target of the move (defaults to opponent but may well be the same as user)
    public Pokemon effectTarget; // The target of any additional effects

    public int[] statChanges; // Stat Changes a Pokemon received
    public double statProb; // Probability of stat change
    public boolean statFailed; // Stats failed to change (either reached highest stage or low prob roll)

    public String statusChange; // Status Condition a Pokemon received
    public double statusProb; // Probability of status change
    public boolean statusFailed; // Status failed to change (immunity/low prob roll/etc.)

    public String immuneStateChange; // The immunity state a Pokemon changed to

    public int weatherChange; // Change in weather
    
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
   

    public EventData(Pokemon a, Pokemon b, Move m) {
        this.user = a;
        this.attackTarget = b;
        this.effectTarget = b;
        this.moveUsed = m;
        this.otherMoveMods = 1.0;
    }

    // Methods
    public void notifyEvent(String eventName) {
        this.user.events().onEvent(eventName, this);
        this.attackTarget.events().onEvent(eventName, this);  
    }

    public static boolean isUser(Pokemon p, EventData e) {
        return p == e.user;
    }

    public static boolean isTarget(Pokemon p, EventData e) {
        return p == e.attackTarget;
    }

}
