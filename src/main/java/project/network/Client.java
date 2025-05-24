package project.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e) {
            closeAll();
        }  
    }

    // Sends a message to the server
    public void sendToServer() {
        try {
            Scanner scanner = new Scanner(System.in);
            // Loops until user disconnects
            while (this.socket.isConnected()) {
                String input = scanner.nextLine();

                // Sends message to server
                this.bufferedWriter.write(input);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
            }
            
        } catch (IOException | NoSuchElementException e) {
            closeAll();
        }
    }

    // On a seperate thread: Receives messages sent from the server then prints to standard input
    public void readFromServer() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String message = bufferedReader.readLine();
                    System.out.println(message);
                } catch (IOException | NoSuchElementException e) {
                    closeAll();
                }
            }
        }).start();
    }

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

    public static void main(String[] args) throws IOException {
        // Step 1: Establish connection with the server
        Socket socket = new Socket("localhost", Server.PORT);
        Client client = new Client(socket);
        
        // Step 2: Send/Recieve data to/from the server
        client.readFromServer();
        client.sendToServer();
    
    }
}