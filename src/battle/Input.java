package battle;

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

}
