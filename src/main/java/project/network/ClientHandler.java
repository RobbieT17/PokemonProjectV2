package project.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import project.config.GameConfig;
import project.debug.MockPokemonTeam;
import project.game.battle.BattlePosition;
import project.game.event.GameEvents.EventID;
import project.game.move.Move;
import project.game.move.Move.MoveTarget;
import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokemon;
import project.game.selectors.MoveSelector;
import project.game.selectors.MoveTargetSelector;
import project.game.selectors.PokemonSelector;
import project.game.selectors.PokemonTrainerSelector;
import project.game.utility.StatDisplay;

// NOTE: This class is run server side
public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private int clientId;

    private String clientName;
    private int playerNum;

    private PokemonTrainer player;
    private PokemonTrainer opponent;

    /**
     * Creates a new class to handle a client connection on the server side
     * @param socket The socket the client is conencted to
     */
    public ClientHandler(Socket socket) {
        if (Server.clientCount == Server.NUM_CLIENTS) {
            throw new IllegalStateException("Reach the maximum amount of instances");
        }

        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.clientId = Server.clientCount++;
            this.playerNum = clientId + 1;

            Server.CLIENTS[this.clientId] = this;
        } catch (IOException e) {
            this.closeAll();
        }

    }

    // Writes message line to buffer
    public void writeToBuffer(String message, Object... args) {
        try {
            this.bufferedWriter.write(String.format(message, args));
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            this.closeAll();
        }
    }
    
    // Reads message from buffer then returns that value
    public String readFromBuffer() {
        try {
            String message = this.bufferedReader.readLine();
            return message;   
        } catch (IOException e) {
            closeAll();
        }
        return null;
    }
 
    // Closes socket connection and buffer reader/writer.
    public void closeAll() {
        try {
            if (this.bufferedReader != null) {
                this.bufferedReader.close();
            }

            if (this.bufferedWriter != null) {
                this.bufferedWriter.close();
            }

            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
 
    /**
     * The server prompts the user for their name
     */
    public void setUpClient() {
        Server.logp(this.playerNum, "Signing in...");

        this.writeToBuffer("Please enter your name >>");
        this.clientName = this.readFromBuffer();

        this.clientName = !this.clientName.isEmpty() ? this.clientName : "P" + this.playerNum;
        
        // Welcome message
        this.writeToBuffer("=======================================");
        this.writeToBuffer("Hello, %s! Welcome to Pokemon Shutdown!", this.clientName);
        this.writeToBuffer("This is a simple Pokemon battle simulator");
        this.writeToBuffer("where you build a team of pokemon and battle");
        this.writeToBuffer("an opponent. Good luck, and have fun!");
        this.writeToBuffer("\nGAME MODE: %s", GameConfig.DOUBLES_MODE_ENABLED ? "DOUBLES" : "SINGLES");
        this.writeToBuffer("=======================================");

        Server.logp(this.playerNum, "Signed in as %s.", this.clientName);
        
    }

    // The server prompts the user to create a Pokemon team for battle, stores
    // the team on the server "database"
    public void buildTeam() {
        Server.logp(this.playerNum, "Building team...");

        if (GameConfig.USE_PRESET_TRAINER_BUILDS) {
            this.writeToBuffer("<CONFIG> Using preset team build.");
            this.player = MockPokemonTeam.build(this.clientId, this.clientName);
        }
        else {
            PokemonTrainerSelector pokemonTrainerSelector = new PokemonTrainerSelector(this);
            this.player = pokemonTrainerSelector.initializPokemonTrainer();
        }

        Server.PLAYERS[this.clientId] = this.player; // Adds reference of trainer to Server class
        Server.logp(this.playerNum, "Ready for battle.");
    }

    // Prompts user to select a pokemon to send into battle at position i
    public Pokemon selectPokemon(int i) {
        Server.logp(this.playerNum, "Selecting a pokemon...");

        PokemonSelector selector = new PokemonSelector(this);
        Pokemon p = selector.choosePokemon();
        this.player.setPokemonInBattle(p, i);

        Server.logp(this.playerNum, "Sent out %s.", this.player.getPokemonInBattle(i));
        this.writeToBuffer("You sent out %s.", this.player.getPokemonInBattle(i));

        return p;
    }

    // Prompts user to select a pokemon move to use
    public Move selectMove(Pokemon p) {
        Server.logp(this.playerNum, "Selecting a move...");

        p.getEvents().updateEvent(EventID.MOVE_SELECTION, null);

        Move m;
        if (p.getMoveSelected() == null) { 
            MoveSelector selector = new MoveSelector(this, p);
            m = selector.chooseMove();
        }
        else { // Preset Move
            m = p.getMoveSelected();
        }
        
        if (m == null) { // Returning null means the player switched out their Pokemon
            return null;
        }

        p.setMoveSelected(m);

        Server.logp(this.playerNum, "Move selected.");
        this.writeToBuffer("You selected %s", p.getMoveSelected());
         
        return m;
    }


    // Prompts user to select the target of the move
    // TODO: Currently, no input is needed bc only implemented single battle (will add double battles soon)
    public BattlePosition[] selectTargetPokemon(Move m, Pokemon p) {
        Server.logp(this.playerNum, "Selecting a target...");

        if (m == null) {
            return null;
        }

        p.getEvents().updateEvent(EventID.TARGET_SELECTION, null);

        BattlePosition[] targetPositions;
        if (p.getTargetPositions() != null) { // Preset Target
            targetPositions = p.getTargetPositions();
        }
        else {
            targetPositions = new MoveTargetSelector(
                this, 
                m.getMoveTarget(),
                p.getPosition(),
                this.player.getBattlePositions(),
                this.opponent.getBattlePositions()
            ).select(); 
            
        }
        
        p.setTargetPositions(targetPositions);
        this.writeToBuffer(StatDisplay.displayTargetsSelected(targetPositions, p));
        
        Server.logp(this.playerNum, "Target locked.");
        return targetPositions;
    }

    public void setup() {
        // Notifies client the server connection was successful
        this.writeToBuffer("You are now connected to the server.");

        // Build Process
        this.setUpClient();
        this.buildTeam();

        // Waits for other client to finish setup
        this.writeToBuffer("You are Player %d, waiting for opponent...", this.playerNum);
        Server.lock();

        // Sets opponent
        this.opponent = Server.PLAYERS[(this.clientId + 1) % 2];

        this.writeToBuffer("==================================\n==================================");
        this.writeToBuffer("Let the battle begin!");
        this.writeToBuffer("Your opponent:\n%s", this.opponent.showPokemon());
        this.writeToBuffer("==================================");

        // Client chooses pokemon to first use in battle.
        for (int i = 0; i < this.player.getBattlePositions().length; i++) {
            this.selectPokemon(i);
        }
        
        this.player.updateShowPokemon();

        // Waits for other player to finish setup
        this.writeToBuffer("Waiting for opponent...");
        Server.lock();
    }

    public void battleProcess() {
        // Battle Process
        while (this.socket.isConnected()) {
            Server.lock(); // Waits for server to finish processing

            // A skipped round indicates a Pokemon fainted the last round, checks if this client's pokemon fainted
            if (Server.skipRound) {
                for (BattlePosition pos : this.player.getBattlePositions()) {
                    if (this.player.canSelectPokemon(pos.getId(), false)) {
                        this.selectPokemon(pos.getId());
                    }
                }
            } 
            else {
                this.writeToBuffer("Round %d", Server.round);

                // Each Pokemon in battle selects a move
                for (BattlePosition pos : this.player.getBattlePositions()) {
                    if (pos.getCurrentPokemon() == null) {
                        continue;
                    }

                    int id = pos.getId();
                    Move m = this.selectMove(pos.getCurrentPokemon());

                    if (m != null) {
                        this.selectTargetPokemon(m, pos.getCurrentPokemon());
                    }
                    else {
                        this.selectPokemon(id);
                    }
                }
   
            }
            this.writeToBuffer("============================================================");
            this.writeToBuffer("Waiting for opponent....");

            Server.lock(); // Waits for opponent
            this.writeToBuffer("Waiting for server...");

            if (this.socket.isClosed()) { // Checks if socket was closed
                break;
            }    
        }
    }

    // Getters
    public int clientId() {
        return this.clientId;
    }

    public String clientName() {
        return this.clientName;
    }

    public PokemonTrainer getPlayer() {
        return this.player;
    }

    public PokemonTrainer getOpponent() {
        return this.opponent;
    }

// Main Function: Clients inputs commands to send back to the server 
    @Override
    public void run() {
        this.setup();
        this.battleProcess();
    }
    
}