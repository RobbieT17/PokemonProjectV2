package project.player;

import java.util.ArrayList;

import project.network.ClientHandler;
import project.pokemon.Pokedex;
import project.pokemon.Pokedex.PokedexEntry;
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
        if (this.party.size() == PokemonTrainerBuilder.MAX_PARTY_CAPACITY) {
            throw new IllegalStateException("Max party capacity reached");
        }
        
        this.party.add(p);
        return this;
    }

    /**
     * Creates a Pokemon trainer through user-commands. User must choose a name
     * then select up to six pokemon.
     *  */ 
    public static PokemonTrainer createPokemonTrainer(ClientHandler c) {
        PokemonTrainerBuilder ptb = new PokemonTrainerBuilder();
        ptb.name = c.clientName();
        
        // User selects up to six Pokemon
        while (ptb.party.size() < PokemonTrainerBuilder.MAX_PARTY_CAPACITY) {
            try {
                c.writeToBuffer("Please select a Pokemon (%d/%d)", ptb.party.size(), PokemonTrainerBuilder.MAX_PARTY_CAPACITY);
                c.writeToBuffer(Pokedex.all());
                c.writeToBuffer("Input [-1] once done >> ");

                String input = c.readFromBuffer();
                int n = Integer.parseInt(input);

                if (n == -1) {
                    if (ptb.party.isEmpty()) {
                        // Need at least one pokemon in the party
                        c.writeToBuffer("Please add at least one pokemon.");
                        continue;
                    }
                    break;
                }

                // Maps input to enum
                PokedexEntry pi = PokedexEntry.values()[n];
                
                // Gives a name to the pokemon 
                c.writeToBuffer("Give your %s a name >> ", pi.name());
                String name = c.readFromBuffer();
                Pokemon p = pi.newInstance(name);
            
                // Adds Pokemon to the party
                ptb.addPokemon(p);
                c.writeToBuffer("Added %s to your party!\n\n", p);

            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                c.writeToBuffer("Invalid input, try again.\n");
            }
        }
        
        PokemonTrainer pt = ptb.build();
        c.writeToBuffer("Your team has been built!");
           
        return pt;
    }

}
