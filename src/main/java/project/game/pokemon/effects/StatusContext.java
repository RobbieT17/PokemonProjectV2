package project.game.pokemon.effects;

import project.game.move.Move;
import project.game.pokemon.Pokemon;

// Using this because some status condition manager class params have multiple inputs, wanted to reduce it to one class
public class StatusContext {

    public final Pokemon target;
    public Pokemon source; // optional
    public Move move;      // optional
    public int count;      // optional
  
    public StatusContext(Pokemon target) {
        this.target = target;
    }
}
