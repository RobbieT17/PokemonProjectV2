package project.data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.yaml.snakeyaml.Yaml;

public class YamlLoader {
    public static void main(String[] args) {
        try (InputStream input = YamlLoader.class.getResourceAsStream("/yaml/move_data.yaml")) {

            if (input == null) {
                throw new RuntimeException("YAML file not found!");
            }

            Yaml yaml = new Yaml();
            MoveDataList moveList = yaml.loadAs(
                    new InputStreamReader(input, StandardCharsets.UTF_8),
                    MoveDataList.class
            );

            System.out.println(moveList.getMoves().get(0).getName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try (InputStream input = YamlLoader.class.getResourceAsStream("/yaml/pokemon_data.yaml")) {

            if (input == null) {
                throw new RuntimeException("YAML file not found!");
            }

            Yaml yaml = new Yaml();
            PokemonDataList pokemonDataList = yaml.loadAs(
                    new InputStreamReader(input, StandardCharsets.UTF_8),
                    PokemonDataList.class
            );

            System.out.println(pokemonDataList.getPokemon().get(0).getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
