package project.services;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import project.game.pokemon.stats.Type;
import project.game.pokemon.stats.TypeName;

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

    private static TypeName[] removeElement(Type[] array, Type value) {
        TypeName[] newArray = new TypeName[array.length - 1];
        int c = 0;

        for (Type t : array) {
            if (t != value) {
                newArray[c++] = t.toTypeName();
            }
        }
            
        return newArray;
    }

    private static double effectiveness(Type t1, Type t2) {
        return 
        Arrays.asList(t1.getImmunities()).contains(t2.toTypeName()) 
        ? 0.0
        : Arrays.asList(t1.getResistances()).contains(t2.toTypeName()) 
            ? 0.5
            : Arrays.asList(t1.getWeaknesses()).contains(t2.toTypeName())
                ? 2.0
                : 1.0;
    }

    private static void displayOptions(TypeName[] set, String prompt) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < set.length; i += 2) {
            
            sb.append(String.format("[%2d] %-20s", i, set[i].name().toUpperCase()));
    
            if (i + 1 < set.length) 
                sb.append(String.format("[%2d] %s", i + 1, set[i + 1].name().toUpperCase()));
            
            sb.append(System.lineSeparator());
        }

        System.out.println(sb.toString());
        System.out.print(prompt);
    }

    private static Type selectOption(TypeName[] set) {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        TypeName typeName = null;

        while (!done) {
            try {
                int input = scanner.nextInt();
                typeName = set[input];
                done = true;
            } catch (Exception e) {
                System.out.println("Invalid input, try again");
                scanner.nextLine();
                done = false;
            }

        }
        return Type.valueOf(typeName.name());
    }

    private static double[] calculateMatchups(Type type) {
        double[] values = new double[Type.values().length];

        for (Type t : Type.values()) 
            TypeMatchCalculator.effectiveness(type, Type.valueOf(t.name()));

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
                case 0 -> immune.add(Type.values()[i].name().toUpperCase());
                case 25 -> heavyResist.add(Type.values()[i].name().toUpperCase());
                case 50 -> resist.add(Type.values()[i].name().toUpperCase());
                case 200 -> weak.add(Type.values()[i].name().toUpperCase());
                case 400 -> heavyWeak.add(Type.values()[i].name().toUpperCase());
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

    private static Type selectType(TypeName[] set, String prompt) {
        displayOptions(set, prompt);
        return selectOption(set);   
    }
    
    public static void main(String[] args) {
        Type type1 = selectType(TypeName.values(), "Select First Type: ");
        Type type2 = selectType(removeElement(Type.values(), type1), "Select Second Type: ");
            
        System.out.printf("%nType: %s-%s%n%n", type1.name().toUpperCase(), type2.name().toUpperCase());
        showMatchups(calculateMatchups(type1, type2));
    }
}
