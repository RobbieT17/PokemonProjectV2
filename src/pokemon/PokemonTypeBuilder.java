package pokemon;

import java.util.ArrayList;
import java.util.Arrays;
import stats.Type;

public class PokemonTypeBuilder {
    // Object Variables
    private Type primaryType = null;
    private Type secondaryType = null;

    private final ArrayList<String> resistances = new ArrayList<>();
    private final ArrayList<String> weaknesses = new ArrayList<>();
    private final ArrayList<String> immunities = new ArrayList<>();

    boolean hasSetMatchups = false;

    public PokemonTypeBuilder setPrimaryType(String t) {
        this.primaryType = Type.getType(t);
        return this;
    }

    public PokemonTypeBuilder setSecondaryType(String t) {
        this.secondaryType = Type.getType(t);
        return this;
    }

    public PokemonTypeBuilder setTypeMatchups() {
        if (this.primaryType == null) throw new IllegalStateException("primaryType must be initialized");
        
        addMatchups(this.primaryType);
        addMatchups(this.secondaryType);

        return this;
    }

    public PokemonType buildPokemonType() {
        if (this.hasSetMatchups == false) throw new IllegalStateException("Type matchups have not been initialized");
        if (this.immunities == null) throw new IllegalStateException("immunities has not been initialized");

        return new PokemonType(
            this.primaryType,
            this.secondaryType, 
            this.resistances.toArray(String[]::new),
            this.weaknesses.toArray(String[]::new),
            this.immunities.toArray(String[]::new)
            );
    }

    private void addMatchups(Type t) {
        if (t == null) return;
        
        this.hasSetMatchups = true;  
        this.resistances.addAll(Arrays.asList(t.resistances()));
        this.weaknesses.addAll(Arrays.asList(t.weaknesses()));
        this.immunities.addAll(Arrays.asList(t.immunities()));
    }

}
