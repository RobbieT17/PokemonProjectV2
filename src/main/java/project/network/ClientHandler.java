package project.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import project.battle.BattleLog;
import project.move.Move;
import project.player.*;
import project.pokemon.Pokemon;

// NOTE: This class is run server side
public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private int clientId;

    private String clientName;
    private int playerNum;
    private PokemonTrainer player;

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
        
        // Welcome message
        this.writeToBuffer("=======================================");
        this.writeToBuffer("Hello, %s! Welcome to Pokemon Shutdown!", this.clientName);
        this.writeToBuffer("This is a simple Pokemon battle simulator");
        this.writeToBuffer("where you build a team of pokemon and battle");
        this.writeToBuffer("an opponent. Good luck, and have fun!");
        this.writeToBuffer("=======================================");

        Server.logp(this.playerNum, "Signed in as %s.", this.clientName);
    }

    // The server prompts the user to create a Pokemon team for battle, stores
    // the team on the server "database"
    public void buildTeam() {
        Server.logp(this.playerNum, "Building team...");
        Server.PLAYERS[this.clientId] = PokemonTrainerBuilder.createPokemonTrainer(this);
        this.player = Server.PLAYERS[this.clientId]; // Adds a reference to the trainer object for easier access
        Server.logp(this.playerNum, "Ready for battle.");
    }

    // Prompts user to select a pokemon to send into battle
    public Pokemon selectPokemon() {
        Server.logp(this.playerNum, "Selecting a pokemon...");
        Pokemon p = this.player.choosePokemon(this);

        if (p == null) {
            Server.broadcast("Player %d is out of Pokemon!", this.playerNum);
            return null;
        }

        this.player.sendOut(p);
        Server.logp(this.playerNum, "Sent out %s.", this.player.pokemonInBattle());
        return p;
    }

    // Prompts user to select a pokemon move to use
    public Move selectMove(Pokemon p) {
        Server.logp(this.playerNum, "Selecting a move...");
        Move m = p.chooseMove(this);

        if (m == null) {
            return null;
        }

        p.setMove(m);

        Server.logp(this.playerNum, "Move selected.");
        this.writeToBuffer("You selected %s", p.moveSelected());
         
        return m;
    }

    // Getters
    public int clientId() {
        return this.clientId;
    }

    public String clientName() {
        return this.clientName;
    }

// Main Function: Clients inputs a commands to send back to the server (currenly echos what was read to terminal)
    @Override
    public void run() {
        // Notifies client the server connection was successful
        this.writeToBuffer("You are now connected to the server.");

        // Build Process
        this.setUpClient();
        this.buildTeam();

        // Waits for other client to finish setup
        this.writeToBuffer("You are Player %d, waiting for opponent...", this.playerNum);
        Server.lock();

        this.writeToBuffer("==================================\n==================================");
        this.writeToBuffer("Let the battle begin!");
        this.writeToBuffer("Your opponent:\n%s", Server.CLIENTS[(this.clientId + 1) % 2].player.showPokemon());
        this.writeToBuffer("==================================");

        // Client chooses a pokemon to first use in battle.
        this.selectPokemon();

        // Waits for other player to finish setup
        this.writeToBuffer("You sent out %s.\nWaiting for opponent...", this.player.pokemonInBattle(), 
        this.playerNum);
        Server.lock();

        // Battle Process
        while (this.socket.isConnected()) {
            Server.lock(); // Waits for server to finish processing

            this.selectMove(this.player.pokemonInBattle());
            this.writeToBuffer("Waiting for opponent....");
            Server.lock(); // Waits for opponent

            this.writeToBuffer("Waiting for server...");

            if (this.socket.isClosed()) { // Checks if socket was closed
                break;
            }    
        }
    }
    
}