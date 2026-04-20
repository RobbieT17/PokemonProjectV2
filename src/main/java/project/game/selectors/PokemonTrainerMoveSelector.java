package project.game.selectors;

import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.network.ClientHandler;
import project.network.Server;

public class PokemonTrainerMoveSelector extends Selector {
    
    private final Pokemon pokemon;
    private final String[] pokemonMovePool;

    private int inputSelected;

    public PokemonTrainerMoveSelector(ClientHandler c, Pokemon p) {
        super(c);
        this.pokemon = p;
        this.pokemonMovePool = Server.SERVER_DATA.getPokemonMovePool(this.pokemon.getPokemonName());
    }

    private String listAllMoves() {
        StringBuilder sb = new StringBuilder();
        int columns = 3;
        int columnWidth = 28; // adjust if your move names are longer

        for (int i = 0; i < this.pokemonMovePool.length; i++) {
            String entry = String.format("[%d] %s", i, this.pokemonMovePool[i]);
            sb.append(String.format("%-" + columnWidth + "s", entry));

            // move to next line every 3 entries
            if ((i + 1) % columns == 0) {
                sb.append("\n");
            }
        }

        // final newline if last row wasn't complete
        if (this.pokemonMovePool.length % columns != 0) {
            sb.append("\n");
        }

        return sb.toString();
    }

    private void readInput()  {
        while (true) {
            try {
                this.client.writeToBuffer("Please select a move (%d/%d)", this.pokemon.getMoves().size(), 4);
                this.client.writeToBuffer(this.listAllMoves());
                this.client.writeToBuffer("Input [-1] once done >> ");

                String input = this.client.readFromBuffer();
                int n = Integer.parseInt(input);

                if (n == -1) {
                    if (this.pokemon.getMoves().size() < 2) {
                        // The pokemon needs at least two moves 
                        this.client.writeToBuffer("Please add at least two moves.");
                    }
                    else { // Valid condition to exit loop
                        this.inputSelected = -1;
                        break;
                    }
                }
                else if (n < -1 || n >= this.pokemonMovePool.length) {
                    this.client.writeToBuffer("Input out of bounds, try again.\n");
                }
                else { // Valid condition to exit loop
                    this.inputSelected = n;
                    break;
                }
            
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                this.client.writeToBuffer("Invalid input, try again.\n");
            }
        }
        
    }

    private Move getMoveListData() {
        if (this.inputSelected < 0) {
            return null;
        }

        String moveName = this.pokemonMovePool[this.inputSelected];
        return Server.SERVER_DATA.newMoveInstance(moveName);
    }

    private Move constructMove() {
        this.readInput();
        return this.getMoveListData();
    }

    public void constructPokemonMoves() {
        while (this.pokemon.getMoves().size() < 4) {
            Move move = this.constructMove();

            // Adds Move to the list
            if (move != null) {
                try {
                    this.pokemon.addMove(move);
                    this.client.writeToBuffer("%s learned %s!\n", pokemon, move);  
                }
                catch (IllegalStateException e) {
                    this.client.writeToBuffer("%s already knows %s, try again.\n", pokemon, move);  
                }
                
            }
            // Null return, player terminated selection process early
            else {
                break;
            }
        }
    }

    @Override
    public Object select() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'select'");
    }
}

