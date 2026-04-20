package project.game.player;

import project.config.GameConfig;
import project.game.battle.BattlePosition;
import project.game.pokemon.Pokemon;
import project.game.utility.StatDisplay;

public class PokemonTrainer {
        
// Object Variables
    private final String playerName; // Player name
    private final Pokemon[] team; // Pokemon Team to use in battle
    private final BattlePosition[] battlePositions; // Slots for Pokemon in battle

// Constructor
    /**
     * Creates a new Pokemon trainer
     * @param name name given from user
     * @param team Pokemon team chosen by the user
     */
    public PokemonTrainer(String name, Pokemon[] team) {
        this.playerName = name;
        this.team = team;
        this.battlePositions = this.initBattlePositions();

        // Sets the pokemons' owner to the this trainer
        for (Pokemon p : team) {
            p.setOwner(this);
        }
    }

// Methods
    // Configures number of battle positions based on the gamemode (Singles/Doubles)
    private BattlePosition[] initBattlePositions() {
        return GameConfig.DOUBLES_MODE_ENABLED
        ? new BattlePosition[] {new BattlePosition(this, 0), new BattlePosition(this, 1)}
        : new BattlePosition[] {new BattlePosition(this, 0)};
    }


// Boolean Methods
    // True when all the trainer's Pokemon have fainted
    public boolean isOutOfPokemon() {
        for (Pokemon p : this.team)
            if (!p.getConditions().isFainted()) return false;
        return true;
    }

    /**
     * Checks all positions on the field for a Pokemon.
     * @param p search Pokemon
     * @return True if the Pokemon reference matches one 
     * of the positions' current Pokemon
     */
    public boolean isPokemonInBattle(Pokemon p) {
        for (BattlePosition pos : this.battlePositions) {
            if (p == pos.getCurrentPokemon()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if a trainer can send 
     * another Pokemon onto the battlefield
     * 
     * Done by seeing if the trainer has more
     * Pokemon avaiable then battle position slot
     * 
     * @param pos postion id
     * @param override ignores null position check
     * @return
     */
    public boolean canSelectPokemon(int pos, boolean override) {
        // Check for an empty slot
        boolean emptyPosition = this.battlePositions[pos].getCurrentPokemon() == null;

        if (override) {
            emptyPosition = true;
        }

        // Check if player has more 

        return emptyPosition && this.pokemonAvailable() > 0;
    }
// Other Methods
    // Formats Pokemon team info
    private String listPokemon() {
        StringBuilder sb = new StringBuilder();

        for (Pokemon p : this.team) {
            sb.append(StatDisplay.showPartyStats(p));
        }

        return sb.toString();
    }

    public String displayPokemonInBattle(String prefix) {
        StringBuilder sb = new StringBuilder();

        for (BattlePosition pos : this.battlePositions) {
            if (pos.getCurrentPokemon() != null) {
                sb.append(StatDisplay.showPartyStats(pos.getIllusionPokemon(), prefix));
            }

        }
        return sb.toString();
    }


    // Number of Pokemon that are still alive and not in battle
    public int pokemonAvailable() {
        int count = 0;

        for (Pokemon p : this.team)
            if (!p.getConditions().isFainted() && !this.isPokemonInBattle(p)) {
                count++;
            } 
                

        return count;
    }

    // Displays trainer's Pokemon to the console
    public String showPokemon() {
        return new StringBuilder()
        .append(String.format("%s's Pokemon:\n", this))
        .append(this.listPokemon())
        .toString();
    }

    // Gets the pokemon currently in position pos
    public Pokemon getPokemonInBattle(int pos) {
        return this.getBattlePositions()[pos].getCurrentPokemon();
    }

    // Gets the pokemon previously in position pos
    public Pokemon getPokemonLastInBattle(int pos) {
        return this.getBattlePositions()[pos].getPrevPokemon();
    }

    public Pokemon getShowPokemon(int pos) {
       return this.battlePositions[pos].getIllusionPokemon();
    }

    // Updates the Pokemon display shown to opponent
    public void updateShowPokemon() {
        for (BattlePosition pos : this.battlePositions) {
            pos.updateIllusion();
        }
    }  

    // Sets the current Pokemon in battle
    public void setPokemonInBattle(Pokemon p, int pos) {
        p.getConditions().setSwitchedIn(true);
        this.battlePositions[pos].setCurrentPokemon(p);
    }

    // Returns the Pokemon on the battle
    public void returns(int pos) {
        if (this.battlePositions[pos].getCurrentPokemon() == null) return;
    
        this.battlePositions[pos].getCurrentPokemon().backToTrainer();
        this.battlePositions[pos].setCurrentPokemon(null);
    }

    @Override
    public String toString() {
        return this.playerName;
    }

// Getters
    public String getPlayerName() { return this.playerName;}
    public Pokemon[] getTeam() {return this.team;}
    public BattlePosition[] getBattlePositions() {return this.battlePositions;}
}
