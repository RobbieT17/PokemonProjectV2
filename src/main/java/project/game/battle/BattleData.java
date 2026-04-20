package project.game.battle;

import java.util.ArrayList;

import project.game.battle.Weather.WeatherEffect;
import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokemon;

public class BattleData {
    
    private final PokemonTrainer player1;
    private final PokemonTrainer player2;
    private final BattlePosition[] allBattlePositions;

    private Weather currenWeather;

    public BattleData(PokemonTrainer pt1, PokemonTrainer pt2) {
        this.player1 = pt1;
        this.player2 = pt2;
        this.allBattlePositions = this.initAllBattlePositions();
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
   
    // Setters
    public void setCurrenWeather(Weather currenWeather) {this.currenWeather = currenWeather;}

    // Getters
    public PokemonTrainer getPlayer1() {return this.player1;}
    public PokemonTrainer getPlayer2() {return this.player2;}
    public BattlePosition[] getAllBattlePositions() {return this.allBattlePositions;}
    public Weather getCurrenWeather() {return this.currenWeather;}
    
}
