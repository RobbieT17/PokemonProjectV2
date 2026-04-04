package project.devtools;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MoveYamlSorter {

    public static void sortMovesById(String inputFile, String outputFile) throws Exception {
        Yaml yaml = new Yaml();

        Map<String, Object> data;
        try (InputStream in = new FileInputStream(inputFile)) {
            data = yaml.load(in);
        }

        List<Map<String, Object>> moves =
                (List<Map<String, Object>>) data.get("moves");

        if (moves == null) {
            throw new IllegalStateException("No 'moves' section found.");
        }

        printDuplicateMoves(moves);

        // Sort by ID, then by name if IDs match
        moves.sort(
                Comparator
                        .comparingInt((Map<String, Object> m) -> ((Number) m.get("id")).intValue())
                        .thenComparing(m -> String.valueOf(m.get("name")))
        );

        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);

        Yaml outputYaml = new Yaml(options);

        StringWriter writer = new StringWriter();
        outputYaml.dump(data, writer);

        String formattedYaml = addBlankLinesBetweenMoves(writer.toString());

        Files.writeString(Path.of(outputFile), formattedYaml);

        System.out.println("Moves sorted successfully.");
    }

    private static void printDuplicateMoves(List<Map<String, Object>> moves) {
        Map<Integer, List<String>> duplicates = new TreeMap<>();

        for (Map<String, Object> move : moves) {
            int id = ((Number) move.get("id")).intValue();
            String name = String.valueOf(move.get("name"));

            duplicates.computeIfAbsent(id, k -> new ArrayList<>()).add(name);
        }

        boolean foundDuplicates = false;

        for (Map.Entry<Integer, List<String>> entry : duplicates.entrySet()) {
            if (entry.getValue().size() > 1) {
                foundDuplicates = true;
                System.out.println("Duplicate ID " + entry.getKey() +
                        " -> " + entry.getValue());
            }
        }

        if (!foundDuplicates) {
            System.out.println("No duplicate IDs found.");
        }
    }

    private static String addBlankLinesBetweenMoves(String yamlText) {
    String[] lines = yamlText.split("\\R");
    StringBuilder sb = new StringBuilder();

    boolean firstMoveFound = false;

    for (String line : lines) {
        String trimmed = line.trim();

        // Detect any list item like "- id:"
        if (trimmed.startsWith("- id:")) {
            if (firstMoveFound) {
                sb.append(System.lineSeparator());
            }
            firstMoveFound = true;
        }

        sb.append(line).append(System.lineSeparator());
    }

    return sb.toString();
}


    public static void main(String[] args) throws Exception {
        sortMovesById(
            "src/main/resources/yaml/move_data.yaml",
            "src/main/resources/yaml/move_data_sorted.yaml"
        );
    }
}
