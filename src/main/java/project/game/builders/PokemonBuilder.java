package project.game.builders;

import java.util.ArrayList;

import project.game.pokemon.Pokemon;
import project.game.pokemon.PokemonConditions;
import project.game.pokemon.PokemonType;
import project.game.pokemon.stats.HealthPoints;
import project.game.pokemon.stats.StatPoint;
import project.game.move.Move;

// Class designed to create Pokemon objects efficiently
public class PokemonBuilder implements Builder{ 

    private final static int MIN_MOVE_ALLOWED = 2; // Pokemon must have at least two moves
    private final static int MAX_MOVES_ALLOWED = 4; // Pokemon can have up to four moves

    private int level = Pokemon.DEFAULT_LEVEL;
    private String name = null;

    // Pokemon have one or two types
    private PokemonType types = null;

    // National Pokedex Number
    private int pokedex = 0;

    // Pokemon Stats
    private HealthPoints hp = null;

    private StatPoint atk = null;
    private StatPoint def = null;
    private StatPoint spAtk = null;
    private StatPoint spDef = null;
    private StatPoint spd = null;

    private StatPoint acc = null;
    private StatPoint eva = null;

    // Weight
    private double weight = 0.0;

    // Moves
    private final ArrayList<Move> moves = new ArrayList<>();

    // Methods
    private int calculateHp(int baseHp) {
		return (((2 * baseHp) * this.level) / 100) + this.level + 10;
	}

	private int calculate(int baseStat) {
		return ((baseStat * 2 * this.level) / 100) + 5;
	}

    // Has two or more of the same moves in the move array
    private boolean duplicateMove(Move m) {
        for (Move move : this.moves)
            if (move.getMoveID() == m.getMoveID()) return true;
        return false;
    }

    @Override
    public void validBuild() {
        if (this.name == null) throw new IllegalStateException("Pokemon does not have name");
        if (this.types == null) throw new IllegalStateException("Types have not been initialized");
        if (this.pokedex == 0) throw new IllegalStateException("Pokedex ID number has not be initialized");
        if (this.hp == null) throw new IllegalStateException("Hp not initialized");
        if (this.atk == null) throw new IllegalStateException("Stats not initialized");
        if (this.weight == 0.0) throw new IllegalStateException("Weight not initialized");
        if (this.moves.size() < MIN_MOVE_ALLOWED) throw new IllegalStateException("Move not initialized or not enough moves implemented");
    }

    /**
     * Converts into a Pokemon
     * @throws IllegalStateException If required variables are not set
     * @return a new Pokemon object
     */
    @Override
    public Pokemon build() {
        validBuild();

        this.acc = new StatPoint(StatPoint.ACCURACY_NAME, StatPoint.ACCURACY, 100);
        this.eva = new StatPoint(StatPoint.EVASION_NAME, StatPoint.EVASION, 100);

        return new Pokemon(
            this.level,
            this.name,
            this.types, 
            this.pokedex, 
            this.weight,
            this.hp, 
            new StatPoint[] {this.atk, this.def, this.spAtk, this.spDef, this.spd, this.acc, this.eva},
            this.moves.toArray(Move[]::new),
            new PokemonConditions()
            );
    }


// Setters
    public PokemonBuilder setLevel(int level) {
        this.level = level;
        return this;
    }

    public PokemonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PokemonBuilder setTypes(String t) {
        this.types = new PokemonTypeBuilder()
        .setPrimaryType(t)
        .build();
        return this;
    }

    public PokemonBuilder setTypes(String t1, String t2) {
        this.types = new PokemonTypeBuilder()
        .setPrimaryType(t1)
        .setSecondaryType(t2)
        .build();
        return this;
    }

    public PokemonBuilder setPokedexNo(int num) {
        this.pokedex = num;
        return this;
    }

    public PokemonBuilder setHp(int value) {
        this.hp = new HealthPoints(this.calculateHp(value));
        return this;
    }

    public PokemonBuilder setStats(int atk, int def, int spAtk, int spDef, int spd) {
        this.atk = new StatPoint(StatPoint.ATTACK_NAME, StatPoint.ATTACK, this.calculate(atk));
        this.def = new StatPoint(StatPoint.DEFENSE_NAME, StatPoint.DEFENSE, this.calculate(def));
        this.spAtk = new StatPoint(StatPoint.SPECIAL_ATTACK_NAME, StatPoint.SPECIAL_ATTACK, this.calculate(spAtk));
        this.spDef = new StatPoint(StatPoint.SPECIAL_DEFENSE_NAME, StatPoint.SPECIAL_DEFENSE, this.calculate(spDef));
        this.spd = new StatPoint(StatPoint.SPEED_NAME, StatPoint.SPEED, this.calculate(spd));
        return this;
    }

    public PokemonBuilder setWeight(double lbs) {
        this.weight = lbs;
        return this;
    }

    // Adds move to moveset, cannot have more than four moves or duplicates moves
    public PokemonBuilder addMove(Move m) {
        if (this.moves.size() == PokemonBuilder.MAX_MOVES_ALLOWED) throw new IllegalStateException("Cannot have more than four moves");
        if (this.duplicateMove(m)) throw new IllegalArgumentException(String.format("Duplicate move: %s", m));

        this.moves.add(m);
        return this;
    }

}
