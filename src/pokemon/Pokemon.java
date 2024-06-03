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
    // Pokemon Level, higher level means stronger pokemon
    private final int level;

    // Pokemon's Name (Given by the player)
    private final String pokemonName;

    // Pokemon have one or two types
    private final PokemonType type;

    // National Pokedex Number
    private final int pokedexNo;

    // Pokemon Stats
    private final HealthPoints hp;
    private final Stat[] stats; // [atk, def, spAtk, spDef, spd, acc, eva]
    private final double weight;

    // Moves
    private final Move[] moves; // Up to 4 moves

    // Status Conditions
    private boolean immobilized;
    private boolean fainted;
    private boolean charged; // Charges moves
    private boolean switchedIn; // Set to true when pokemon first enters the field;
    private boolean hasMoved;
    private boolean flinched;

    private StatusCondition primaryCondition;

    // Other Stats
    private int damageDealt;
    private Move moveSelected;

    // Constructor
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
    public void checkFlinched() {
        if (!this.flinched) return;

        this.charged = false;
        this.flinched = false;
        throw new PokemonCannotActException(String.format("%s flinched and couldn't move!", this));    
    }

    public void checkConditions(boolean before) {
        if (this.primaryCondition != null)
            if (this.primaryCondition.beforeMove() == before) this.primaryCondition.action().apply(this);
        this.checkFlinched();
    }

    public void useMove(Move move, Pokemon defender) {
        BattleLog.add(String.format("%s used %s!", this, move));
        move.pp().decrement();
        move.action().act(this, defender, move);
    }

    public void useTurn(Move move, Pokemon defender){
        try {
            this.checkConditions(true);
            this.useMove(move, defender);
        } catch (PokemonCannotActException | MoveInterruptedException e) {
            BattleLog.add(e.getMessage());
        }
        this.hasMoved = true;
    }

    public void takeDamage(int value) {
        if (value <= 0) throw new IllegalArgumentException("Damage must be a positive value");

        this.hp.change(-value); 
        if (this.hp.depleted()) {
            this.fainted = true; 
            throw new PokemonFaintedException(this);
        } 
    }

    public void healDamage(int value) {
        if (value <= 0) throw new IllegalArgumentException("Damage must be a positive value");
        this.hp.change(value);
    }

    // Statistic Methods
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
