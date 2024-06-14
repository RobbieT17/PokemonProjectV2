package pokemon;

import battle.Battle;
import battle.BattleField;
import battle.BattleLog;
import battle.Weather;
import exceptions.*;
import move.Move;
import stats.Stat;
import stats.StatusCondition;
import stats.Type;

public class Pokemon {

// Error Messages
    public static final String INVALID_DAMAGE_ERR = "Damage value must be positive";

// Class Variables


    // Default Level
    public static final int DEFAULT_LEVEL = 50;


// Object Variables
    private final int level; // Pokemon Level, higher level means a stronger pokemon 
    private final String pokemonName; // Pokemon's Name (Given by the player)
    private final PokemonType pokemonType; // Pokemon have one or two types
    private final int pokedexID; // National Pokedex Number

    // Pokemon Stats
    private final HealthPoints hp; // Amount of HP the Pokemon has
    private final Stat[] stats; // [atk, def, spAtk, spDef, spd, acc, eva]
    private final double weight; // Weight of the Pokemon

    // Moves
    private final Move[] moves; // Available moves, can have up to four

    //Conditions
    private final PokemonConditions conditions; 

    // Other Stats
    private int damageDealt; // Amount of damage dealt during the round
    private int damageReceived; // Amount of damage received from opposing Pokemon
    private Move moveSelected; // Move selected for the round
    private Move lastMove; // Move used the last turn

// Constructor
    // Creates a new Pokemon for trainers to use in battle
    public Pokemon(
        int level,
        String name,
        PokemonType types,
        int pokedex,
        double weight,
        HealthPoints hp,
        Stat[] stats, 
        Move[] moves,
        PokemonConditions conditions
    ) {
        this.level = level;
        this.pokemonName = name;
        this.pokemonType = types;

        this.pokedexID = pokedex;
        this.weight = weight;
        this.hp = hp;
        this.stats = stats;

        this.moves = moves;
        this.conditions = conditions;
    }

// Methods
    /**
     * Checks a Pokemon's primary condition if it has one
     * @param before If the effect goes before the move
     */
    private void checkPrimaryCondition(boolean before) {
        if (this.conditions.filterPrimaryCondition(before) == null) return;
        this.conditions.filterPrimaryCondition(before).action().apply(this);
    }

    /**
     * A Pokemon that flinches cannot act
     * @throws PokemonCannotActException If the Pokemon flinched
     */
    private void checkFlinched() {
        if (!this.conditions.flinched()) return;

        this.conditions.setForcedMove(false);
        throw new PokemonCannotActException("%s flinched and couldn't move!", this);    
    }

    /**
     * Applies all other before/after move conditions
     * @param before
     */
    private void checkVolatileConditions(boolean before) {
        this.conditions.filterVolatileConditions(before).forEach(c -> c.action().apply(this)); 
    }


    /**
     * Checks all the Pokemon's status effects
     * @param before If looking at condition that are applied before the Pokemon moves
     */
    public void checkConditions(boolean before) {
        this.checkPrimaryCondition(before);
        this.checkFlinched();
        this.checkVolatileConditions(before);
    }

    /**
     * Uses a move on a target
     * Decrements that moves PP
     * @param move the Move chosen
     * @param defender the target Pokemon
     */
    public void useMove(Pokemon defender) {
        BattleLog.add("%s used %s!", this, this.moveSelected);
        this.moveSelected.pp().decrement(this);
        this.moveSelected.action().act(this, defender, this.moveSelected);
    }

    /**
     * Checks any condition, then
     * Pokemon uses their move if actionable
     * Pokemon is considered to have moved (even if they cannot act)
     * 
     * @param move the Move chosen
     * @param defender the target Pokemon
     */
    public void useTurn(Pokemon defender){
        try {
            this.checkConditions(true);
            this.useMove(defender);
            this.conditions.setInterrupted(false); // Successful Move
        } catch (MoveInterruptedException | PokemonCannotActException e) {
            BattleLog.add(e.getMessage());
    
            // Resets any move modifications
            this.moveSelected().power(); 
            this.moveSelected().accuracy();

            // Stops any ongoing moves
            this.conditions.setForcedMove(false);
            this.conditions.setFocused(false);
            this.conditions.setInterrupted(true);
            this.conditions.stopRampage();
        }
        this.conditions.setHasMoved(true); 
    }

    /**
     * Takes damage which lowers HP
     * If HP drops to 0, the pokemon faints
     * 
     * @throws IllegalArgumentException if value isn't positive
     * @param value damage received
     */ 
    public void takeDamage(int value) {
        if (value <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);

        this.hp.change(-value); 
        if (this.hp.depleted()) this.faints();   
    }

    /**
     * Takes damage, but prevents HP from dropping to zero
     * @throws IllegalArgumentException if value isn't positive
     * @param value damage received
     */
    public void takeDamageEndure(int value) {
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
    public void healDamage(int value) {
        if (value <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);
        this.hp.change(value);
    }

    // Takes damage from Sandstorm / Hail Weather
    public void weatherEffect() {
        switch (BattleField.currentWeather) {
            case Weather.SANDSTORM ->  {
                // Ground, Rock, and Steel types are immune to sandstorm
                if (this.isType(Type.GROUND) || this.isType(Type.ROCK) || this.isType(Type.STEEL)) return;

                int damage = (int) (this.hp().max() / 8.0);
                BattleLog.add("%s took %d damage from the sandstorm!", this, damage);
                this.takeDamage(damage);
            }
            case Weather.HAIL ->  {
                // Ice types are immune to hail
                if (this.isType(Type.ICE)) return;

     
                int damage = (int) (this.hp().max() / 16.0);  
                BattleLog.add("%s took %d damage from the hail!", this, damage);
                this.takeDamage(damage);
            }
        }
        if (this.conditions.fainted()) throw new PokemonFaintedException();
    }

    // List Pokemon's stats
    public String showAllStats() {
		return new StringBuilder()
		.append(String.format("Name: %s  |  ", this.pokemonName))
		.append(String.format("Type: %s  |  ", this.pokemonType.toString()))
        .append(String.format("LEVEL %d  |  ", this.level))
		.append(String.format("Pokedex #: %d%n", this.pokedexID))
		.append(String.format("%nHP: %s%n", this.hp.toString()))
		.append(String.format("%n%s", PokemonStats.listStats(this)))
        .append(String.format("%nMOVES: %n%s", PokemonStats.listMoves(this)))
        .toString();
	}

    public String showSomeStats() {
        return new StringBuilder()
        .append(String.format("Name: %s  |  ", this.pokemonName))
        .append(String.format("Type: %s%n", this.pokemonType.toString()))
        .append(String.format("%nHP: %s%n", this.hp.toString()))
        .append(String.format("Status Effect: %s%n", PokemonStats.showCondition(this)))
        .append(String.format("Other Effects: %s%n", PokemonStats.showVolatileConditions(this)))
        .append(String.format("%nMOVES: %n%s", PokemonStats.listMoves(this)))
        .toString();
    }

    @Override
    public String toString() {
        return this.pokemonName;
    }


// Boolean Methods
    public boolean hasPrimaryCondition() {
        return this.conditions.primaryCondition() != null;
    }

    public boolean hasPrimaryCondition(int i) {
        return this.conditions.primaryCondition() != null
        ? this.conditions.primaryCondition().id() == i
        : false;
    }

    public boolean hasCondition(int i) {
        return this.conditions.hasKey(i);
    }

    public boolean hasNoMoves() {
        for (Move m : this.moves) 
            if (!m.pp().depleted()) return false;
        return true;
    }

    public boolean isType(String type) {
        return (this.pokemonType.hasSecondaryType())
        ? this.pokemonType.primaryType().typeName().equals(type) || this.pokemonType.secondaryType().typeName().equals(type)
        : this.pokemonType.primaryType().typeName().equals(type);
    }

    public boolean hasTakenDamage() {
        return this.damageReceived != 0;
    }
 
// Setters
    private void faints() {
        this.conditions.setFainted(true);
        this.conditions.clearPrimaryCondition();
        this.conditions.clearVolatileConditions();
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

    public void clearPrimaryCondition(int c) {
        this.conditions.clearPrimaryCondition();
        BattleLog.add(this + StatusCondition.expireMessage(c));
    }

    public void clearCondition(int c) {
        this.conditions.remove(c);
        BattleLog.add(this + StatusCondition.expireMessage(c));
    }

    public void setMove(Move m) {
        this.moveSelected = m;
    }

    public void resetMove() {
        this.lastMove = this.moveSelected;
        this.moveSelected = null;
    }

    public void afterEffects() {
        this.conditions.setSwitchedIn(false);
        if (Battle.skipRound || this.conditions.fainted()) return;
            
        this.resetMove();
        this.damageDealt = 0;
        this.damageReceived = 0;
        this.conditions.clearAtEndRound();

        try {
            this.weatherEffect();
            this.checkConditions(false);
        } catch (PokemonFaintedException e) {
        }   
    }

    // Clears any temporary effects and volatile conditions
    public void backToTrainer() {
        this.resetMove();
        this.conditions.clearAtReturn();
        this.damageDealt = 0;
        this.damageReceived = 0;
        this.lastMove = null;     
    }

// Getters
    public int level() {
        return this.level;
    }

    public String pokemonName() {
        return this.pokemonName;
    }

    public PokemonType pokemonType() {
        return this.pokemonType;
    }

    public int pokedexID() {
        return this.pokedexID;
    }

    public HealthPoints hp() {
        return this.hp;
    }

    public Stat[] stats() {
        return this.stats;
    }

    public Stat attack() {
		return this.stats[Stat.ATTACK];
	}

	public Stat defense() {
		return this.stats[Stat.DEFENSE];
	}

	public Stat specialAttack() {
		return this.stats[Stat.SPECIAL_ATTACK];
	}

	public Stat specialDefense() {
		return this.stats[Stat.SPECIAL_DEFENSE];
	}

	public Stat speed() {
		return this.stats[Stat.SPEED];
	}

	public Stat accuracy() {
		return this.stats[Stat.ACCURACY];
	}

	public Stat evasion() {
		return this.stats[Stat.EVASION];
	}

	public double weight() {
		return this.weight;
	}

    public Move[] moves() {
        return this.moves;
    }

    public PokemonConditions conditions() {
        return this.conditions;
    }

    public int damageDealt() {
        return this.damageDealt;
    }

    public int damageReceived() {
        return this.damageReceived;
    }

    public Move moveSelected() {
        return this.moveSelected;
    }

    public Move lastMove() {
        return this.lastMove;
    }

}
