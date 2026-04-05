package project.game.move.moveactions;

import project.game.battle.BattleField;
import project.game.battle.Weather;
import project.game.event.EventData;
import project.game.event.EventManager;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move;

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
