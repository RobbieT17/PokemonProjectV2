package project.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import project.battle.Battle;
import project.player.PokemonTrainer;
import project.utility.Time;


public class Server {
// Class Variables
    public static final int PORT = 6575; // Change to any ephemeral you'd like (PORT > 1024)
    public static final int NUM_CLIENTS = 2; // Number of clients connected (Exactly 2 needed for game to start)

    // A list of all connected clients
    public static final ClientHandler[] CLIENTS = new ClientHandler[Server.NUM_CLIENTS];
    public static int clientCount = 0;

    // Stores player data (name and pokemon team)
    public static final PokemonTrainer[] PLAYERS = new PokemonTrainer[Server.NUM_CLIENTS];

    // Syncronizines client threads and server for processing
    public static final CyclicBarrier BARRIER = new CyclicBarrier(Server.NUM_CLIENTS + 1); 
    public static int round = 0; // The current round number of the battle

// Class Methods
    /**
     * The server broadcasts a message to all connected clients
     */
    public static void broadcast(String message, Object... args) {
        for (ClientHandler c : Server.CLIENTS) {
            c.writeToBuffer("[SERVER] %s", String.format(message, args));
        }
    }

    // Closes the server socket
    public static void close(ServerSocket ss) {
        try {
            if (ss != null) {
                ss.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // For thread syncronation
    public static void lock() {
        try {
            Server.BARRIER.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        } 
    }

    // Prints out a message to the server terminal
    public static void log(String s, Object ...args) {
        System.out.printf("[SERVER] %s\n", String.format(s, args));
    }

    // Prints out a message to server termimal from the specific client
    public static void logp(int n, String s, Object ...args) {
        System.out.printf("[PLAYER %d] %s\n", n, String.format(s, args));
    }

    // Starts the server on the specified port
    // Waits for two clients to connect before finishing
    public static void start(ServerSocket ss) {
        Server.log("Server started on port %d.", Server.PORT);
        try {
            for (int i = 0; i < Server.NUM_CLIENTS; i++) { // While server is still running
                int playerNum = i + 1;
                Server.log("Waiting for Player %d to connect...", playerNum);

                // Waits until a client makes a connection request
                Socket socket = ss.accept(); 
                Server.logp(playerNum, "Connected to server.");

                // Runs accepted client on a separate thread (View ClientHandler class)
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Starts a new Pokemon battle
    public static void beginBattle() {
        // Waits until both players are ready to battle
        Server.log("Both players connected. Waiting for players to setup...");
        Server.lock();

        Battle battle = new Battle(Server.PLAYERS[0], Server.PLAYERS[1]);
        Server.log("Battle initiated");

        // Waits for players to select starting pokemon
        Server.lock();

        battle.initiatationMessage();

        Server.lock();

        while (true) {
            Server.log("Waiting for players...");
            Server.lock(); // Waits for players to choose a move

            Time.hold(3); // Holds to simulate server delay

            Server.log("Processing round...");  
            battle.processRound();

            Server.log("Round %d", ++Server.round);
            Server.lock();
        }
            
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Server.PORT);
        start(serverSocket);
        beginBattle();
    }
}