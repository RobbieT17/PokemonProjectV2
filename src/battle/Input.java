package battle;

import java.util.Random;

public class Input {
    
    public static boolean isNumeric(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean isChar(String s, char c) {
        return s.length() == 1 && s.toLowerCase().charAt(0) == c;
    }


    
    public static int randomInt(int min, int max) {
        if (min > max) throw new IllegalArgumentException("Max must be greater than min");
        return new Random().nextInt((max - min) + 1) + min;
    }
}
