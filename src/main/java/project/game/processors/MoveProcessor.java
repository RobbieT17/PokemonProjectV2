package project.game.processors;

import project.game.event.EventManager;
import project.game.move.Move;
import project.game.move.Movedex;
import project.game.move.Move.MoveCategory;
import project.game.move.Move.MoveTarget;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionAdditionalEffects;
import project.game.move.moveactions.MoveActionAttack;

public class MoveProcessor {
    
    private final EventManager eventManager;

    public MoveProcessor(EventManager e) {
        this.eventManager = e;
    }

    /**
     * Some moves require some more complex and sophisticated 
     * logic that is not currently possible with how the default processor
     * is programmed. For example, some moves calculate move power/accurary
     * under highly specific conditions. 
     */
    private void dynamicMoveProcess(Movedex moveEntry) {
        moveEntry.act(this.eventManager);
    }

    /**
     * The default move process. First checks which Pokemon is the target of a move
     * and then determines the move category. Physical and Special moves always deal
     * attack damage. Status moves usually apply some special effect to the target.
     * Applies any additional effects after the branching procedures.<br><br>
     * 
     * <b>IMPORTANT NOTE:</b> This is the function most moves will be ran through. Simple damaging attacks, 
     * stat changes, weather, and battlefield condition changes are done here. This
     * signifcantly reduces the need for each move to have a specific function just for it.
     */
    private void defaultMoveProcess() {
        Move move = this.eventManager.data.moveUsed;
       
        if (move.getMoveTarget() != MoveTarget.Self) {
            if (move.getCategory() == MoveCategory.Status) { 
                // Status do not deal direct damage to the target, but still have an accuracy check
                MoveActionAccuracy.rollForAccuracy(eventManager);
            }
            else {
                // Attacks the target Pokemon for damage
                MoveActionAttack.attackTarget(this.eventManager);
            }
        }

        // Applies additional effects
        MoveActionAdditionalEffects.applyAdditionEffects(eventManager);
    }

    /**
     * Process the move. Checks if there is a move specific function for the move used
     * in the <b>Movedex</b>. If not, the default move function is called.
     */
    public void processMove() {
        String moveName = this.eventManager.data.moveUsed.getMoveName();
        Movedex moveEntry;

        // Checks Movedex for specified function, if none moveEntry value is null
        try {
            moveEntry = Movedex.valueOf(moveName);
        } catch (Exception e) {
            moveEntry = null;
        }

        if (moveEntry != null) {
            this.dynamicMoveProcess(moveEntry);
        }
        else {
            this.defaultMoveProcess();
        }
        
    }
} 
