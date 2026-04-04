package project.game.pokemon;

import java.util.ArrayList;

import project.game.battle.BattleLog;
import project.game.event.GameEvents;
import project.game.event.GameEvents.EventID;
import project.game.move.Move;
import project.game.player.PokemonTrainer;
import project.game.pokemon.effects.Ability;
import project.game.pokemon.effects.HeldItem;
import project.game.pokemon.stats.HealthPoints;
import project.game.pokemon.stats.StatPoint;


public class Pokemon {

// Error Messages
    public static final String INVALID_DAMAGE_ERR = "Damage value must be positive";

// Class Variables
    // Default Level
    public static final int DEFAULT_LEVEL = 50;

// Object Variables
    private final int level; // Pokemon Level, higher level means a stronger pokemon 
    private final String pokemonName; // Pokemon's Name (Pokedex Entry)
    private final PokemonType pokemonType; // Pokemon have one or two types
    private final int pokedexID; // National Pokedex Number

    // Pokemon Stats
    private final HealthPoints hp; // Amount of HP the Pokemon has
    private final StatPoint[] stats; // [atk, def, spAtk, spDef, spd, acc, eva]
    private final double weight; // Weight of the Pokemon

    // Moves
    private final ArrayList<Move> moves; // Available moves, can have up to four

    //Conditions
    private final PokemonConditions conditions; 

    // Ability
    private Ability ability;

    // Held Item
    private HeldItem item;

    // Owner of the Pokemon
    private PokemonTrainer owner;

    // Pokemon's Name (Given by the player)
    private String nickname; 


    // Event Listeners
    private final GameEvents events;

    // Other Stats
    private Move moveSelected; // Move selected for the round
    private Move firstMove; // First move used since switched in
    private Move lastMove; // Move used the last turn
    private int damageDealt; // Amount of damage dealt during the round
    private int damageReceived; // Amount of damage received from opposing Pokemon
    private int roundCount; // Rounds since last switch-in

// Constructor
    // Creates a new Pokemon for trainers to use in battle
    public Pokemon(
        int level,
        String name,
        PokemonType types,
        int id,
        double weight,
        HealthPoints hp,
        StatPoint[] stats
    ) {
        this.level = level;
        this.pokemonName = name;
        this.pokemonType = types;

        this.pokedexID = id;
        this.weight = weight;
        this.hp = hp;
        this.stats = stats;

        this.moves = new ArrayList<Move>();
        this.conditions = new PokemonConditions();
        this.events = new GameEvents();
    }

// Methods

    /**
     * Takes damage which lowers HP
     * If HP drops to 0, the pokemon faints
     * 
     * @throws IllegalArgumentException if value isn't positive
     * @param value damage received
     */ 
    public void takeDamage(int value) {
        if (value <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);

        this.conditions.setTookDamage(true);
        this.damageReceived += value;

        if (this.getConditions().getEndure().isActive()) {
            this.conditions.getEndure().setActive(false);
            takeDamageEndure(value);
        }
        else this.hp.change(-value); 
        if (this.hp.depleted()) this.faints();   
    }

    
    // Takes damage equal to a percent of max HP
    public void takeDamagePercentMaxHP(double percent, String message) {
        int damage = (int) (this.getHp().getCurrentHealthPoints() * percent);
        BattleLog.add("%s took %d damage%s", this, damage, message + "!");
        this.takeDamage(damage);
    }

    /**
     * Takes damage, but prevents HP from dropping to zero
     * @throws IllegalArgumentException if value isn't positive
     * @param value damage received
     */
    private void takeDamageEndure(int value) {
        if (value <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);

        this.hp.change(-value);
        if (this.hp.depleted()) {
            this.hp.change(1);
            BattleLog.add("%s endured the hit!", this);
        }
    }

    /**
     * Restores HP
     * @throws IllegalArgumentException if value isn't positive
     * @param value health restored
     */
    public void restoreHP(int value) {
        if (value <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);
        this.hp.change(value);
    }

     // Takes damage equal to a percent of max HP
     public void restoreHpPercentMaxHP(double percent, String message) {
        int heal = (int) (this.hp.getCurrentHealthPoints() * percent);
        BattleLog.add("%s restored %d HP%s", this, heal, message + "!");
        this.restoreHP(heal);
    }


    public boolean hpLessThanPercent(double percent) {
        return this.hp.getCurrentHealthPoints() / (double) this.hp.getMaxHealthPoints() < 0.01 * percent; 
    }

    public boolean firstRound() {
        return this.roundCount == 0;
    }


    @Override
    /**
     * Returns the Pokemon's name (uses nickname if initialized)
     */
    public String toString() {
        return this.nickname != null ? this.nickname : this.pokemonName;
    }


// Boolean Methods
    public boolean hasNoMoves() {
        for (Move m : this.moves) 
            if (!(m.getPp().depleted() || m.getDisabled())) return false;
        return true;
    }

    public boolean isType(String type) {
        return this.pokemonType.typeEquals(type);
    }
 
// Setters
    public void faints() {
        this.conditions.setFainted(true);
        BattleLog.add("%s fainted!", this);
    } 
    
    public void addDealtDamage(int d) {
        if (d <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);
        this.damageDealt += d;
    }

    public void addDamageReceived(int d) {
        if (d <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);
        this.damageReceived += d;
    }

    public void resetDamageDealt() {
        this.damageDealt = 0;
    }

    public void setMove(Move m) {
        this.moveSelected = m;
    }

    public void resetMove() {
        if (this.moveSelected == null) return;
        if (this.firstRound()) this.firstMove = this.moveSelected;

        this.moveSelected.resetStats();
        this.lastMove = this.moveSelected;
        this.moveSelected = null;
    }

    public void addMove(Move m) {
        if (this.moves.size() > 4) { // 4 moves max
            throw new IllegalStateException("Pokemon cannot have more than 4 moves");
        }

        // Checks for duplicate moves
        for (Move move : this.moves) {
            if (move.getMoveID() == m.getMoveID()){
                throw new IllegalStateException("Duplicate move ids found, not allowed");
            }

        }

        // Adds moves 
        this.moves.add(m);
    } 

    public void clearStatMods() {
        for (StatPoint s : this.stats) s.resetMod();
    }

    public void endOfRoundReset() {
        if (this.conditions.isFainted()) {
            this.conditions.clearPrimary();
            this.conditions.clearVolatileConditions();
            return;
        }
            
        this.clearStatMods();
        this.conditions.setTookDamage(false);
        this.conditions.setHasMoved(false);
     
    
        if (this.conditions.isSwitchedIn()) {
            this.conditions.setSwitchedIn(false);
            return;
        }

        this.resetMove();
        this.damageDealt = 0;
        this.damageReceived = 0;
        this.roundCount++;
    }

    // Clears any temporary effects and volatile conditions
    public void backToTrainer() {
        this.resetMove();
        this.conditions.clearAtReturn();
        this.firstMove = null;
        this.lastMove = null;   
        this.damageDealt = 0;
        this.damageReceived = 0;  
        this.roundCount = 0;

        this.events.updateEvent(EventID.SWITCH_OUT, null);
    }

    public void removeItem() {
        if (this.item == null) return;
        this.item.removeEffect();
    }

// Setters
    public void setAbility(Ability a) {this.ability = a;}
    public void setOwner(PokemonTrainer pt) {this.owner = pt;}
    public void setItem(HeldItem i) {this.item = i;}
    public void setNickName(String n) {this.nickname = n != "" ? n : null;}

// Getters
    public int getLevel() {return this.level;}
    public String getPokemonName() {return this.pokemonName;}
    public PokemonType getPokemonType() {return this.pokemonType;}
    public int getPokedexID() {return this.pokedexID;}
    public HealthPoints getHp() {return this.hp;}
    public StatPoint[] getStats() {return this.stats;}
    public StatPoint getAttack() {return this.stats[StatPoint.ATTACK];}
	public StatPoint getDefense() {return this.stats[StatPoint.DEFENSE];}
	public StatPoint getSpecialAttack() {return this.stats[StatPoint.SPECIAL_ATTACK];}
	public StatPoint getSpecialDefense() {return this.stats[StatPoint.SPECIAL_DEFENSE];}
	public StatPoint getSpeed() {return this.stats[StatPoint.SPEED];}
	public StatPoint getAccuracy() {return this.stats[StatPoint.ACCURACY];}
	public StatPoint getEvasion() {return this.stats[StatPoint.EVASION];}
	public double getWeight() {return this.weight;}
    public ArrayList<Move> getMoves() {return this.moves;}
    public PokemonConditions getConditions() {return this.conditions;}
    public Move getMoveSelected() {return this.moveSelected;}
    public Move getFirstMove() {return this.firstMove;}
    public Move getLastMove() {return this.lastMove;}
    public int getDamageDealt() {return this.damageDealt;}
    public int getDamageReceived() {return this.damageReceived;}
    public int getRoundCount() {return this.roundCount;}
    public Ability getAbility() {return this.ability;}
    public HeldItem getItem() {return this.item;}
    public String getNickname() {return this.nickname;}
    public PokemonTrainer getOwner() {return this.owner;}
    public GameEvents getEvents() {return this.events;}

}
