package project.debug;

import project.game.builders.PokemonTrainerBuilder;
import project.game.move.Move;
import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokedex;
import project.game.pokemon.Pokemon;
import project.network.Server;

public class MockPokemonTeam {

    private static Pokemon mockPokemon(Pokedex entry) {
        return Server.SERVER_DATA.newPokemonInstance(entry, Pokemon.DEFAULT_LEVEL);
    }

    private static Move mockMove(String moveName) {
        return Server.SERVER_DATA.newMoveInstance(moveName);
    }

    private static PokemonTrainer build1(String name) {
        Pokemon p1 = MockPokemonTeam.mockPokemon(Pokedex.Venusaur);
        Pokemon p2 = MockPokemonTeam.mockPokemon(Pokedex.Venusaur);

        p1.addMove(MockPokemonTeam.mockMove("Solar Beam"));
        p1.addMove(MockPokemonTeam.mockMove("Leech Seed"));
        p1.addMove(MockPokemonTeam.mockMove("Amnesia"));
        p1.addMove(MockPokemonTeam.mockMove("Poison Powder"));
        p1.setNickName("Vince");

        p2.addMove(MockPokemonTeam.mockMove("Take Down"));
        p2.addMove(MockPokemonTeam.mockMove("Leech Seed"));
        p2.addMove(MockPokemonTeam.mockMove("Amnesia"));
        p2.addMove(MockPokemonTeam.mockMove("Poison Powder"));
        p2.setNickName("Vance");

        PokemonTrainer pt = new PokemonTrainerBuilder()
        .setName(name)
        .addPokemon(p1)
        .addPokemon(p2)
        .build();

        p1.setOwner(pt);
        p2.setOwner(pt);

        return pt;
    }

    private static PokemonTrainer build2(String name) {
        Pokemon p1 = MockPokemonTeam.mockPokemon(Pokedex.Charizard);
        Pokemon p2 = MockPokemonTeam.mockPokemon(Pokedex.Blastoise);

        p1.addMove(MockPokemonTeam.mockMove("Sunny Day"));
        p1.addMove(MockPokemonTeam.mockMove("Inferno"));
        p1.addMove(MockPokemonTeam.mockMove("Fake Out"));
        p1.addMove(MockPokemonTeam.mockMove("Flamethrower"));
        p1.setNickName("Charlie");

        p2.addMove(MockPokemonTeam.mockMove("Fury Attack"));
        p2.addMove(MockPokemonTeam.mockMove("Icy Wind"));
        p2.addMove(MockPokemonTeam.mockMove("Bite"));
        p2.addMove(MockPokemonTeam.mockMove("Rain Dance"));
        p2.setNickName("Barry");

        PokemonTrainer pt = new PokemonTrainerBuilder()
        .setName(name)
        .addPokemon(p1)
        .addPokemon(p2)
        .build();

        p1.setOwner(pt);
        p2.setOwner(pt);

        return pt;

    }

    public static PokemonTrainer build(int id, String name) {
        return id == 0 ? build1(name) : build2(name);
    }
}
