package battle;

import pokemon.Pokemon;

public class PokemonFaintsException extends RuntimeException  {
    private final Pokemon faintedPokemon;

    public PokemonFaintsException(Pokemon p) {
        super("fainted");
        this.faintedPokemon = p;
    }

    public String log() {
        return String.format("%s fainted!", faintedPokemon);
    }
}
