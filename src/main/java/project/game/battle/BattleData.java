package project.game.battle;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventData;
import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokemon;
import project.network.Server;

public class BattleData {
    
    private final PokemonTrainer player1;
    private final PokemonTrainer player2;
    private final BattlePosition[] allBattlePositions;
    private final Deque<EventData> dataQueue; // 

    private Weather currenWeather;
    

    public BattleData(PokemonTrainer pt1, PokemonTrainer pt2) {
        this.player1 = pt1;
        this.player2 = pt2;
        this.allBattlePositions = this.initAllBattlePositions();
        this.dataQueue = new LinkedList<>();
        this.currenWeather = new Weather(WeatherEffect.Clear);
    }

    public boolean isCurrentWeather(WeatherEffect e) {
        return this.currenWeather.getWeatherEffect() == e;
    }

    // Methods
    private BattlePosition[] initAllBattlePositions() {
        ArrayList<BattlePosition> list = new ArrayList<>();

        for (BattlePosition pos : this.player1.getBattlePositions()) {
            list.add(pos);
        }
        for (BattlePosition pos : this.player2.getBattlePositions()) {
            list.add(pos);
        }
        
        return list.toArray(BattlePosition[]::new);
    }

    // Gets all current Pokemon on the field
    public Pokemon[] getAllPokemonInBattle() {
        ArrayList<Pokemon> list = new ArrayList<>();

        for (BattlePosition pos : this.allBattlePositions) {
            if (pos.getCurrentPokemon() != null) { // Ignore null positions
               list.add(pos.getCurrentPokemon()); 
            }
        }
        
        return list.toArray(Pokemon[]::new);
    }

    public void logEventData() {
        int i = 0;
        while(!this.dataQueue.isEmpty()) {
            EventData data = this.dataQueue.pop();
            Server.log("EventData (id: %d)\n.......................................\n%s", i++, data);
        }
        Server.log("------------------------------------------------------------");
    }
   
    // Setters
    public void setCurrenWeather(Weather currenWeather) {this.currenWeather = currenWeather;}
    public void addEventData(EventData data) {this.dataQueue.offer(data);}

    // Getters
    public PokemonTrainer getPlayer1() {return this.player1;}
    public PokemonTrainer getPlayer2() {return this.player2;}
    public BattlePosition[] getAllBattlePositions() {return this.allBattlePositions;}
    public Deque<EventData> getDateQueue() {return this.dataQueue;}
    public Weather getCurrenWeather() {return this.currenWeather;}
    
}
