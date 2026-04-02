package project.game.player;

import project.game.pokemon.Pokemon;
import project.game.utility.StatDisplay;

public class PokemonTrainer {
        
// Object Variables
    private final String playerName; // Player name
    private final Pokemon[] team; // Pokemon Team to use in battle

    private Pokemon pokemonInBattle; // Current pokemon on the battlefield

// Constructor
    /**
     * Creates a new Pokemon trainer
     * @param name name given from user
     * @param team Pokemon team chosen by the user
     */
    public PokemonTrainer(String name, Pokemon[] team) {
        this.playerName = name;
        this.team = team;

        // Sets the pokemons' owner to the this trainer
        for (Pokemon p : team) p.setOwner(this);
    }

// Methods
   
    // Formats Pokemon team info
    private String listPokemon() {
        StringBuilder sb = new StringBuilder();

        for (Pokemon p : this.team) {
            sb.append(StatDisplay.showPartyStats(p));
        }

        return sb.toString();
    }

    // True when all the trainer's Pokemon have fainted
    public boolean outOfPokemon() {
        for (Pokemon p : this.team)
            if (!p.getConditions().isFainted()) return false;
        return true;
    }

    // Number of Pokemon that are still alive (not fainted)
    public int pokemonAvailable() {
        int count = 0;

        for (Pokemon p : this.team)
            if (!p.getConditions().isFainted()) count++;

        return count;
    }

    // Displays trainer's Pokemon to the console
    public String showPokemon() {
        return new StringBuilder()
        .append(String.format("%s's Pokemon:\n", this))
        .append(this.listPokemon())
        .toString();
    }

    // Sends a Pokemon to the battle, the Pokemon cannot act until the next turn
    public void sendOut(Pokemon p) {
        this.pokemonInBattle = p;
        this.pokemonInBattle.getConditions().setSwitchedIn(true);
    }

    // Returns the Pokemon on the battle
    public void returns() {
        if (this.pokemonInBattle == null) return;
    
        this.pokemonInBattle.backToTrainer();
        this.pokemonInBattle = null;
    }

    

    @Override
    public String toString() {
        return this.playerName;
    }

// Getters
    public String getPlayerName() {
        return this.playerName;
    }

    public Pokemon[] getTeam() {
        return this.team;
    }

    public Pokemon getPokemonInBattle() {
        return this.pokemonInBattle;
    }
}
