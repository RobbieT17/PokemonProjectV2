package battle;

import pokemon.Pokemon;

public class PokemonFaintedException extends RuntimeException{
    public PokemonFaintedException(Pokemon p) {
        super(String.format("%s fainted!", p));
        p.setImmobilized(true);
        p.clearPrimaryCondition();
    }
}
