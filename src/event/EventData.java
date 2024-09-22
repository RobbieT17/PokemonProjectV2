package event;

import move.Move;
import pokemon.Pokemon;
import utility.Protection;


public class EventData {
    // Variables
    private final Pokemon user;
    private final Pokemon target;
    private final Move move;
    
    /* Ex. [0, -2, 0, -2, 0, 0, 0, 1] 
     * Lowers target's Def and Sp.Def by two stages
     * Final Index: 0 = User, 1 = Opponent*/
    private int[] statChanges; 
    private int damage;
    private double moveEffectiveness;
    private boolean moveHits;
    private boolean criticalHit;
    private int hitCount; // Number of times move connects (multi-hit moves)
    private double percent; // Recoil % or Drain % or Heal % etc.
    private int immuneState;
    private String message;
    private int weather;
    private Protection protect;
   

    // Constructors
    public EventData(Pokemon a) {
        this.user = a;
        this.target = null;
        this.move = null;
    }

    public EventData(Pokemon a, Pokemon b, Move m) {
        this.user = a;
        this.target = b;
        this.move = m;
    }

    // Setters
    public void addDamage(int i) {this.damage += i;}
    public void setStatChanges(int[] arr) {this.statChanges = arr;}
    public void setMoveEffectiveness(double d) {this.moveEffectiveness = d;}
    public void setPercent(double d) {this.percent = d;}
    public void setImmuneState(int i) {this.immuneState = i;}
    public void setMessage(String s) {this.message = s;}
    public void setWeather(int i) {this.weather = i;}
    public void setProtect(Protection p) {this.protect = p;}
    public void setMoveHits(boolean b) {this.moveHits = b;}
    public void incHitCount() {this.hitCount++;}
    public void setCriticalHit(boolean b) {this.criticalHit = b;}

    // Getters
    public Pokemon user() {return this.user;}
    public Pokemon target() {return this.target;}
    public Move moveUsed() {return this.move;}
    public int damage() {return this.damage;}
    public int[] statChanges() {return this.statChanges;}
    public double moveEffectiveness() {return this.moveEffectiveness;}
    public double percent() {return this.percent;}
    public int immuneState() {return this.immuneState;}
    public String message() {return this.message;}
    public int weather() {return this.weather;}
    public Protection protect() {return this.protect;}
    public boolean moveHits() {return this.moveHits;}
    public int hitCount() {return this.hitCount;}
    public boolean criticalHit() {return this.criticalHit;}
}
