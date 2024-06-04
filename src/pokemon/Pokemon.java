package pokemon;

import battle.BattleLog;
import battle.MoveInterruptedException;
import battle.PokemonCannotActException;
import battle.PokemonFaintedException;
import move.Move;
import stats.Stat;
import stats.StatusCondition;

public class Pokemon {
// Class Variables
    // Default Level
    public static final int DEFAULT_LEVEL = 50;


// Object Variables
    private final int level; // Pokemon Level, higher level means a stronger pokemon 
    private final String pokemonName; // Pokemon's Name (Given by the player)
    private final PokemonType type; // Pokemon have one or two types
    private final int pokedexNo; // National Pokedex Number

    // Pokemon Stats
    private final HealthPoints hp; // Amount of HP the Pokemon has
    private final Stat[] stats; // [atk, def, spAtk, spDef, spd, acc, eva]
    private final double weight; // Weight of the Pokemon

    // Moves
    private final Move[] moves; // Available moves, can have up to four

    // Status Conditions (TODO: Make these booleans into a new Class)
    private boolean fainted; // When the Pokemon is unable to 
    private boolean immobilized; // When the pokemon cannot act or dodge attacks
    private boolean charged; // When the Pokemon charges up a move
    private boolean switchedIn; // Set to true when the pokemon first enters the field;
    private boolean hasMoved; // When the Pokemon has moved during the round
    private boolean flinched; // When the Pokemon cannot act for the turn

    private StatusCondition primaryCondition; // Non-Volatile Condition (Does not clear when the Pokemon switches)

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
        Move[] moves
    ) {
        this.level = level;
        this.pokemonName = name;
        this.type = types;

        this.pokedexNo = pokedex;
        this.weight = weight;
        this.hp = hp;
        this.stats = stats;

        this.moves = moves;
    }

// Methods
    /**
     * A Pokemon that flinches cannot act
     * @throws PokemonCannotActException If the Pokemon flinched
     */
    public void checkFlinched() {
        if (!this.flinched) return;

        this.charged = false;
        this.flinched = false;
        throw new PokemonCannotActException(String.format("%s flinched and couldn't move!", this));    
    }

    /**
     * Checks all the Pokemon's status effects
     * @param before If looking at condition that are applied before the Pokemon moves
     */
    public void checkConditions(boolean before) {
        if (this.primaryCondition != null)
            if (this.primaryCondition.beforeMove() == before) this.primaryCondition.action().apply(this);
        this.checkFlinched();
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
        this.hasMoved = true;
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

// Statistic Methods (All Methods display a Pokemon's information to the console)
    public String listStats() {
        return new StringBuilder()
        .append(String.format("Attack: %d%n", this.stats[Stat.ATTACK].power()))
        .append(String.format("Defense: %d%n", this.stats[Stat.DEFENSE].power()))
        .append(String.format("Special-Attack: %d%n", this.stats[Stat.SPECIAL_ATTACK].power()))
        .append(String.format("Special-Defense: %d%n", this.stats[Stat.SPECIAL_DEFENSE].power()))
        .append(String.format("Speed: %d%n", this.stats[Stat.SPEED].power()))
        .append(String.format("Accuracy: %d%%%n", this.stats[Stat.ACCURACY].power()))
        .append(String.format("Evasion: %d%%%n", this.stats[Stat.EVASION].power()))
        .toString();
    }

    public String listMoves() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.moves.length; i++) 
            sb.append(String.format("[%d] %s", i, this.moves[i].moveStats())); 
            
        return sb.toString();
    }

    public String showAllStats() {
		return new StringBuilder()
		.append(String.format("Name: %s%n", this.pokemonName))
		.append(String.format("Type: %s", this.type.toString()))
        .append(String.format("LEVEL %d%n", this.level))
		.append(String.format("%nPokedex Number: %d%n", this.pokedexNo))
		.append(String.format("%nSTATS:%nHP: %s%n", this.hp.toString()))
		.append(String.format("%s", this.listStats()))
        .append(String.format("%nMOVES: %n%s", this.listMoves()))
        .toString();
	}

    public String showCondition() {
        if (this.fainted) return "FAINTED";
        if (!this.hasPrimaryCondition()) return "";
        return this.primaryCondition.toString();
    }

    public String showPartyStats() {
        return String.format("%s (HP: %s) %s%n", this, this.hp, this.showCondition());
    }


    @Override
    public String toString() {
        return this.pokemonName;
    }


// Boolean Methods
    public boolean hasPrimaryCondition() {
        return this.primaryCondition != null;
    }

    public boolean hasPrimaryCondition(int i) {
        if (this.primaryCondition == null) return false;
        return this.primaryCondition.id() == i;
    }

    public boolean hasNoMoves() {
        for (Move m : this.moves) 
            if (!m.pp().depleted()) return false;
        return true;
    }
 
// Setters
    public void setFainted(boolean f) {
        this.fainted = f;
    }
    public void setImmobilized(boolean i) {
        this.immobilized = i;
    }

    public void setCharge(boolean c) {
        this.charged = c;
    }

    public void setSwitchedIn(boolean s) {
        this.switchedIn = s;
    }

    public void setHasMoved(boolean h) {
        this.hasMoved = h;
    }

    public void setFlinched(boolean f) {
        this.flinched = f;
    }

    public void setPrimaryCondition(StatusCondition c) {
        this.primaryCondition = c;
    }

    public void clearPrimaryCondition() {
        this.primaryCondition = null;
    }

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
        this.charged = false;
        this.switchedIn = false;
        this.immobilized = false;
        this.moveSelected = null;
    }

// Getters
    public int level() {
        return this.level;
    }

    public String pokemonName() {
        return this.pokemonName;
    }

    public PokemonType pokemonType() {
        return this.type;
    }

    public int pokedexNo() {
        return this.pokedexNo;
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

    public boolean immobilized() {
        return this.immobilized;
    }

    public boolean fainted() {
        return this.fainted;
    }

    public boolean charged() {
        return this.charged;
    }

    public boolean switchedIn() {
        return this.switchedIn;
    }

    public boolean hasMoved() {
        return this.hasMoved;
    }

    public boolean flinched() {
        return this.flinched;
    }

    public Move[] moves() {
        return this.moves;
    }

    public StatusCondition primaryCondition() {
        return this.primaryCondition;
    }

    public int damageDealt() {
        return this.damageDealt;
    }

    public Move moveSelected() {
        return this.moveSelected;
    }

}
