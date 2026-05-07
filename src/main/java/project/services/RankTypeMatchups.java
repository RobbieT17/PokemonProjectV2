package project.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.game.pokemon.stats.Type;

public class RankTypeMatchups {

    private static double[] defenseArr(Type t1, Type t2, boolean isSingleType) {
        return isSingleType 
        ? TypeMatchupHelpers.calculateMatchups(t1)
        : TypeMatchupHelpers.calculateMatchups(t1, t2);
    }

    private static double[] offenseArr(Type t1, Type t2, boolean isSingleType) {
        return isSingleType 
        ? TypeMatchupHelpers.calculateMatchupsOffense(t1)
        : TypeMatchupHelpers.calculateMatchupsOffense(t1, t2);
    }

    public static HashMap<String, TypeMatchup> initMatchupsMap() {
        // Ex. Key: "Fire-Fighting", Value: 0.95
        HashMap<String, TypeMatchup> map = new HashMap<>();

        Type[] typeArr = Type.values();
        int typeCount = typeArr.length;

        // Calculate matchups for each type combination
        for (int i = 0; i < typeCount; i++) {
            for (int j = i; j < typeCount; j++) {
                Type t1 = typeArr[i];
                Type t2 = typeArr[j];

                boolean isMonoType = i == j;

                String name = isMonoType 
                ? t1.name().toUpperCase()
                : t1.name().toUpperCase() + "-" + t2.name().toUpperCase();
   
                double[] defMatchups = defenseArr(t1, t2, isMonoType);
                double[] offMatchups = offenseArr(t1, t2, isMonoType);

                TypeMatchup matchup = new TypeMatchup(defMatchups, offMatchups);
                map.put(name, matchup);
            }
        }

        return map;
    }

    public static List<Map.Entry<String, TypeMatchup>> convertToList(HashMap<String, TypeMatchup> map, boolean isDef) {
        // Converts HashMap into a sorted list
        List<Map.Entry<String, TypeMatchup>> list = new ArrayList<>(map.entrySet());
        list.sort(
            Comparator.comparingDouble(entry -> isDef 
                ? entry.getValue().getAverageDefense() 
                : entry.getValue().getAverageOffense() * -1.0 // Reverts order
            )
        );

        return list;
    }

    public static String aggregateContent(List<Map.Entry<String, TypeMatchup>> list, boolean isDefense) {
        StringBuilder sb = new StringBuilder();

        int rank = 0;
        for (Map.Entry<String, TypeMatchup> map : list) {
            double value;
            double arr[];
            if (isDefense) {
                value = map.getValue().getAverageDefense();
                arr = map.getValue().getMatchupsDefense();
            }
            else{
                value = map.getValue().getAverageOffense();
                arr = map.getValue().getMatchupsOffense();
            }

            String keyValue = String.format("%d: %-18s%n\tAverage: %.3f%n%s%n", 
                ++rank, map.getKey(), value, TypeMatchupHelpers.showMatchups(arr));

            sb.append(keyValue);
        }

        return sb.toString();
    }

    public static void writeToFile(String content, String outputFile) {

        // Writes ranked list items to a file
        try {
            Files.writeString(Path.of(outputFile), content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("Successfully wrote ranked list to file: %s%n", outputFile);
    }

    public static void rankMatchups(HashMap<String, TypeMatchup> map, boolean cond) {
        List<Map.Entry<String, TypeMatchup>> list = convertToList(map, cond);
        String content = aggregateContent(list, cond);
        String outputFile = "src/main/resources/txt/ranked-matchups-" + (cond ? "defense" : "offense") + ".txt";

        writeToFile(content, outputFile);
    }

    public static void main(String[] args) {
        HashMap<String, TypeMatchup> typeMatchupMap = initMatchupsMap();
        rankMatchups(typeMatchupMap, true); // Ranks matchups defense-wise
        rankMatchups(typeMatchupMap, false); // Ranks matchups offense-wise
    }
}
