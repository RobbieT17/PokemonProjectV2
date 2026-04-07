package project.data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;


public class YamlLoader {

    public static HashMap<String, PokemonData> initializePokemonData() {
        try (InputStream input = YamlLoader.class.getResourceAsStream("/yaml/pokemon_data.yaml")) {

            if (input == null) {
                throw new RuntimeException("YAML file not found!");
            }

            Yaml yaml = new Yaml();
            PokemonDataList pokemonDataList = yaml.loadAs(
                    new InputStreamReader(input, StandardCharsets.UTF_8),
                    PokemonDataList.class
            );

            HashMap<String, PokemonData> map = new HashMap<>();
            for (PokemonData data : pokemonDataList.getPokemon()) {
                map.put(data.getName(), data);
            }

            System.out.println("Pokemon database initialized");
            return map;

        } catch (Exception e) {
            System.out.printf("[ERROR] Failed to load pokemon database: %s\n", e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static HashMap<String, MoveData> initalizeMoveData() {
        try (InputStream input = YamlLoader.class.getResourceAsStream("/yaml/move_data.yaml")) {

            if (input == null) {
                throw new RuntimeException("YAML file not found!");
            }

            Yaml yaml = new Yaml();
            MoveDataList moveDataList = yaml.loadAs(
                    new InputStreamReader(input, StandardCharsets.UTF_8),
                    MoveDataList.class
            );

            HashMap<String, MoveData> map = new HashMap<>();
            for (MoveData data : moveDataList.getMoves()) {
                map.put(data.getName(), data);
            }

            System.out.println("Move database initialized");
            return map;

        } catch (Exception e) {
            System.out.printf("[ERROR] Failed to load move database: %s\n", e.getMessage());
            System.exit(1);
        }
        return null;
    }
}
