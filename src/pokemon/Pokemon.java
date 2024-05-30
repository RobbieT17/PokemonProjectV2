package pokemon;

import battle.BattleLog;
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
    private boolean actionable;
    private boolean immobilized;
    private boolean fainted;
    private boolean charged; // Charges moves

    private StatusCondition primaryCondition;

    // Other Stats
    private int damageDealt;


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

        this.actionable = true;
    }

    // Private Methods
    private String listStats() {
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

    private String listMoves() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.moves.length; i++) 
            sb.append(String.format("[%d] %s", i, this.moves[i].moveStats())); 
            
        return sb.toString();
    }

    // Methods
    public void applyEffects(boolean before) {
        if (this.primaryCondition != null)
            if (this.primaryCondition.beforeMove() == before) this.primaryCondition.action().apply(this);
    }

    public void useTurn(Move move, Pokemon defender) {
        this.applyEffects(true);
        this.useMove(move, defender);
    }

    public void useMove(Move move, Pokemon defender) {
        if (!this.actionable) return;

        BattleLog.add(String.format("%s used %s!", this, move));
        move.pp().decrement();
        move.action().useMove(this, defender, move);
    }

    public void takeDamage(int value) {
        if (value <= 0) throw new IllegalArgumentException("Damage must be a positive value");
        this.hp.change(-value); 
        if (this.hp.value() == 0) {
            this.fainted = true;
            BattleLog.add(String.format("%s fainted!", this));
        }
    }

	public String showStats() {
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

    @Override
    public String toString() {
        return this.pokemonName;
    }


    // Boolean Methods
    public boolean hasNonVolatileCondition() {
        return this.primaryCondition != null;
    }

    public boolean hasNonVolatileCondition(int i) {
        return this.primaryCondition.id() == i;
    }

    public boolean hasNoMoves() {
        for (Move m : this.moves) 
            if (!m.pp().depleted()) return false;

        return true;
    }
 
    // Setters
    public void setActionable(boolean a) {
        this.actionable = a;
    }

    public void setImmobilized(boolean i) {
        this.immobilized = i;
    }

    public void setCharge(boolean c) {
        this.charged = c;
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

    public boolean actionable() {
        return this.actionable;
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

    public Move[] moves() {
        return this.moves;
    }

    public StatusCondition primaryCondition() {
        return this.primaryCondition;
    }

    public int damageDealt() {
        return this.damageDealt;
    }

}
