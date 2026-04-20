package project.game.selectors;

import project.config.GameConfig;
import project.game.builders.PokemonTrainerBuilder;
import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokedex;
import project.game.pokemon.Pokemon;
import project.network.ClientHandler;
import project.network.Server;

public class PokemonTrainerSelector extends Selector {

    private final PokemonTrainerBuilder ptb;

    public PokemonTrainerSelector(ClientHandler c) {
        super(c);
        this.ptb = new PokemonTrainerBuilder();
    }

    private int getPokemonInput() {
        while (true) {
            try {
                this.client.writeToBuffer("Please select a Pokemon (%d/%d)", this.ptb.getParty().size(), PokemonTrainerBuilder.MAX_PARTY_CAPACITY);
                this.client.writeToBuffer(Pokedex.listAllPokemon());
                this.client.writeToBuffer("Input [-1] once done >> ");

                String input = this.client.readFromBuffer();
                int n = Integer.parseInt(input);

                if (n == -1) {
                    if (GameConfig.DOUBLES_MODE_ENABLED && this.ptb.getParty().size() < 2) { // Double battle requirements
                        this.client.writeToBuffer("Please add at least two pokemon.");
                    }
                    else if (this.ptb.getParty().isEmpty()) {
                        // Need at least one pokemon in the party
                        this.client.writeToBuffer("Please add at least one pokemon.");
                    }
                    else {
                        return -1;
                    }
                }
                else if (n < -1 || n >= Pokedex.values().length) {
                    this.client.writeToBuffer("Input out of bounds, try again");
                }
                else {
                    return n;
                }
               
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                this.client.writeToBuffer("Invalid input, try again.\n");
            }
        }
        
        
    }

    private Pokemon getPokemonPokedexData(int input) {
        if (input < 0) {
            return null;
        }

        // Maps input to enum
        Pokedex dex = Pokedex.values()[input];

        // Retrieve pokemon data from the database
        return Server.SERVER_DATA.newPokemonInstance(dex, Pokemon.DEFAULT_LEVEL);
    }

    /**
     * Gives the pokemon a nickname. If nothing is input, the 
     * default name is used.
     * @param pokemon
     */
    private void assignNickname(Pokemon pokemon) {
        this.client.writeToBuffer("Give your %s a nickname >> ", pokemon);
        String nickname = this.client.readFromBuffer();
        pokemon.setNickName(nickname.strip());
    }

    /**
     * Accesses the Move database then the player selects moves
     * to add to the Pokemon (Min: 2, Max: 2)
     * @param pokemon
     */
    private void assignMoves(Pokemon pokemon) {
        PokemonTrainerMoveSelector pokemonTrainerMoveSelector = new PokemonTrainerMoveSelector(this.client, pokemon);
        pokemonTrainerMoveSelector.constructPokemonMoves();
    }

    /**
     * Grants an ability to the pokemon
     * @param pokemon
     */
    private void assignAbility(Pokemon pokemon) {}

    /**
     * Grants an item to the pokemon
     * @param pokemon
     */
    private void assignHeldItem(Pokemon pokemon) {}

    /**
     * Constructs a Pokemon
     * Returning null signifies the caller function no more Pokemon will be created
     * @return
     */
    private Pokemon constructPokemon() {
        
        // Validates input
        int input = this.getPokemonInput();

        Pokemon pokemon = this.getPokemonPokedexData(input);

        if (pokemon == null) {
            return null;
        }
    
        // Gives a name to the pokemon 
        this.assignNickname(pokemon);
        
        // Give moves to the pokemon
        this.assignMoves(pokemon);

        // Give an ability to the pokemon
        this.assignAbility(pokemon);

        // (OPTION) Give an item to the pokemon
        this.assignHeldItem(pokemon);
    
        return pokemon;
    }

    /**
     * Creates a Pokemon trainer through user-commands. User must choose a name
     * then select up to six pokemon.
     *  */ 
    public PokemonTrainer select() {
        this.ptb.setName(this.client.clientName());

        while (this.ptb.getParty().size() < PokemonTrainerBuilder.MAX_PARTY_CAPACITY) {
            Pokemon pokemon = this.constructPokemon();

            // Adds Pokemon to the party
            if (pokemon != null) {
                this.ptb.addPokemon(pokemon);
                this.client.writeToBuffer("Added %s to your party!\n\n", pokemon);  
            }
            // Null return, player terminated selection process early
            else {
                break;
            }
        }

        PokemonTrainer pt = this.ptb.build();
        this.client.writeToBuffer("Your team has been built!");

        return pt;
    }

}
