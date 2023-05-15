package aufgabe16.aufgabenblatt16;

import java.net.*;
import java.io.*;

public class ChatClient {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    public ChatClient(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            input = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";
            while (!line.equals(Protocol.DISCONNECT_STRING)) {
                line = input.readLine();
                out.writeUTF(Protocol.MESSAGE_STRING + " " + line);
            }

            input.close();
            out.close();
            socket.close();
        } catch(UnknownHostException u) {
            System.out.println(u);
        } catch(IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        ChatClient client = new ChatClient("127.0.0.1", 5000);
    }
}
