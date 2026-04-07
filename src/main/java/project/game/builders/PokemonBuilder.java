package project.game.builders;


import project.game.pokemon.Pokemon;
import project.game.pokemon.PokemonType;
import project.game.pokemon.stats.HealthPoints;
import project.game.pokemon.stats.StatPoint;

// Class designed to create Pokemon objects efficiently
public class PokemonBuilder implements Builder{ 
    
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

    // Methods
    @Override
    public void validateBuild() {
        if (this.name == null) throw new IllegalStateException("Pokemon does not have name");
        if (this.types == null) throw new IllegalStateException("Types have not been initialized");
        if (this.pokedex == 0) throw new IllegalStateException("Pokedex ID number has not be initialized");
        if (this.hp == null) throw new IllegalStateException("Hp not initialized");
        if (this.atk == null) throw new IllegalStateException("Stats not initialized");
        if (this.weight == 0.0) throw new IllegalStateException("Weight not initialized");
    
    }

    /**
     * Converts into a Pokemon
     * @throws IllegalStateException If required variables are not set
     * @return a new Pokemon object
     */
    @Override
    public Pokemon build() {
        validateBuild();

        this.acc = new StatPoint(StatPoint.ACCURACY_NAME, StatPoint.ACCURACY, 100);
        this.eva = new StatPoint(StatPoint.EVASION_NAME, StatPoint.EVASION, 100);

        return new Pokemon(
            this.level,
            this.name,
            this.types, 
            this.pokedex, 
            this.weight,
            this.hp, 
            new StatPoint[] {this.atk, this.def, this.spAtk, this.spDef, this.spd, this.acc, this.eva}
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
        if (t2 == null) {
            return this.setTypes(t1);
        }

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
        this.hp = new HealthPoints(StatPoint.calculateHp(value, this.level));
        return this;
    }

    public PokemonBuilder setStats(int atk, int def, int spAtk, int spDef, int spd) {
        this.atk = new StatPoint(StatPoint.ATTACK_NAME, StatPoint.ATTACK, StatPoint.calculate(atk, this.level));
        this.def = new StatPoint(StatPoint.DEFENSE_NAME, StatPoint.DEFENSE, StatPoint.calculate(def, this.level));
        this.spAtk = new StatPoint(StatPoint.SPECIAL_ATTACK_NAME, StatPoint.SPECIAL_ATTACK, StatPoint.calculate(spAtk, this.level));
        this.spDef = new StatPoint(StatPoint.SPECIAL_DEFENSE_NAME, StatPoint.SPECIAL_DEFENSE, StatPoint.calculate(spDef, this.level));
        this.spd = new StatPoint(StatPoint.SPEED_NAME, StatPoint.SPEED, StatPoint.calculate(spd, this.level));
        return this;
    }

    public PokemonBuilder setWeight(double lbs) {
        this.weight = lbs;
        return this;
    }

}
