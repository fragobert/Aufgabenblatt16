package aufgabe16.aufgabenblatt16;

import java.io.*;
import java.net.*;

public class ChatClient2 {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String commandPrefix = "/";

    public void start(String serverIp, int port, String username) {
        this.username = username;
        try {
            clientSocket = new Socket(serverIp, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Senden des Benutzernamens an den Server
            out.println(username);

            // Erstellen eines Threads, um die eingehenden Nachrichten zu lesen und anzuzeigen
            Thread messageReaderThread = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        if (serverResponse.startsWith("!")) {
                            handleCommand(serverResponse); // Behandlung von Befehlen
                        } else {
                            System.out.println(serverResponse);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading server response: " + e.getMessage());
                }
            });
            messageReaderThread.start();

            // Lesen der Benutzereingaben und Senden an den Server
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                if (userInput.startsWith(commandPrefix)) {
                    handleCommand(userInput.substring(1)); // Behandlung von Befehlen
                } else {
                    out.println(username + ": " + userInput);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + serverIp);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + serverIp);
            System.exit(1);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private void handleCommand(String command) {
        if (command.equals("exit")) {
            System.out.println("Verbindung wird geschlossen...");
            System.exit(0);
        } else {
            System.out.println("Unbekannter Befehl: " + command);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Geben Sie Ihren Benutzernamen ein:");
        String username = reader.readLine();
        System.out.println("Geben Sie die IP des Servers ein:");
        String serverIp = reader.readLine();

        ChatClient client = new ChatClient();
        client.start(serverIp, 4444, username);
    }
}

