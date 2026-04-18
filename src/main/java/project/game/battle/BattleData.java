package project.game.battle;

import project.game.battle.Weather.WeatherEffect;
import project.game.player.PokemonTrainer;

public class BattleData {
    
    private final PokemonTrainer player1;
    private final PokemonTrainer player2;

    private Weather currenWeather;

    public BattleData(PokemonTrainer pt1, PokemonTrainer pt2) {
        this.player1 = pt1;
        this.player2 = pt2;
        this.currenWeather = new Weather(WeatherEffect.Clear);
    }

    public boolean isCurrentWeather(WeatherEffect e) {
        return this.currenWeather.getWeatherEffect() == e;
    }

   
    // Setters
    public void setCurrenWeather(Weather currenWeather) {this.currenWeather = currenWeather;}

    // Getters
    public PokemonTrainer getPlayer1() {return this.player1;}
    public PokemonTrainer getPlayer2() {return this.player2;}
    public Weather getCurrenWeather() {return this.currenWeather;}
    
}
