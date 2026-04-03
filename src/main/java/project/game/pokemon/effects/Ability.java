package project.game.pokemon.effects;

import project.game.pokemon.Pokemon;
import project.game.pokemon.stats.Effect;

public class Ability extends Effect {

// Public Class Variables
    public static final String BLAZE_ID = "Blaze";
    public static final String CHLOROPHYLL_ID = "Chlorophyll";
    public static final String OVERGROW_ID = "Overgrow";
    public static final String RAIN_DISH_ID = "Rain Dish"; 
    public static final String SOLAR_POWER_ID = "Solar Power";
    public static final String TORRENT_ID = "Torrent";
    public static final String WATER_ABSORB_ID = "Water Absorb";

// Object   
    public Ability(Pokemon p, String name, String[] flags) {
        super(p, name, flags);
    }

    @Override
    public void removeEffect() {
        this.getBearer().getEvents().removeEventListener(this.getFlags(), this.getEffectName());
        this.getBearer().setAbility(null);
    }
    
}
