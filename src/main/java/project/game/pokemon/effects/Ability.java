package project.game.pokemon.effects;

import java.util.function.Function;

import project.game.event.GameEvents.EventID;
import project.game.pokemon.Pokemon;

public class Ability extends Effect {

    public enum AbilityID {
        All_Or_Nothing(AbilityManager::allOrNothing),
        Blaze(AbilityManager::blaze),
        Chlorophyll(AbilityManager::chlorophyll),
        Overgrow(AbilityManager::overgrow),
        Rain_Dish(AbilityManager::rainDish),
        Simple(AbilityManager::simple),
        Solar_Power(AbilityManager::solarPower),
        Torrent(AbilityManager::torrent),
        Water_Absorb(AbilityManager::waterAbsorb);

        private final Function<Pokemon, Ability> func;

        AbilityID(Function<Pokemon, Ability> func) {
            this.func = func;
        }    

        public Ability apply(Pokemon p) {
            return this.func.apply(p);
        }

    }

 
    public Ability(Pokemon p, String name, EventID[] flags) {
        super(p, name, flags);
    }

    @Override
    public void removeEffect() {
        this.getBearer().getEvents().removeEventListener(this.getFlags(), this.getEffectName());
        this.getBearer().setAbility(null);
    }

}
