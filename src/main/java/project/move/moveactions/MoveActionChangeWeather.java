package project.move.moveactions;

import project.battle.BattleField;
import project.battle.Weather;
import project.event.EventData;
import project.event.EventManager;
import project.exceptions.MoveInterruptedException;
import project.move.Move;

public interface MoveActionChangeWeather extends MoveAction {
    
    // Changes current weather
    public static void changeWeather(EventManager eventManager, int c) {
        EventData data  = eventManager.eventData;
        data.weatherChange = c;

        if (BattleField.currentWeather == c) {
            throw new MoveInterruptedException(Move.FAILED);
        }
        
        Weather.change(c);
    }
}
