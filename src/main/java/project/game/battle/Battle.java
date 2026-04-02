package project.game.battle;

import project.game.event.EventManager;
import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokemon;
import project.game.pokemon.PokemonActions;

/**
 * This class acts as an instance of a pokemon battle. It is responsible for tracking
 * all metrics retaining to the players and handling all battle logic. This class works
 * with the Server class to deliever communication between multiple players 
 * 
 * */ 
public class Battle {
// Object Variables

    // Players
    private final PokemonTrainer player1;
    private final PokemonTrainer player2;

// Constructor
    public Battle(PokemonTrainer p1, PokemonTrainer p2) {
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
        BattleLog.add("%s sends out %s!", this.player1, this.player1.getPokemonInBattle());
        BattleLog.add("%s sends out %s!", this.player2, this.player2.getPokemonInBattle());
        BattleLog.out();
    }

    /**
     * Processes the round based on what each player chose.
     * For now, it just prints out which moves each pokemon used.
     */
    public void processRound() {
        // Determine move order
        BattleActions battleActions = new BattleActions(this.player1.getPokemonInBattle(), this.player2.getPokemonInBattle());
        
        battleActions.constructTurnOrder();
        battleActions.processPokemonActions();
        battleActions.checkWinConditions();
        battleActions.processRoundEnd();

        BattleLog.out();
    }
}
