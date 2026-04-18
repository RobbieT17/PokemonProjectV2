package project.game.utility;

import java.util.Random;

public interface  RandomValues {
        
    /**
     * @param min int
     * @param max int
     * @return random integer between max and min
     */
    public static int generateInt(int min, int max) {
        if (min > max) throw new IllegalArgumentException("Max must be greater than min");
        long seed = System.currentTimeMillis();
        return new Random(seed).nextInt((max - min) + 1) + min;
    }

    public static double randomDouble() {
        long seed = System.currentTimeMillis();
        return new Random(seed).nextDouble();
    }

    /**
     * Random chance.
     * @param prob the higher the value, the higher truth likelihood
     * @return True if the next random double is less than the value
     */
    public static boolean chance(double prob) {
        if (prob > 100 || prob <= 0) throw new IllegalArgumentException("Chance must be between 1-100%");
        long seed = System.currentTimeMillis();
        double chance = new Random(seed).nextDouble();
        return (chance <= prob * 0.01);
    }
}
