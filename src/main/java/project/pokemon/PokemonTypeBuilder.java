package project.pokemon;

import java.util.ArrayList;
import java.util.Arrays;

import project.stats.Type;
import project.utility.Builder;

public class PokemonTypeBuilder implements Builder {

// Object Variables
    private Type primaryType = null;
    private Type secondaryType = null; // Optional (Not every Pokemon has a second type)

    private final ArrayList<String> resistances = new ArrayList<>();
    private final ArrayList<String> weaknesses = new ArrayList<>();
    private final ArrayList<String> immunities = new ArrayList<>();


    @Override
    public void validBuild() {
        if (this.primaryType == null) throw new IllegalStateException("Primary type has not be initialized");
    }

    /**
     * Converts to PokemonType
     * @throws IllegalStateException If required variables are not set
     * @return new PokemonType object
     */
    @Override
    public PokemonType build() {
        validBuild();
        this.setTypeMatchups();

        return new PokemonType(
            this.primaryType,
            this.secondaryType, 
            this.resistances.toArray(String[]::new),
            this.weaknesses.toArray(String[]::new),
            this.immunities.toArray(String[]::new)
            );
    }

// Setters
    public PokemonTypeBuilder setPrimaryType(String t) {
        this.primaryType = Type.getType(t);
        return this;
    }

    public PokemonTypeBuilder setSecondaryType(String t) {
        this.secondaryType = Type.getType(t);
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
 
        this.resistances.addAll(Arrays.asList(t.resistances()));
        this.weaknesses.addAll(Arrays.asList(t.weaknesses()));
        this.immunities.addAll(Arrays.asList(t.immunities()));
    }

}
