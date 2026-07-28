package project.game.move.moveactions;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;
import project.game.processors.WeatherProcessor;

public class MoveActionChangeWeather {

    private final EventManager eventManager;

    public MoveActionChangeWeather(EventManager e) {
        this.eventManager = e;
    }
    
    // Changes current weather
    public void changeWeather(WeatherEffect w) {
        EventData data  = this.eventManager.data;
        data.weatherChange = w;

        if (data.battleData.isCurrentWeather(data.weatherChange)) {
            throw new MoveInterruptedException(Move.FAILED);
        }

        WeatherProcessor weatherProcessor = new WeatherProcessor(data.battleData);
        weatherProcessor.changeWeather(w);
    }
}
