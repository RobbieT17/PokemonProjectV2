package project.game.pokemon;

import project.game.player.PokemonTrainer;
import project.game.utility.StatDisplay;
import project.network.ClientHandler;

public class PokemonSelector {

    private final ClientHandler client;
    private final PokemonTrainer trainer;

    public PokemonSelector(ClientHandler c) {
        this.client = c;
        this.trainer = c.getPlayer();
    }

    // Formats Pokemon team info with selection option
    private String listPokemonSelect() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.trainer.getTeam().length; i++) {
            sb.append(String.format("[%d] %s", i, StatDisplay.showPartyStats(this.trainer.getTeam()[i])));
        }

        sb.append("\nPlease select a pokemon >>");

        return sb.toString();
    }

     // Checks if a the select pokemon is valid. (1. Cannot be fainted and cannot already be on the field)
    private boolean validPokemonChoice(Pokemon p) {
        return !p.getConditions().isFainted() && (this.trainer.getPokemonInBattle() != null) ?  !this.trainer.getPokemonInBattle().equals(p) : true;
    }


    /**
     * Chooses a pokemon through user input
     * @param c the client connection
     */
    public Pokemon choosePokemon() {
        if (this.trainer.outOfPokemon()) {
            return null;
        }

        Pokemon p = null;
        while (true) {
            try {
                // Lists out trainer's pokemon
                this.client.writeToBuffer(this.listPokemonSelect());
                String input = this.client.readFromBuffer();
                int i = Integer.parseInt(input);

                p = this.trainer.getTeam()[i];
                if (this.validPokemonChoice(p)) {
                    break;
                }

                // Invalid Pokemon choice, the user will have to repick
                this.client.writeToBuffer("This pokemon cannot be used, try again.");

            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                this.client.writeToBuffer("Invalid input, try again.");
            }
            
        }

        return p;
    }
}
