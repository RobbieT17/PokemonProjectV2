package project.battle;

import project.player.PokemonTrainer;

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
        BattleLog.add("%s sends out %s!", player1, player1.pokemonInBattle());
        BattleLog.add("%s sends out %s!", player2, player2.pokemonInBattle());
        BattleLog.out();
    }

    /**
     * Processes the round based on what each player chose.
     * For now, it just prints out which moves each pokemon used.
     */
    public void processRound() {
        BattleLog.add("%s used %s!", player1.pokemonInBattle(), player1.pokemonInBattle().moveSelected());
        BattleLog.add("%s used %s!", player2.pokemonInBattle(), player2.pokemonInBattle().moveSelected());
        BattleLog.out();
    }
}
