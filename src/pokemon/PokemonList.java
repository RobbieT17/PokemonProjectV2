package pokemon;

import move.MoveList;
import stats.GameType;

// List of Pokemon (Listed in pokedex order)
public interface PokemonList {

    private static String defaultName(String a, String b) {
        return (b.equals("")) ? a : b;
    }

    public static Pokemon venusaur(String name) {
        return new PokemonBuilder()
        .setName(defaultName("Venusaur", name))
        .setTypes(GameType.GRASS, GameType.POISON)
        .setPokedexNo(3)
        .setWeight(100.0)
        .setHp(80)
        .setStats(82, 83, 100, 100, 80)
        .addMove(MoveList.petalBlizzard())
        .addMove(MoveList.leechSeed())
        .addMove(MoveList.sunnyDay())
        .addMove(MoveList.synthesis())
        .buildPokemon();
    }

    public static Pokemon charizard(String name) {
        return new PokemonBuilder()
        .setName(defaultName("Charizard", name))
        .setTypes(GameType.FIRE, GameType.FLYING)
        .setPokedexNo(6)
        .setWeight(199.5)
        .setHp(78)
        .setStats(84, 78, 109, 85, 100)
        .addMove(MoveList.flameCharge())
        .addMove(MoveList.dragonBreath())
        .addMove(MoveList.airSlash())
        .addMove(MoveList.scaryFace())
        .buildPokemon();
    }    


    public static Pokemon blastoise(String name) {
        return new PokemonBuilder()
        .setName(defaultName("Blastoise", name))
        .setTypes(GameType.WATER)
        .setPokedexNo(9)
        .setWeight(188.5)
        .setHp(79)
        .setStats(83, 100, 85, 105, 78)
        .addMove(MoveList.waterPulse())
        .addMove(MoveList.flashCannon())
        .addMove(MoveList.shellSmash())
        .addMove(MoveList.ironDefense())
        .buildPokemon();
    }

   
}
