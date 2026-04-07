package project.game.builders;

import java.util.ArrayList;
import java.util.Arrays;

import project.game.pokemon.PokemonType;
import project.game.pokemon.stats.Type;
import project.game.pokemon.stats.TypeName;

public class PokemonTypeBuilder implements Builder {

// Object Variables
    private Type primaryType = null;
    private Type secondaryType = null; // Optional (Not every Pokemon has a second type)

    private final ArrayList<TypeName> resistances = new ArrayList<>();
    private final ArrayList<TypeName> weaknesses = new ArrayList<>();
    private final ArrayList<TypeName> immunities = new ArrayList<>();

    @Override
    public void validateBuild() {
        if (this.primaryType == null) throw new IllegalStateException("Primary type has not be initialized");
    }

    /**
     * Converts to PokemonType
     * @throws IllegalStateException If required variables are not set
     * @return new PokemonType object
     */
    @Override
    public PokemonType build() {
        validateBuild();
        this.setTypeMatchups();

        return new PokemonType(
            this.primaryType,
            this.secondaryType, 
            this.resistances.toArray(TypeName[]::new),
            this.weaknesses.toArray(TypeName[]::new),
            this.immunities.toArray(TypeName[]::new)
            );
    }

// Setters
    public PokemonTypeBuilder setPrimaryType(Type t) {
        this.primaryType = t;
        return this;
    }

    public PokemonTypeBuilder setSecondaryType(Type t) {
        this.secondaryType = t;
        return this;
    }

    private PokemonTypeBuilder setTypeMatchups() {
        this.addMatchups(this.primaryType);
        this.addMatchups(this.secondaryType);
        return this;
    }

    // Adds a type's matchups to resistances/weaknesses/immunities
    private void addMatchups(Type t) {
        if (t == null) return;
 
        this.resistances.addAll(Arrays.asList(t.getResistances()));
        this.weaknesses.addAll(Arrays.asList(t.getWeaknesses()));
        this.immunities.addAll(Arrays.asList(t.getImmunities()));
    }

}
