package project.game.battle;

import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokemon;

public class BattlePosition {
    
    private final PokemonTrainer trainer;

    private Pokemon currentPokemon; // Current Pokemon in battle
    private Pokemon prevPokemon; // Pokemon previously in battle

    // TODO: Add trap and screen effects

    public BattlePosition(PokemonTrainer pt) {
        this.trainer = pt;
    }

    public void setCurrentPokemon(Pokemon p) {
        this.prevPokemon = this.currentPokemon;
        this.currentPokemon = p;
    }

    public PokemonTrainer getTrainer() {return this.trainer;}
    public Pokemon getCurrentPokemon() {return this.currentPokemon;}
    public Pokemon getPrevPokemon() {return this.prevPokemon;}
    
}
