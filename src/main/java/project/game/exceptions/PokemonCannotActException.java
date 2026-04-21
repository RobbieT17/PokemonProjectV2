package project.game.exceptions;

public class PokemonCannotActException extends RuntimeException {
    /**
     * Creates a new exception to be thrown 
     * @param failMessage explains why the exception was thrown
     */
    public PokemonCannotActException() {
        super("");
    }

    public PokemonCannotActException(String message){
        super(message);
    }

    public PokemonCannotActException(String message, Object... args) {
        super(String.format(message, args));
    }
}
