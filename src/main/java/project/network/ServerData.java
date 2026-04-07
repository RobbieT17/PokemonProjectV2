package project.network;

import java.util.HashMap;
import java.util.List;

import project.data.MoveData;
import project.data.PokemonData;
import project.data.YamlLoader;
import project.game.move.Move;
import project.game.pokemon.Pokedex;
import project.game.pokemon.Pokemon;

public class ServerData {
    
    private final HashMap<String, PokemonData> pokemonDatabase;
    private final HashMap<String, MoveData> moveDatabase; 

    public ServerData() {
        this.pokemonDatabase = YamlLoader.initializePokemonData();
        this.moveDatabase = YamlLoader.initalizeMoveData();
    }

    public HashMap<String, PokemonData> getPokemonDatabase() {
        return this.pokemonDatabase;
    }

    public HashMap<String, MoveData> getMoveDatabase() {
        return this.moveDatabase;
    }

    public String[] getPokemonMovePool(String pokemonName) {
        List<String> moveList = this.pokemonDatabase.get(pokemonName).getMovePool();
        return moveList.toArray(String[]::new);
    } 

    /**
     * Creates a new Pokemon instance using the pokemon entry info from the server database
     * The object created does not initalize the Pokemon's moves, this must be done 
     * afterwards.
     * @param pokemonName
     * @return new Pokemon Object
     */
    public Pokemon newPokemonInstance(Pokedex pokemon, int level) {
        PokemonData data = this.pokemonDatabase.get(pokemon.name());

        if (data == null) {
            throw new IllegalArgumentException(pokemon.name() + " not found in pokemon database\n");
        }

        return new Pokemon(
            level, 
            data.getName(), 
            data.getPokemonType(), 
            data.getId(), 
            data.getWeight(), 
            data.getHealthPoints(level),
            data.getStats(level)
        );
    }

    /**
     * Create a new Move instance using the move entry info from the server database
     * @param moveName
     * @return New Move Object
     */
    public Move newMoveInstance(String moveName) {
        MoveData data = this.moveDatabase.get(moveName);

        if (data == null) {
            throw new IllegalArgumentException(moveName + " not found in pokemon database\n");
        }
        
        return new Move(
            data.getId(), 
            data.getName(), 
            data.getMoveType(), 
            data.getMoveCategory(), 
            data.getPowerPoints(), 
            data.getPower(), 
            data.getAccuracy(), 
            data.getMoveTarget(), 
            data.getCrit(), 
            data.getPriority(),
            data.getContact(), 
            data.getMultiHit(), 
            data.getAddEffects()
        );
    }

}
