package project.move;

import project.network.ClientHandler;
import project.pokemon.Pokemon;
import project.utility.StatDisplay;

public class MoveSelector {

    private final ClientHandler client;
    private final Pokemon pokemon;

    public MoveSelector(ClientHandler c, Pokemon p) {
        this.client = c;
        this.pokemon = p;
    }

    private String listMoveSelect() {
        StringBuilder sb = new StringBuilder();

        sb.append(StatDisplay.showSomeStats(this.pokemon));
        sb.append("\nPlease select a move >>");

        return sb.toString();
    }
    
    // User selects a move through input
    public Move chooseMove() {
        Move m = null;

        while (true) {
            try {
                this.client.writeToBuffer(this.listMoveSelect());
                String input = this.client.readFromBuffer();
                int i = Integer.parseInt(input);

                m = this.pokemon.getMoves()[i];
                if (!(m.getPp().depleted() || m.getDisabled())) {
                    break;
                }

            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                this.client.writeToBuffer("Invalid input, try again.");
            }

        }
        return m;
    }

    
}
