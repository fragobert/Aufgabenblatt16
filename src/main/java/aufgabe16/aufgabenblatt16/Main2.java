package aufgabe16.aufgabenblatt16;

import java.io.*;

public class Main2 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Möchten Sie ein Server oder ein Client sein? (Server/Client)");
        String role = reader.readLine();

        if (role.equalsIgnoreCase("Server")) {
            ChatServer server = new ChatServer();
            server.start(4444);
        } else if (role.equalsIgnoreCase("Client")) {
            System.out.println("Geben Sie Ihren Benutzernamen ein:");
            String username = reader.readLine();
            System.out.println("Geben Sie die IP des Servers ein:");
            String serverIp = reader.readLine();

            ChatClient client = new ChatClient();
            client.start(serverIp, 4444, username);
        } else {
            System.out.println("Ungültige Auswahl. Bitte wählen Sie 'Server' oder 'Client'.");
        }
    }
}