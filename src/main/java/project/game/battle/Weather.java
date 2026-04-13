package project.game.battle;

import project.game.utility.Counter;

public class Weather {

    public enum WeatherEffect {Clear, Sunny, Rain, Sandstorm, Hail}

    private final WeatherEffect weatherEffect;
    private final Counter counter;

    public Weather(WeatherEffect e) {
        this.weatherEffect = e;
        this.counter = e != WeatherEffect.Clear ? new Counter(5) : new Counter();
    }

    public WeatherEffect getWeatherEffect() {
        return this.weatherEffect;
    }

    public Counter getCounter() {
        return this.counter;
    }

} 
