package project.game.processors;

import project.game.battle.BattleData;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.PokemonFaintedException;
import project.game.pokemon.Pokemon;

public class EndRoundProcessor implements Processor {
    
    private final BattleData battleData;
    private final Pokemon pokemon;

    public EndRoundProcessor(BattleData data, Pokemon p) {
        this.battleData = data;
        this.pokemon = p;
    }

    @Override
    public void process() {
        if (this.pokemon.getConditions().isFainted()) {
            return;
        }

        try {
            this.pokemon.endOfRoundReset();

            new WeatherProcessor(this.battleData)
            .pokemonTakeWeatherDamage(this.pokemon);

            EventManager eventManager = new EventManager(this.battleData, this.pokemon);
            eventManager.updateEventMaps();
            eventManager.notifyUserPokemon(EventID.WEATHER_EFFECT);
            eventManager.notifyUserPokemon(EventID.PRIMARY_STATUS_AFTER);
            eventManager.notifyUserPokemon(EventID.STATUS_AFTER);
            eventManager.notifyUserPokemon(EventID.END_OF_ROUND);

        } catch (PokemonFaintedException e) { 
            // Some effects might cause the Pokemon to faint, skip to next Pokemon if so
        }
    }
}
