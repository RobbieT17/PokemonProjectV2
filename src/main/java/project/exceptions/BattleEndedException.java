package project.exceptions;

import project.player.PokemonTrainer;

public class BattleEndedException extends RuntimeException {
    
    public BattleEndedException(PokemonTrainer winner, PokemonTrainer loser) {
        super(String.format("%s is out of Pokemon! %s wins the battle!", loser, winner));
    }

    public BattleEndedException(String message) {
        super(message);
    }
}
