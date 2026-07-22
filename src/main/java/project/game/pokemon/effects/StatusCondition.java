package project.game.pokemon.effects;

import java.util.function.Function;

import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;

public class StatusCondition extends Effect {

    public enum StatusConditionID {

        Burn(StatusConditionManager::addBurn, StatusConditionManager::condBurn, true),
        Freeze(StatusConditionManager::addFreeze, StatusConditionManager::condFreeze, true),
        Infect(StatusConditionManager::addInfect, StatusConditionManager::condInfect, true),
        Paralysis(StatusConditionManager::addParalysis, StatusConditionManager::condParalysis, true),
        Poison(StatusConditionManager::addPoison, StatusConditionManager::condPoison, true),
        Bad_Poison(StatusConditionManager::addBadPoison, StatusConditionManager::condPoison ,true),
        Sleep(StatusConditionManager::addSleep, StatusConditionManager::condSleep, true),
        Flinch(StatusConditionManager::addFlinch, StatusConditionManager::condFlinch),
        Bound(StatusConditionManager::addBound, null),
        Confusion(StatusConditionManager::addConfusion, null),  
        Forced_Move(StatusConditionManager::addForcedMove, null),
        Grounded(StatusConditionManager::addGrounded, null),
        Seeded(StatusConditionManager::addSeeded, null),
        Endure(StatusConditionManager::addEndure, null),
        Glitch(StatusConditionManager::addGlitch, null),
        Semi_Immune(StatusConditionManager::addSemiImmune, null),
        Vpn(StatusConditionManager::addVpn, null, 1),
        Link(StatusConditionManager::addLink, StatusConditionManager::condLink, 1),
        
        // No Function Provided
        Recharge(),
        Charge(),
        Rampage(),
        Focused(),
        Fly_State(),
        Dig_State(),
        Dive_State(),
        No_Invul(),
        Protect();

        private final Function<StatusContext, StatusCondition> applyFunc;
        private final Function<Pokemon, Boolean> condFunc;
        private final boolean isNonVolatile;
        private final int isPositive; // Non-zero is consider positive

        private StatusConditionID() {
            this.applyFunc = null;
            this.condFunc = null;
            this.isNonVolatile = false;
            this.isPositive = 0;
        }

        StatusConditionID(Function<StatusContext, StatusCondition> func, Function<Pokemon, Boolean> cond) {
            this.applyFunc = func;
            this.condFunc = cond;
            this.isNonVolatile = false;
            this.isPositive = 0;
        }
        
        StatusConditionID(Function<StatusContext, StatusCondition> func, Function<Pokemon, Boolean> cond, int pos) {
            this.applyFunc = func;
            this.condFunc = cond;
            this.isNonVolatile = false;
            this.isPositive = pos;
        }  

        StatusConditionID(Function<StatusContext, StatusCondition> func, Function<Pokemon, Boolean> cond, boolean nonVol) {
            this.applyFunc = func;
            this.condFunc = cond;
            this.isNonVolatile = nonVol;
            this.isPositive = 0;
        }  

    
        public StatusCondition addCondition(StatusContext c) {
            return this.applyFunc.apply(c);
        }

        public boolean canApplyCondition(Pokemon p) {
            boolean valid = true;

            if (this.condFunc != null) { // Some condition have special requirements to be applied
                valid = this.condFunc.apply(p);
            }

            // Validate based on status effect type 
            if (this.isNonVolatile) { // Can only have one non-volatile effect active at a time
                return !p.getConditions().hasPrimary() && valid;
            }
            else { // Can have multiple volatile effects, but not duplicates
                return  valid;
            }
        }

        public boolean isNonVolatile() {
            return this.isNonVolatile;
        }

        public boolean isPositive() {
            return this.isPositive != 0;
        }
    }




// Error Messages
    public static final String ID_ERR = "Invalid status condition ID: ";

    private final StatusConditionID id;


    public StatusCondition(Pokemon p, StatusConditionID id, EventID[] flags) {
        super(p, id.name(), flags);
        this.id = id;
    }


    @Override
    public void removeEffect() {
        this.getBearer().getEvents().removeEventListener(this.getFlags(), this.getEffectName());
    }

    public StatusConditionID getId() {return this.id;}     
}
