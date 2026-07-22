package project.game.pokemon.effects;

import project.game.battle.BattlePosition;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusCondition.StatusConditionID;

// Using this because some status condition manager class params have multiple inputs, wanted to reduce it to one class
public class StatusContext {

    public final StatusConditionID id;
    public final Pokemon target;
    
    // optional
    public BattlePosition source;   // position on which initiate the effect (for seeded condition)
    public Pokemon target2;         // addition target (for linked condition)
    public boolean isSlave;         // for linked condition id
    public Move move;               // move used
    public int count;               // turn duration
    public int[] exceptions;        // moves that his through semi-immune states
    public String message;
  
    public StatusContext(StatusConditionID id, Pokemon target) {
        this.id = id;
        this.target = target;
    }

    /**
     * Attempts to apply status effect to target
     * @return True is effect was applied
     */
    public boolean applyEffectToTarget() {
        if (this.id.canApplyCondition(this.target)) {
            StatusCondition c = this.id.addCondition(this);
            this.target.getConditions().addCondition(c);
            return true;
        }
        return false;
    }
}
