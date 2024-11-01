package project.applications;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import project.stats.Type;

public class TypeMatchCalculator {

    private static String listTypes(ArrayList<String> list, String prefix) {
        if (list.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();

        sb.append(prefix).append(": ");

        for (String value : list) 
            sb.append(value).append(", ");

        String s = sb.toString();
        return s.length() > 2 ? s.substring(0, s.length() - 2) + "\n" : s + "\n";    
    }

    private static String[] removeElement(String[] array, int value) {
        String[] newArray = new String[array.length - 1];

        for (int i = 0, j = 0; i < array.length; i++) 
            if (i != value) newArray[j++] = array[i];
            
        return newArray;
    }

    private static double effectiveness(Type t1, String t2) {
        return 
        Arrays.asList(t1.immunities()).contains(t2) 
        ? 0.0
        : Arrays.asList(t1.resistances()).contains(t2) 
            ? 0.5
            : Arrays.asList(t1.weaknesses()).contains(t2)
                ? 2.0
                : 1.0;
    }

    private static void displayOptions(String[] set, String prompt) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < set.length; i += 2) {
            
            sb.append(String.format("[%2d] %-20s", i, set[i].toUpperCase()));
    
            if (i + 1 < set.length) 
                sb.append(String.format("[%2d] %s", i + 1, set[i + 1].toUpperCase()));
            
            sb.append(System.lineSeparator());
        }

        System.out.println(sb.toString());
        System.out.print(prompt);
    }

    private static String selectOption(String[] set) {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        String type = null;

        while (!done) {
            try {
                int input = scanner.nextInt();
                type = set[input];
                done = true;
            } catch (Exception e) {
                System.out.println("Invalid input, try again");
                scanner.nextLine();
                done = false;
            }

        }
        return type;
    }

    private static double[] calculateMatchups(Type type) {
        double[] values = new double[Type.ALL_TYPES.length];

        for (int i = 0; i < values.length; i++) 
            values[i] = effectiveness(type, Type.ALL_TYPES[i]);

        return values;     
    }

    private static double[] calculateMatchups(Type type1, Type type2) {
        double[] values1 = calculateMatchups(type1);
        double[] values2 = calculateMatchups(type2);

        double[] combinedValues = new double[values1.length];

        for (int i = 0; i < values1.length; i++) combinedValues[i] = values1[i] * values2[i];   
        return combinedValues;
    }

    private static void showMatchups(double[] values) {
            
        ArrayList<String> immune = new ArrayList<>(); // x0
        ArrayList<String> heavyResist = new ArrayList<>(); // x0.25
        ArrayList<String> resist = new ArrayList<>(); // x0.5
        ArrayList<String> weak = new ArrayList<>();  // x2
        ArrayList<String> heavyWeak = new ArrayList<>(); // x4

        for (int i = 0; i < values.length; i++) {
            switch ((int) (values[i] * 100)) {
                case 0 -> immune.add(Type.ALL_TYPES[i].toUpperCase());
                case 25 -> heavyResist.add(Type.ALL_TYPES[i].toUpperCase());
                case 50 -> resist.add(Type.ALL_TYPES[i].toUpperCase());
                case 200 -> weak.add(Type.ALL_TYPES[i].toUpperCase());
                case 400 -> heavyWeak.add(Type.ALL_TYPES[i].toUpperCase());
            }
        }

        System.out.println(new StringBuilder()
        .append(listTypes(immune, "[x0]"))
        .append(listTypes(heavyResist, "[x0.25]"))
        .append(listTypes(resist, "[x0.5]"))
        .append(listTypes(weak, "[x2]"))
        .append(listTypes(heavyWeak, "[x4]"))
        .toString());
         
    }

    private static Type selectType(String[] set, String prompt) {
        displayOptions(set, prompt);
        return Type.getType(selectOption(set));   
    }
    

    public static void main(String[] args) {
        Type type1 = selectType(Type.ALL_TYPES, "Select First Type: ");
        Type type2 = selectType(removeElement(Type.ALL_TYPES, type1.typeId()), "Select Second Type: ");
            
        System.out.printf("%nType: %s-%s%n%n", type1.typeName().toUpperCase(), type2.typeName().toUpperCase());
        showMatchups(calculateMatchups(type1, type2));
    }
}
