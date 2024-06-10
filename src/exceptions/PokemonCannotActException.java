package exceptions;

public class PokemonCannotActException extends RuntimeException {
    /**
     * Creates a new exception to be thrown 
     * @param message explains why the exception was thrown
     */
    public PokemonCannotActException() {
        super("");
    }

    public PokemonCannotActException(String message){
        super(message);
    }
}
