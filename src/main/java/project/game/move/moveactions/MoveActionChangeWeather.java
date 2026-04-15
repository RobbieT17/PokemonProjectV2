package project.game.move.moveactions;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;
import project.game.processors.WeatherProcessor;

public interface MoveActionChangeWeather extends MoveAction {
    
    // Changes current weather
    public static void changeWeather(EventManager eventManager, WeatherEffect w) {
        EventData data  = eventManager.data;
        data.weatherChange = w;

        if (data.battleData.isCurrentWeather(w)) {
            throw new MoveInterruptedException(Move.FAILED);
        }

        WeatherProcessor weatherProcessor = new WeatherProcessor(data.battleData);
        weatherProcessor.changeWeather(w);
    }
}
