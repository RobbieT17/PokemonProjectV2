package pokemon;

import move.MoveList;
import stats.Type;

// List of Pokemon (Listed in pokedex order)
public interface PokemonList {

    private static String defaultName(String a, String b) {
        return (b.equals("")) ? a : b;
    }

    public static Pokemon venusaur(String name) {
        return new PokemonBuilder()
        .setName(defaultName("Venusaur", name))
        .setTypes(Type.GRASS, Type.POISON)
        .setPokedexNo(3)
        .setWeight(100.0)
        .setHp(80)
        .setStats(82, 83, 100, 100, 80)
        .addMove(MoveList.vineWhip())
        .addMove(MoveList.solarBeam())
        .addMove(MoveList.waveCrash())
        .addMove(MoveList.sleepPowder())
        .buildPokemon();
    }

    public static Pokemon charizard(String name) {
        return new PokemonBuilder()
        .setName(defaultName("Charizard", name))
        .setTypes(Type.FIRE, Type.FLYING)
        .setPokedexNo(6)
        .setWeight(199.5)
        .setHp(78)
        .setStats(84, 78, 109, 85, 100)
        .addMove(MoveList.flareBlitz())
        .addMove(MoveList.dragonClaw())
        .addMove(MoveList.focusPunch())
        .addMove(MoveList.sandstorm())
        .buildPokemon();
    }    


    public static Pokemon blastoise(String name) {
        return new PokemonBuilder()
        .setName(defaultName("Blastoise", name))
        .setTypes(Type.WATER)
        .setPokedexNo(9)
        .setWeight(188.5)
        .setHp(79)
        .setStats(83, 100, 85, 105, 78)
        .addMove(MoveList.waterPulse())
        .addMove(MoveList.dive())
        .addMove(MoveList.swift())
        .addMove(MoveList.smackDown())
        .buildPokemon();
    }

   
}
