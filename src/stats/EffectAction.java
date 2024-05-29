package stats;

import battle.BattleLog;
import pokemon.Pokemon;

@FunctionalInterface
public interface EffectAction {
    // Effects
    public static final EffectAction BURN_ACT = (p, e) -> {
        BattleLog.add(String.format("%s is burning!", p.pokemonName()));
    };

    // Function
    public void act(Pokemon pokemon, Effect effect);

  
    // Methods
    public static EffectAction getAction(int id) {
        switch (id){
            case StatusCondition.BURN: return BURN_ACT;
            default: throw new IllegalArgumentException("Invalid id");
        }
    }
    

}
