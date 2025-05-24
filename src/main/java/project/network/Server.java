package project.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Server {
    public static final int PORT = 6575; // Change to any ephemeral you'd like 
    public static final int NUM_CLIENTS = 2; // Number of clients connected (Exactly 2 needed for game to start)
    
    // Syncronizines client threads and server for processing
    public static final CyclicBarrier BARRIER = new CyclicBarrier(Server.NUM_CLIENTS + 1); 
    
    private final ServerSocket serverSocket;
    
    public Server(ServerSocket socket) {
        this.serverSocket = socket;
    }


    // Starts the server on the specified port
    // Waits for two clients to connect before finishing
    public void start() {
        System.out.printf("[SERVER] Server started on port [%d].\n", Server.PORT);
        try {
            for (int i = 0; i < Server.NUM_CLIENTS; i++) { // While server is still running
                int playerNum = i + 1;
                System.out.printf("[SERVER] Waiting for Player %d to connect...\n", playerNum);

                // Waits until a client makes a connection request
                Socket socket = this.serverSocket.accept(); 
                System.out.printf("[PLAYER %d] Connected to server.\n", playerNum);

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
    public void beginBattle() {
       
        // Waits until both players are ready to battle
        System.out.println("[SERVER] Both players connected. Waiting for players to setup...");
        Server.lock();

        ClientHandler.broadcastMessageAll("The battle has begun");

        while (true) {
            System.out.println("[SERVER] Waiting for players...");
            Server.lock();

            System.out.println("[SERVER] Next round");
        }
            
      
    }

    // Closes the server socket
    public void close() {
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
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

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Server.PORT);
        Server server = new Server(serverSocket);
        server.start();
        server.beginBattle();
    }
}