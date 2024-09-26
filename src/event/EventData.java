package event;

import move.Move;
import pokemon.Pokemon;
import utility.Protection;


public class EventData {
    // Turn Info: Relevant to a singular Pokemon turn, this data is reset after each turn
    private final Pokemon user; // User of the move
    private final Pokemon target; // Target of the move
    private final Move moveUsed; // Move Used

    private int[] statChanges; // Stat Changes a Pokemon received
    private int[] statusChanges; // Status Condition a Pokemon received
    private int weatherChange; // Change in weather
    private int immuneStateChange; // The immunity state a Pokemon changed to
    private Protection protectionType; // The kind of damage nullifier

    private int damageDealt; // Damage dealt by attacker
 
    private double moveEffectiveness; // Effectiveness of the move on the target
    private boolean moveHits; // True if the move connects
    private boolean criticalHit; // True if the move was a critical hit
    private boolean moveFailed; // Move failed to execute
    private boolean moveInterrupted; // Move was interrupted
    private int hitCount; // Number of times move connects (multi-hit moves)

    private double recoilPercent; // Percentage of damage dealt applied to recoil
    private double drainPercent; // Percentage of damage dealt applied to heal

    private String message; // A message produced 
   

    public EventData(Pokemon a, Pokemon b, Move m) {
        this.user = a;
        this.target = b;
        this.moveUsed = m;
    }

    // Methods
    public void notifyEvent(String eventName) {
        this.user.events().onEvent(eventName, this);
        this.target.events().onEvent(eventName, this);  
    }

// Setters
    public void addDamageDealt(int damageDealt) {this.damageDealt += damageDealt;}
    public void setStatChanges(int[] statChanges) {this.statChanges = statChanges;}
    public void setStatusChanges(int[] statusChanges) {this.statusChanges = statusChanges;}
    public void setWeatherChange(int weatherChange) {this.weatherChange = weatherChange;}
    public void setImmuneStateChange(int immuneStateChange) {this.immuneStateChange = immuneStateChange;}
    public void setProtectionType(Protection protectionType) {this.protectionType = protectionType;}
    public void setMoveEffectiveness(double moveEffectiveness) {this.moveEffectiveness = moveEffectiveness;}
    public void setMoveHits(boolean moveHits) {this.moveHits = moveHits;}
    public void setCriticalHit(boolean criticalHit) {this.criticalHit = criticalHit;}
    public void setMoveFailed(boolean moveFailed) {this.moveFailed = moveFailed;}
    public void setMoveInterrupted(boolean moveInterrupted) {this.moveInterrupted = moveInterrupted;}
    public void setHitCount(int hitCount) {this.hitCount = hitCount;}
    public void setRecoilPercent(double recoilPercent) {this.recoilPercent = recoilPercent;}
    public void setDrainPercent(double drainPercent) {this.drainPercent = drainPercent;}
    public void setMessage(String message) {this.message = message;}

// Getters
    public Pokemon user() {return this.user;}
    public Pokemon target() {return this.target;}
    public Move moveUsed() {return this.moveUsed;}
    public int[] statChanges() {return this.statChanges;}
    public int[] statusChanges() {return this.statusChanges;}
    public int weatherChange() {return this.weatherChange;}
    public int immuneStateChange() {return this.immuneStateChange;}
    public Protection protectionType() {return this.protectionType;}
    public int damageDealt() {return this.damageDealt;}
    public double moveEffectiveness() {return this.moveEffectiveness;}
    public boolean moveHits() {return this.moveHits;}
    public boolean criticalHit() {return this.criticalHit;}
    public boolean moveFailed() {return this.moveFailed;}
    public boolean moveInterrupted() {return this.moveInterrupted;}
    public int hitCount() {return this.hitCount;}
    public double recoilPercent() {return this.recoilPercent;}
    public double drainPercent() {return this.drainPercent;}
    public String message() {return this.message;} 
}
