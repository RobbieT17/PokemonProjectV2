package project.data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.yaml.snakeyaml.Yaml;

public class YamlLoader {
     public static void main(String[] args) {
        Yaml yaml = new Yaml();

        try (InputStream input = YamlLoader.class
                .getClassLoader()
                .getResourceAsStream("yaml/move_data.yaml")) {

            if (input == null) {
                throw new RuntimeException("YAML file not found");
            }

            MoveData moveData = yaml.loadAs(
                    new InputStreamReader(input, StandardCharsets.UTF_8),
                    MoveData.class
            );

            System.out.println(moveData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
