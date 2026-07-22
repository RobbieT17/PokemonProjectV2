package project.game.pokemon.effects;

import java.util.function.Function;

import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;

public class HeldItem extends Effect {

    public enum HeldItemID {
        Assault_Vest(HeldItemManager::assaultVest),
        Black_Sludge(HeldItemManager::blackSludge),
        Bomb_Surprise(HeldItemManager::bombSurprise),
        Choice_Band(HeldItemManager::choiceBand),
        Choice_Scarf(HeldItemManager::choiceScarf),
        Choice_Specs(HeldItemManager::choiceSpecs),
        Leftovers(HeldItemManager::leftovers),
        Muscle_Band(HeldItemManager::muscleBand),
        Rocky_Helmet(HeldItemManager::rockyHelmet),
        Wise_Glasses(HeldItemManager::wiseGlasses);

        private final Function<Pokemon, HeldItem> func;

        HeldItemID(Function<Pokemon, HeldItem> func) {
            this.func = func;
        }    

        public HeldItem apply(Pokemon p) {
            return this.func.apply(p);
        }
    }
    
    public HeldItem(Pokemon p, String name, EventID[] flags) {
        super(p, name, flags);
    }

    @Override
    public void removeEffect() {
        this.getBearer().getEvents().removeEventListener(this.getFlags(), this.getEffectName());
        this.getBearer().setItem(null);
    }

}
