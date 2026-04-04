package project.data;

import java.util.HashMap;
import java.util.List;

import project.game.builders.MoveBuilder;
import project.game.builders.PokemonBuilder;
import project.game.move.Move;
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
        return moveList.toArray(new String[0]);
    } 

    /**
     * Creates a new Pokemon instance using the pokemon entry info from the server database
     * The object created does not initalize the Pokemon's moves, this must be done 
     * afterwards.
     * @param pokemonName
     * @return new Pokemon Object
     */
    public Pokemon newPokemonInstance(String pokemonName) {
        PokemonData data = this.pokemonDatabase.get(pokemonName);

        if (data == null) {
            throw new IllegalArgumentException(pokemonName + " not found in pokemon database\n");
        }

        return new PokemonBuilder()
        .setPokedexNo(data.getId())
        .setName(data.getName())
        .setTypes(data.getType1(), data.getType2())
        .setWeight(data.getWeight())
        .setHp(data.getHp())
        .setStats(data.getAtk(), data.getDef(), data.getSpAtk(), data.getSpDef(), data.getSpd())
        .setLevel(Pokemon.DEFAULT_LEVEL)
        .build();
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

        return new MoveBuilder()
        .setId(data.getId())
        .setName(data.getName())
        .setType(data.getType())
        .setCategory(data.getCategory())
        .setPP(data.getPp())
        .setPower(data.getPow())
        .setAccuracy(data.getAcc())
        .setCritRatio(data.getCrit())
        .setPriority(data.getPriority())
        .setContact(data.getContact())
        .build();
    }

    
}
