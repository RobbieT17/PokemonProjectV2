package project.game.battle;

import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokemon;

public class BattlePosition  {
    
    private final PokemonTrainer trainer;
    private final int id;

    private Pokemon currentPokemon; // Current Pokemon in battle
    private Pokemon prevPokemon; // Pokemon previously in battle

    // Shows this pokemon to the opponent.
    // Avoid exposes knowledge of a switch
    // to the opponent before the battle
    // round begins
    private Pokemon illusionPokemon; 

    // TODO: Add trap and screen effects

    public BattlePosition(PokemonTrainer pt, int id) {
        this.trainer = pt;
        this.id = id;
    }

    public void updateIllusion() {
        this.illusionPokemon = this.currentPokemon;
    }

    /**
     * Sets current Pokemon to p. Previous Pokemon field
     * is assigned old current Pokemon value.
     * @param p
     */
    public void setCurrentPokemon(Pokemon p) {
        this.prevPokemon = this.currentPokemon;
        this.currentPokemon = p;

        if (this.prevPokemon != null) {
            this.prevPokemon.setPosition(null);
        }

        if (this.currentPokemon != null) {
            p.setPosition(this);  
        }
        else { // Null pokemon
            this.updateIllusion();
        }
       
        
    }

     // Sends out a Pokemon to position pos, returns the Pokemon already in battle
    public void sendOutPokemon() {
        if (this.currentPokemon == null || !this.currentPokemon.getConditions().isSwitchedIn()) {
            return;
        }

        this.currentPokemon.getConditions().setSwitchedIn(false);
        this.updateIllusion();

        if (this.prevPokemon != null) {
            BattleLog.add("%s returns %s!", this.trainer, this.prevPokemon);
        }

        BattleLog.add("%s sends out %s!", this.trainer, this.currentPokemon);
    }

    public PokemonTrainer getTrainer() {return this.trainer;}
    public int getId() {return this.id;}
    public Pokemon getCurrentPokemon() {return this.currentPokemon;}
    public Pokemon getPrevPokemon() {return this.prevPokemon;}
    public Pokemon getIllusionPokemon() {return this.illusionPokemon;}

}
