package player;

import java.util.ArrayList;
import pokemon.Pokemon;

public class PokemonTrainerBuilder {
    
    private String name = null;
    private final ArrayList<Pokemon> party = new ArrayList<>();

    public PokemonTrainer buildTrainer() {
        if (this.name == null) throw new IllegalArgumentException("Name not initialized");
        if (this.party.isEmpty()) throw new IllegalArgumentException("Party not initialized");

        return new PokemonTrainer(this.name, this.party.toArray(Pokemon[]::new));
    }

    public PokemonTrainerBuilder setName(String n) {
        this.name = n;
        return this;
    }

    public PokemonTrainerBuilder addPokemon(Pokemon p) {
        if (this.party.size() == 6) throw new IllegalStateException("Max party capacity reached");

        this.party.add(p);
        return this;
    }
}
