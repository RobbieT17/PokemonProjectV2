package pokemon;

import battle.BattleLog;
import exceptions.*;
import move.Move;
import stats.Stat;

public class Pokemon {
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
    private Move moveSelected; // Move selected for the round

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

        this.conditions.setCharge(false);
        throw new PokemonCannotActException(String.format("%s flinched and couldn't move!", this));    
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
    public void useMove(Move move, Pokemon defender) {
        BattleLog.add(String.format("%s used %s!", this, move));
        move.pp().decrement();
        move.action().act(this, defender, move);
    }

    /**
     * Checks any condition, then
     * Pokemon uses their move if actionable
     * Pokemon is considered to have moved (even if they cannot act)
     * 
     * @param move the Move chosen
     * @param defender the target Pokemon
     */
    public void useTurn(Move move, Pokemon defender){
        try {
            this.checkConditions(true);
            this.useMove(move, defender);
        } catch (PokemonCannotActException | MoveInterruptedException e) {
            BattleLog.add(e.getMessage());
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
        if (value <= 0) throw new IllegalArgumentException("Damage must be a positive value");

        this.hp.change(-value); 
        if (this.hp.depleted()) throw new PokemonFaintedException(this);   
    }

    /**
     * Restores HP
     * @throws IllegalArgumentException if value isn't positive
     * @param value health restored
     */
    public void healDamage(int value) {
        if (value <= 0) throw new IllegalArgumentException("Damage must be a positive value");
        this.hp.change(value);
    }

    // List Pokemon's stats
    public String showAllStats() {
		return new StringBuilder()
		.append(String.format("Name: %s%n", this.pokemonName))
		.append(String.format("Type: %s", this.pokemonType.toString()))
        .append(String.format("LEVEL %d%n", this.level))
		.append(String.format("%nPokedex Number: %d%n", this.pokedexID))
		.append(String.format("%nSTATS:%nHP: %s%n", this.hp.toString()))
		.append(String.format("%s", PokemonStats.listStats(this)))
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
        if (this.conditions.primaryCondition() == null) return false;
        return this.conditions.primaryCondition().id() == i;
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
 
// Setters
    public void addDealtDamage(int d) {
        if (d <= 0) throw new IllegalArgumentException("Damage must be positive");
        this.damageDealt += d;
    }

    public void resetDamageDealt() {
        this.damageDealt = 0;
    }

    public void setMove(Move m) {
        this.moveSelected = m;
    }

    public void resetMove() {
        this.moveSelected = null;
    }

    // Clears any temporary effects
    public void backToTrainer() {
        this.damageDealt = 0;
        this.moveSelected = null;
        this.conditions.setCharge(false);
        this.conditions.setSwitchedIn(false);
        this.conditions.setImmobilized(false);
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

    public Move moveSelected() {
        return this.moveSelected;
    }

}
