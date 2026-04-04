package project.game.pokemon;

public enum Pokedex {
    Venusaur(3),
    Charizard(6),
    Blastoise(9);

    private final int id;

    Pokedex(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }

    public static String listAllPokemon() {
        StringBuilder sb = new StringBuilder();

        for (Pokedex p : Pokedex.values()) {
            sb.append(String.format("[%d] %s\n", p.ordinal(), p.name()));
        } 

        return sb.toString();
    }


}
