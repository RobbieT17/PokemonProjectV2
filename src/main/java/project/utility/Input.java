package project.utility;

public interface Input {
    
    /**
     * Checks for a numeric input
     * @param s string input
     * @return true if input is numeric
     */
    public static boolean isNumeric(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * Checks if a input matches a character
     * @param s string input
     * @param c character to match
     * @return true if input matches the character
     */
    public static boolean isChar(String s, char c) {
        return s.length() == 1 && s.toLowerCase().charAt(0) == c;
    }

}
