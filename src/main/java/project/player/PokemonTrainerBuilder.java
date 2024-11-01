package project.player;

import java.util.ArrayList;

import project.pokemon.Pokemon;
import project.utility.Builder;

// // Class designed to create PokemonTrainer objects efficiently
public class PokemonTrainerBuilder implements Builder {
    private static final int MAX_PARTY_CAPACITY = 6; // Trainers can have up to six Pokemon

    // Must Set
    private String name = null;
    private final ArrayList<Pokemon> party = new ArrayList<>();

    @Override
    public void validBuild() {
        if (this.name == null) throw new IllegalStateException("Trainer does not have a name");
        if (this.party.isEmpty()) throw new IllegalStateException(String.format("%s needs at least one pokemon in their party", this.name));
    }

    @Override
    public PokemonTrainer build() {
        validBuild();
        return new PokemonTrainer(this.name, this.party.toArray(Pokemon[]::new));
    }

    // Setters
    public PokemonTrainerBuilder setName(String n) {
        this.name = n;
        return this;
    }

    // Adds a Pokemon to the party, cannot go over the max allowed
    public PokemonTrainerBuilder addPokemon(Pokemon p) {
        if (this.party.size() == MAX_PARTY_CAPACITY) throw new IllegalStateException("Max party capacity reached");

        this.party.add(p);
        return this;
    }

}
