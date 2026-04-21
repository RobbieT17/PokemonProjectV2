package project.game.pokemon.effects;

import project.game.battle.BattlePosition;
import project.game.move.Move;
import project.game.pokemon.Pokemon;

// Using this because some status condition manager class params have multiple inputs, wanted to reduce it to one class
public class StatusContext {

    public final Pokemon target;
    
    // optional
    public BattlePosition source;   // position on which initiate the effect (for seeded condition)
    public Move move;               // move used
    public int count;               // turn duration
    public int[] exceptions;        // moves that his through semi-immune states
    public String message;
  
    public StatusContext(Pokemon target) {
        this.target = target;
    }
}
