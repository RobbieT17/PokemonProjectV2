package project.game.player;

import project.game.battle.BattleLog;
import project.game.battle.BattlePosition;
import project.game.pokemon.Pokemon;
import project.game.utility.StatDisplay;

public class PokemonTrainer {
        
// Object Variables
    private final String playerName; // Player name
    private final Pokemon[] team; // Pokemon Team to use in battle
    private final BattlePosition battlePosition;

    private Pokemon showPokemon; // Pokemon in battle

// Constructor
    /**
     * Creates a new Pokemon trainer
     * @param name name given from user
     * @param team Pokemon team chosen by the user
     */
    public PokemonTrainer(String name, Pokemon[] team) {
        this.playerName = name;
        this.team = team;
        this.battlePosition = new BattlePosition(this);

        // Sets the pokemons' owner to the this trainer
        for (Pokemon p : team) {
            p.setOwner(this);
        }
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

    // Sends out a Pokemon, returns the Pokemon already in battle
    public void sendOutPokemon() {
        this.getPokemonInBattle().getConditions().setSwitchedIn(false);
        this.updateShowPokemon();

        if (this.getPokemonLastInBattle() != null) {
            BattleLog.add("%s returns %s!", this, this.getPokemonLastInBattle());
        }

        BattleLog.add("%s sends out %s!", this, this.getPokemonInBattle());
    }

    // Sets the current Pokemon in battle
    public void setPokemonInBattle(Pokemon p) {
        p.getConditions().setSwitchedIn(true);
        this.battlePosition.setCurrentPokemon(p);
    }

    // Returns the Pokemon on the battle
    public void returns() {
        if (this.battlePosition.getCurrentPokemon() == null) return;
    
        this.battlePosition.getCurrentPokemon().backToTrainer();
        this.battlePosition.setCurrentPokemon(null);
    }

    // Sets show Pokemon to current Pokemon in battle
    public void updateShowPokemon() {
        this.showPokemon = this.battlePosition.getCurrentPokemon();
    }

    // Shows the current Pokemon in battle (hides if the pokemon was switched out)
    // Only update this at the end of the turn to reflect 
    public Pokemon showPokemonInBattle() {
        return this.showPokemon;
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

    public BattlePosition getBattlePosition() {
        return this.battlePosition;
    }

    public Pokemon getPokemonInBattle() {
        return this.getBattlePosition().getCurrentPokemon();
    }

    public Pokemon getPokemonLastInBattle() {
        return this.getBattlePosition().getPrevPokemon();
    }
}
