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

    private final PokemonTrainer player1;
    private final PokemonTrainer player2;

// Constructor
    public Battle(PokemonTrainer p1, PokemonTrainer p2) {
        this.battleData = new BattleData(p1, p2);
        this.player1 = p1;
        this.player2 = p2;
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
     * Brodacts a new switched in Pokemon. This done by checking if the player's Pokemon
     * just switched in this turn.
     */
    public void switchInMessage() {
        if (this.player1.getPokemonInBattle().getConditions().isSwitchedIn()) {
            BattleLog.add("%s sends out %s!", this.player1, this.player1.getPokemonInBattle());
        }
        if (this.player2.getPokemonInBattle().getConditions().isSwitchedIn()) {
            BattleLog.add("%s sends out %s!", this.player2, this.player2.getPokemonInBattle());
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
            BattleProcessor battleProcessor = new BattleProcessor(this.battleData);
        
            battleProcessor.constructTurnOrder();
            battleProcessor.processPokemonActions();
            battleProcessor.checkWinConditions();
            battleProcessor.processRoundEnd();

            
            status = new BattleStatus(0);
        } catch (BattleEndedException e) {
            BattleLog.add(e.getMessage());
            status = new BattleStatus(1, e.getMessage());
        }
        
        BattleLog.out();
        return status;
     
    }
}
