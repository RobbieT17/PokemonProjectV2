package project.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import project.player.PokemonTrainerBuilder;

// NOTE: This class is run server side
public class ClientHandler implements Runnable {

    // A list of all connected clients
    private static final ClientHandler[] clientHandlers = new ClientHandler[Server.NUM_CLIENTS];
    private static int clientCount = 0;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private int clientId;

    private String clientName;
    private int playerNum;

    /**
     * Creates a new class to handle a client connection on the server side
     * @param socket The socket the client is conencted to
     */
    public ClientHandler(Socket socket) {
        if (ClientHandler.clientCount == Server.NUM_CLIENTS) {
            throw new IllegalStateException("Reach the maximum amount of instances");
        }

        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.clientId = ClientHandler.clientCount++;
            this.playerNum = clientId + 1;

            ClientHandler.clientHandlers[this.clientId] = this;
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

    // Getters
    public int clientId() {
        return this.clientId;
    }

    public String clientName() {
        return this.clientName;
    }

   
    /**
     * The server prompts the user for their name
     */
    public void setUpClient() {
        Server.printoutP(this.playerNum, "Signing in...");

        this.writeToBuffer("Please enter your name >>");
        this.clientName = this.readFromBuffer();
        this.writeToBuffer("Hello, %s! Welcome to Pokemon Shutdown!\n", this.clientName);

        Server.printoutP(this.playerNum, "Signed in as %s.", this.clientName);
    }

    // The server prompts the user to create a Pokemon team for battle, stores
    // the team on the server "database"
    public void buildTeam() {
        Server.printoutP(this.playerNum, "Building team...");
        Server.PLAYERS[this.clientId] = PokemonTrainerBuilder.createPokemonTrainer(this);
        Server.printoutP(this.playerNum, "Ready for battle.");
    }

    // Class Methods
    // The server sends a message to itself and all connected clients
    public static void broadcastMessageAll(String message, Object... args) {
        for (ClientHandler c : ClientHandler.clientHandlers) {
            c.writeToBuffer(String.format(message, args));
        }
    }

    @Override
    // Main Function: Clients inputs a commands to send back to the server (currenly echos what was read to terminal)
    public void run() {
        // Notifies client the server connection was successful
        this.writeToBuffer("You are now connected to the server.");

        // Build Process
        this.setUpClient();
        this.buildTeam();

        // Waits for other client to finish setup
        this.writeToBuffer("You are Player %d, waiting for other player...", this.playerNum);
        Server.lock();
        
        // Battle Process
        while (this.socket.isConnected()) {
            this.writeToBuffer(">> ");
            this.readFromBuffer(); // Waits for user-input

            if (this.socket.isClosed()) { // Checks if socket was closed
                break;
            }    
        }
    }
    
}