package project.player;

import project.battle.BattleLog;
import project.pokemon.Pokemon;
import project.stats.StatDisplay;

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

    // Formats Pokemon team info with selection option
    private String listPokemonSelect() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.team.length; i++)
            sb.append(String.format("[%d] %s", i, StatDisplay.showPartyStats(this.team[i])));

        return sb.toString();
    }

    // True when all the trainer's Pokemon have fainted
    public boolean outOfPokemon() {
        for (Pokemon p : this.team)
            if (!p.conditions().fainted()) return false;
        return true;
    }

    // Number of Pokemon that are still alive (not fainted)
    public int pokemonAvailable() {
        int count = 0;

        for (Pokemon p : this.team)
            if (!p.conditions().fainted()) count++;

        return count;
    }

    // Displays trainer's Pokemon to the console
    public String showPokemon() {
        return new StringBuilder()
        .append(String.format("%s's Pokemon:%n", this))
        .append(this.listPokemon())
        .toString();
    }

    // Sends a Pokemon to the battle, the Pokemon cannot act until the next turn
    public void sendOut(Pokemon p) {
        this.pokemonInBattle = p;
        this.pokemonInBattle.conditions().setSwitchedIn(true);

        BattleLog.add("%n%s sends out %s!", this, p);
    }

    // Returns the Pokemon on the battle
    public void returns() {
        if (this.pokemonInBattle == null) return;
        
        BattleLog.add("%n%s returns %s!", this, this.pokemonInBattle);
        this.pokemonInBattle.backToTrainer();
        this.pokemonInBattle = null;
    }

    @Override
    public String toString() {
        return this.playerName;
    }

// Getters
    public String playerName() {
        return this.playerName;
    }

    public Pokemon[] team() {
        return this.team;
    }

    public Pokemon pokemonInBattle() {
        return this.pokemonInBattle;
    }
}
