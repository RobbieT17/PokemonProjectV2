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

    private static Pokemon mockPokemon(Pokedex entry, int level) {
        return Server.SERVER_DATA.newPokemonInstance(entry, level);
    }

    private static Move mockMove(String moveName) {
        return Server.SERVER_DATA.newMoveInstance(moveName);
    }

    private static PokemonTrainer build1(String name) {
        Pokemon p1 = MockPokemonTeam.mockPokemon(Pokedex.Venusaur);
        Pokemon p2 = MockPokemonTeam.mockPokemon(Pokedex.Venusaur);
        Pokemon p3 = MockPokemonTeam.mockPokemon(Pokedex.Blastoise);
        Pokemon p4 = MockPokemonTeam.mockPokemon(Pokedex.Charizard);

        p1.addMove(MockPokemonTeam.mockMove("Solar Beam"));
        p1.addMove(MockPokemonTeam.mockMove("Leaf Storm"));
        p1.addMove(MockPokemonTeam.mockMove("Earthquake"));
        p1.addMove(MockPokemonTeam.mockMove("Growth"));
        p1.setNickName("Victoria");
        p1.setAbility(AbilityID.Chlorophyll);
        p1.setItem(HeldItemID.Black_Sludge);

        p2.addMove(MockPokemonTeam.mockMove("Poison Powder"));
        p2.addMove(MockPokemonTeam.mockMove("Venoshock"));
        p2.addMove(MockPokemonTeam.mockMove("Giga Drain"));
        p2.addMove(MockPokemonTeam.mockMove("Synthesis"));
        p2.setNickName("Vance");
        p2.setAbility(AbilityID.Water_Absorb);
        p2.setItem(HeldItemID.Leftovers);

        p3.addMove(MockPokemonTeam.mockMove("Rock Slide"));
        p3.addMove(MockPokemonTeam.mockMove("Bulldoze"));
        p3.addMove(MockPokemonTeam.mockMove("Surf"));
        p3.addMove(MockPokemonTeam.mockMove("Shell Smash"));
        p3.setNickName("Bob");
        p3.setAbility(AbilityID.Torrent);
        p3.setItem(HeldItemID.Rocky_Helmet);

        p4.addMove(MockPokemonTeam.mockMove("Focus Punch"));
        p4.addMove(MockPokemonTeam.mockMove("Flame Charge"));
        p4.addMove(MockPokemonTeam.mockMove("Hurricane"));
        p4.addMove(MockPokemonTeam.mockMove("Swords Dance"));
        p4.setNickName("Carly");
        p4.setAbility(AbilityID.Blaze);
        p4.setItem(HeldItemID.Muscle_Band);

        return new PokemonTrainerBuilder()
        .setName(name)
        .addPokemon(p1)
        .addPokemon(p2)
        .addPokemon(p3)
        .addPokemon(p4)
        .build();
    }

    private static PokemonTrainer build2(String name) {
        Pokemon p1 = MockPokemonTeam.mockPokemon(Pokedex.Charizard, 60);
        Pokemon p2 = MockPokemonTeam.mockPokemon(Pokedex.Blastoise, 60);
        Pokemon p3 = MockPokemonTeam.mockPokemon(Pokedex.Venusaur, 60);
        Pokemon p4 = MockPokemonTeam.mockPokemon(Pokedex.Charizard, 60);

        p1.addMove(MockPokemonTeam.mockMove("Sandstorm"));
        p1.addMove(MockPokemonTeam.mockMove("Dragon Dance"));
        p1.addMove(MockPokemonTeam.mockMove("Fly"));
        p1.addMove(MockPokemonTeam.mockMove("Fire Fang"));
        p1.setNickName("Charlie");
        p1.setAbility(AbilityID.Blaze);
        p1.setItem(HeldItemID.Muscle_Band);

        p2.addMove(MockPokemonTeam.mockMove("Dive"));
        p2.addMove(MockPokemonTeam.mockMove("Icy Wind"));
        p2.addMove(MockPokemonTeam.mockMove("Earth Power"));
        p2.addMove(MockPokemonTeam.mockMove("Rain Dance"));
        p2.setNickName("Barry");
        p2.setAbility(AbilityID.Torrent);
        p2.setItem(HeldItemID.Wise_Glasses);

        p3.addMove(MockPokemonTeam.mockMove("Petal Dance"));
        p3.addMove(MockPokemonTeam.mockMove("Dig"));
        p3.addMove(MockPokemonTeam.mockMove("Amnesia"));
        p3.addMove(MockPokemonTeam.mockMove("Sleep Powder"));
        p3.setNickName("Violet");
        p3.setAbility(AbilityID.Overgrow);

        p4.addMove(MockPokemonTeam.mockMove("Giga Impact"));
        p4.addMove(MockPokemonTeam.mockMove("Graveyard Shock"));
        p4.addMove(MockPokemonTeam.mockMove("Sunny Day"));
        p4.addMove(MockPokemonTeam.mockMove("Overheat"));
        p4.setNickName("Chandler");
        p4.setAbility(AbilityID.Blaze);
        p4.setItem(HeldItemID.Muscle_Band);
        

        return new PokemonTrainerBuilder()
        .setName(name)
        .addPokemon(p1)
        .addPokemon(p2)
        .addPokemon(p3)
        .addPokemon(p4)
        .build();
    }

    public static PokemonTrainer build(int id, String name) {
        return id == 0 ? build1(name) : build2(name);
    }
}
