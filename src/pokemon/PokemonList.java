package pokemon;

import move.MoveList;
import stats.Type;

public interface PokemonList {

    private static String defaultName(String a, String b) {
        return (b.equals("")) ? a : b;
    }
    
    public static Pokemon bulbasaur(String name) {
        return new PokemonBuilder()
        .setName(defaultName("Bulbasaur", name))
        .setTypes(Type.GRASS, Type.POISON)
        .setPokedexNo(1)
        .setWeight(15.2)
        .setHp(45)
        .setStats(49, 49, 65, 65, 45)
        .addMove(MoveList.solarBeam())
        .addMove(MoveList.growth())
        .addMove(MoveList.sleepPowder())
        .addMove(MoveList.sunnyDay())
        .buildPokemon();
    }

    public static Pokemon charmander(String name) {
        return new PokemonBuilder()
        .setName(defaultName("Charmander", name))
        .setTypes(Type.FIRE)
        .setPokedexNo(4)
        .setWeight(18.7)
        .setHp(39)
        .setStats(52, 43, 60, 50, 65)
        .addMove(MoveList.tackle())
        .addMove(MoveList.ember())
        .addMove(MoveList.smokescreen())
        .addMove(MoveList.sunnyDay())
        .buildPokemon();
    }


    public static Pokemon squirtle(String name) {
        return new PokemonBuilder()
        .setName(defaultName("Squirtle", name))
        .setTypes(Type.WATER)
        .setPokedexNo(7)
        .setWeight(19.8)
        .setHp(44)
        .setStats(48, 65, 50, 64, 43)
        .addMove(MoveList.takeDown())
        .addMove(MoveList.waterGun())
        .addMove(MoveList.tailWhip())
        .addMove(MoveList.rainDance())
        .buildPokemon();
    }
}
