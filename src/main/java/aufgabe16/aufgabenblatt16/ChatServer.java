package aufgabe16.aufgabenblatt16;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server gestartet. Warte auf Clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler client = new ClientHandler(clientSocket);
                clients.add(client);
                client.start();
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                username = in.readLine(); // Lese den Benutzernamen des Clients

                logMessage(username + " hat den Chat betreten.");

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    broadcast(inputLine);
                }

                // Client verlässt den Chat
                logMessage(username + " hat den Chat verlassen.");

                clientSocket.close();
                removeClient(this);

            } catch (IOException e) {
                System.out.println("Exception caught when trying to interact with a client");
                System.out.println(e.getMessage());
            }
        }

        private void logMessage(String message) {
            System.out.println(message);
        }

        private void broadcast(String message) {
            for (ClientHandler client : clients) {
                if (!client.username.equals(username)) {
                    client.out.println(message);
                }
            }
        }
    }

    private synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start(4444);
    }
}
