package project.pokemon;

import project.move.MoveList;
import project.stats.Type;

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
        .addMove(MoveList.solarBeam())
        .addMove(MoveList.petalDance())
        .addMove(MoveList.confuseRay())
        .addMove(MoveList.sleepPowder())
        .build();
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
        .addMove(MoveList.fakeOut())
        .addMove(MoveList.willOWisp())
        .addMove(MoveList.sunnyDay())
        .build();
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
        .addMove(MoveList.furyAttack())
        .addMove(MoveList.swift())
        .addMove(MoveList.rainDance())
        .build();
    }

   
}
