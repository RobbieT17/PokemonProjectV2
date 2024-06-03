package battle;

public class PokemonCannotActException extends RuntimeException {
    public PokemonCannotActException(String message){
        super(message);
    }
}
