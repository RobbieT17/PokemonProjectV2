package project.game.selectors;

import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.utility.StatDisplay;
import project.network.ClientHandler;

public class MoveSelector {

    private final ClientHandler client;
    private final Pokemon pokemon;

    public MoveSelector(ClientHandler c, Pokemon p) {
        this.client = c;
        this.pokemon = p;
    }

    private String listPlayerOptions() {
        return new StringBuilder()
        .append("OPTIONS:\n")
        .append("[S] Switch Pokemon\n")
        .append("============================================================\n")
        .toString();
        
    }

    private String listMoveSelect() {
        return new StringBuilder()
        .append(StatDisplay.showSomeStats(this.pokemon))
        .append(listPlayerOptions())
        .append("\nPlease select an option >>")
        .toString();
    }

    
    
    /**
     * Returns the move choosen by the player.
     * There is an option for the player to switch out their 
     * current Pokemon. A null pointer is return if this option 
     * is selected.
     */
    public Move chooseMove() {
        Move m = null;

        while (true) {
            try {
                this.client.writeToBuffer(this.listMoveSelect());
                String input = this.client.readFromBuffer();

                // Player chose to switch out Pokemon in battle
                if (input.toLowerCase().equals("s")) {
                    break;
                }

                // Else, player chose to use a move
                int i = Integer.parseInt(input);
                m = this.pokemon.getMoves().get(i);

                String message;
                // Validates move
                if (m.getPp().depleted()) {
                    message = String.format("%s has no more PP left.", m);
                }
                else if (m.isDisabled()) {
                    message = String.format("%s is currently disabled.", m);
                }
                else {
                    break;
                }

                String.format(message);

            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                this.client.writeToBuffer("Invalid input, try again.");
            }

        }
        return m;
    }

    
}
