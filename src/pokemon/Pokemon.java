package pokemon;

import battle.BattleLog;
import move.Move;
import stats.Stat;

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
    private boolean fainted;
    private boolean charged; // Charges moves

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
            sb.append(String.format("[%d] %s", i, this.moves[i].toString())); 
            
        return sb.toString();
    }

    // Methods
    public void useTurn(Move move, Pokemon defender) {
        
    }

    public void useMove(Move move, Pokemon defender) {
        BattleLog.add(String.format("%s used %s!", this.pokemonName, move.moveName()));
        move.pp().decrement();
        move.action().useMove(this, defender, move);
    }

    public void takeDamage(int value) {
        if (value <= 0) throw new IllegalArgumentException("Damage must be a positive value");
        this.hp.change(-value); 
        if (this.hp.value() == 0) {
            this.fainted = true;
            BattleLog.add(String.format("%s fainted!", this.pokemonName));
        }
    }

    @Override
	public String toString() {
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

    // Setters
    public void setCharge(boolean c) {
        this.charged = c;
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

    public boolean fainted() {
        return this.fainted;
    }

    public boolean charged() {
        return this.charged;
    }

    public Move[] moves() {
        return this.moves;
    }

}
