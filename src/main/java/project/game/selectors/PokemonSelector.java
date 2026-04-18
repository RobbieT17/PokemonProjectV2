package project.game.selectors;

import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokemon;
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
    private int validPokemonChoice(Pokemon p) {
        if (p.getConditions().isFainted()) {
            return -1;
        }

        if (p == this.trainer.getPokemonInBattle()) {
            return -2;
        }
        return 0;
    }


    /**
     * Chooses a pokemon through user input
     * @param c the client connection
     */
    public Pokemon choosePokemon() {
        Pokemon p = null;
        while (true) {
            try {
                // Lists out trainer's pokemon
                this.client.writeToBuffer(this.listPokemonSelect());
                String input = this.client.readFromBuffer();
                int i = Integer.parseInt(input);

                p = this.trainer.getTeam()[i];

                int status = this.validPokemonChoice(p);
                String message;

                if (status == 0) {
                    break;
                }
                else if (status == -1) {
                    message = String.format("%s fainted and is unable to fight.", p);
                }
                else if (status == -2) {
                    message = String.format("%s is already in the battle.", p);
                }
                else {
                    message = "This Pokemon cannot be used";
                }

                // Invalid Pokemon choice, the user will have to repick
                this.client.writeToBuffer(message);

            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                this.client.writeToBuffer("Invalid input, try again.");
            }
            
        }

        return p;
    }
}
