package project.game.battle;

import project.game.exceptions.BattleEndedException;
import project.game.player.PokemonTrainer;
import project.game.processors.BattleProcessor;

/**
 * This class acts as an instance of a pokemon battle. It is responsible for tracking
 * all metrics retaining to the players and handling all battle logic. This class works
 * with the Server class to deliever communication between multiple players 
 * 
 * */ 
public class Battle {
// Object Variables

    // Players
    private final BattleData battleData;

// Constructor
    public Battle(PokemonTrainer p1, PokemonTrainer p2) {
        this.battleData = new BattleData(p1, p2);
    }

// Methods
    /**
     * Broadcasts the initial start message and declares
     * the pokemon sent out by each player.
     */
    public void initiatationMessage() {
        BattleLog.add("Let the battle begin!");
        BattleLog.out();
    }

    /**
     * Brodcasts a new switched-in Pokemon alert. This done by checking if the player's Pokemon
     * just switched in this turn.
     */
    public void switchInMessage() {
        for (BattlePosition pos : this.battleData.getAllBattlePositions()) {
            pos.sendOutPokemon();
        }

        BattleLog.out();
    }

    /**
     * Processes the round based on what each player chose.
     * For now, it just prints out which moves each pokemon used.
     * 
     * @returns A positive int if a win condition is met
     */
    public BattleStatus processRound() {
        BattleStatus status;
        try {
            new BattleProcessor(this.battleData).process();
            status = new BattleStatus(0);
            
        } catch (BattleEndedException e) {
            BattleLog.add(e.getMessage());
            status = new BattleStatus(1, e.getMessage());
        }
        
        BattleLog.out();
        return status;
     
    }
}
