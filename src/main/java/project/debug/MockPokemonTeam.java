package project.debug;

import project.game.builders.PokemonTrainerBuilder;
import project.game.move.Move;
import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokedex;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.AbilityManager.AbilityID;
import project.game.pokemon.effects.HeldItemManager.HeldItemID;
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
        Pokemon p3 = MockPokemonTeam.mockPokemon(Pokedex.Blastoise);

        p1.addMove(MockPokemonTeam.mockMove("Solar Beam"));
        p1.addMove(MockPokemonTeam.mockMove("Scratch"));
        p1.addMove(MockPokemonTeam.mockMove("Scary Face"));
        p1.addMove(MockPokemonTeam.mockMove("Tail Whip"));
        p1.setNickName("Vince");
        p1.setAbility(AbilityID.Water_Absorb);

        p2.addMove(MockPokemonTeam.mockMove("Orbital Strike"));
        p2.addMove(MockPokemonTeam.mockMove("Vine Whip"));
        p2.addMove(MockPokemonTeam.mockMove("Growth"));
        p2.addMove(MockPokemonTeam.mockMove("Synthesis"));
        p2.setNickName("Vance");
        p2.setAbility(AbilityID.Chlorophyll);

        p3.addMove(MockPokemonTeam.mockMove("Necrotic Claw"));
        p3.addMove(MockPokemonTeam.mockMove("Bulldoze"));
        p3.addMove(MockPokemonTeam.mockMove("Gyro Ball"));
        p3.addMove(MockPokemonTeam.mockMove("Shell Smash"));
        p3.setNickName("Bob");
        p3.setAbility(AbilityID.Torrent);
        p3.setItem(HeldItemID.Bomb_Surprise);

        return new PokemonTrainerBuilder()
        .setName(name)
        .addPokemon(p1)
        .addPokemon(p2)
        .addPokemon(p3)
        .build();
    }

    private static PokemonTrainer build2(String name) {
        Pokemon p1 = MockPokemonTeam.mockPokemon(Pokedex.Charizard);
        Pokemon p2 = MockPokemonTeam.mockPokemon(Pokedex.Blastoise);

        p1.addMove(MockPokemonTeam.mockMove("Infect"));
        p1.addMove(MockPokemonTeam.mockMove("Dragon Dance"));
        p1.addMove(MockPokemonTeam.mockMove("Amnesia"));
        p1.addMove(MockPokemonTeam.mockMove("Fire Fang"));
        p1.setNickName("Charlie");
        p1.setAbility(AbilityID.Simple);

        p2.addMove(MockPokemonTeam.mockMove("Aqua Jet"));
        p2.addMove(MockPokemonTeam.mockMove("Icy Wind"));
        p2.addMove(MockPokemonTeam.mockMove("Bite"));
        p2.addMove(MockPokemonTeam.mockMove("Rain Dance"));
        p2.setAbility(AbilityID.Torrent);
        p2.setNickName("Barry");

        return new PokemonTrainerBuilder()
        .setName(name)
        .addPokemon(p1)
        .addPokemon(p2)
        .build();
    }

    public static PokemonTrainer build(int id, String name) {
        return id == 0 ? build1(name) : build2(name);
    }
}
