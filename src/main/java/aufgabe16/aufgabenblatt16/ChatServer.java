package aufgabe16.aufgabenblatt16;

import java.io.*;
import java.net.*;

public class ChatServer {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public ChatServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";
            while (!line.equals(Protocol.DISCONNECT_STRING)) {
                try {
                    line = in.readUTF();
                    if (line.startsWith(Protocol.CONNECT_STRING)) {
                        out.writeUTF("Connection Successful");
                    } else if (line.startsWith(Protocol.MESSAGE_STRING)) {
                        System.out.println(line.substring(Protocol.MESSAGE_STRING.length()+1));
                    }
                } catch(IOException i) {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");
            socket.close();
            in.close();
        } catch(IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        ChatServer server = new ChatServer(5000);
    }
}
