package project.exceptions;

/*
 * Exception is thrown when a Pokemon's HP drops to 0
 * The turn immediately, some effects such as recoil damage
 * still happen
 */
public class PokemonFaintedException extends RuntimeException {
    /**
     * Creates a new exception to be thrown
     * @param p Pokemon who fainted
     */
    public PokemonFaintedException() {
        super();
    }
}
