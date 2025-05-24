package project.pokemon;

import project.move.MoveList;
import project.stats.Type;

// List of Pokemon (Listed in pokedex order)
public interface Pokedex {

    public enum PokedexEntry {
        VENUSAUR {
            @Override
            public Pokemon newInstance(String name) {
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
        }, 
 
        CHARIZARD {
            @Override
            public Pokemon newInstance(String name) {
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
        }, 
        BLASTOISE {
            @Override
            public Pokemon newInstance(String name) {
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
        };
        
        public abstract Pokemon newInstance(String name);
    };

  

    /**
     * If no name is provided, sets the pokemon's name to the default
     * @param a default name
     * @param b name provided
     */
    private static String defaultName(String a, String b) {
        return (b.equals("")) ? a : b;
    }

    // Prints out all Pokemon (id number and name)
    public static void printAll() {
        for (PokedexEntry p : PokedexEntry.values()) {
            System.out.printf("[%d] %s\n", p.ordinal(), p.name());
        } 
    }
    
    // Returns the list of all Pokemon
    public static String all() {
        StringBuilder sb = new StringBuilder();

        for (PokedexEntry p : PokedexEntry.values()) {
            sb.append(String.format("[%d] %s\n", p.ordinal(), p.name()));
        } 

        return sb.toString();
    }

    // public static Pokemon venusaur(String name) {
    //     return new PokemonBuilder()
    //     .setName(defaultName("Venusaur", name))
    //     .setTypes(Type.GRASS, Type.POISON)
    //     .setPokedexNo(3)
    //     .setWeight(100.0)
    //     .setHp(80)
    //     .setStats(82, 83, 100, 100, 80)
    //     .addMove(MoveList.solarBeam())
    //     .addMove(MoveList.petalDance())
    //     .addMove(MoveList.confuseRay())
    //     .addMove(MoveList.sleepPowder())
    //     .build();
    // }

    // public static Pokemon charizard(String name) {
    //     return new PokemonBuilder()
    //     .setName(defaultName("Charizard", name))
    //     .setTypes(Type.FIRE, Type.FLYING)
    //     .setPokedexNo(6)
    //     .setWeight(199.5)
    //     .setHp(78)
    //     .setStats(84, 78, 109, 85, 100)
    //     .addMove(MoveList.flareBlitz())
    //     .addMove(MoveList.fakeOut())
    //     .addMove(MoveList.willOWisp())
    //     .addMove(MoveList.sunnyDay())
    //     .build();
    // }    


    // public static Pokemon blastoise(String name) {
    //     return new PokemonBuilder()
    //     .setName(defaultName("Blastoise", name))
    //     .setTypes(Type.WATER)
    //     .setPokedexNo(9)
    //     .setWeight(188.5)
    //     .setHp(79)
    //     .setStats(83, 100, 85, 105, 78)
    //     .addMove(MoveList.waterPulse())
    //     .addMove(MoveList.furyAttack())
    //     .addMove(MoveList.swift())
    //     .addMove(MoveList.rainDance())
    //     .build();
    // }

   
}
