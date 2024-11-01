package project.utility;

import java.util.Random;

public interface  RandomValues {
        
    /**
     * @param min int
     * @param max int
     * @return random integer between max and min
     */
    public static int generateInt(int min, int max) {
        if (min > max) throw new IllegalArgumentException("Max must be greater than min");
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static boolean chance(double value) {
        if (value > 100 || value <= 0) throw new IllegalArgumentException("Chance must be between 1-100%");
        return (new Random().nextDouble() <= value * 0.01);
    }
}
