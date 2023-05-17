package aufgabe16.aufgabenblatt16;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient2 {
    private JFrame frame;
    private JTextField messageField;
    private JTextArea chatArea;
    private JButton sendButton;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String commandPrefix = "/";

    public ChatClient2() {
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);

        messageField.addActionListener(e -> {
            sendMessage();
        });

        sendButton = new JButton("Send");
        bottomPanel.add(sendButton, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> {
            sendMessage();
        });

        frame.setSize(500, 500);
        frame.setVisible(true);
    }
    public void sendMessage(){
        String message = messageField.getText();
        if(!message.isEmpty()) {
            if (message.startsWith(commandPrefix)) {
                handleCommand(message.substring(1));
            } else {
                chatArea.append("Ich: " + message + "\n");
                out.println(username + ": " + message);
            }
            messageField.setText("");
        }
    }

    public void start(String serverIp, int port, String username) {
        this.username = username;
        try {
            clientSocket = new Socket(serverIp, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println(username);

            Thread messageReaderThread = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        if (serverResponse.startsWith("!")) {
                            handleCommand(serverResponse);
                        } else {
                            chatArea.append(serverResponse + "\n");
                        }
                    }
                } catch (Exception e) {
                    chatArea.append("Error reading server response: " + e.getMessage() + "\n");
                }
            });
            messageReaderThread.start();
        } catch (Exception e) {
            chatArea.append("Couldn't get I/O for the connection to " + serverIp + "\n");
            System.exit(1);
        }
    }

    private void handleCommand(String command) {
        if (command.equals("exit")) {
            chatArea.append("Verbindung wird geschlossen...\n");
            System.exit(0);
        } else {
            chatArea.append("Unbekannter Befehl: " + command + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChatClient2 client = new ChatClient2();
                String username = JOptionPane.showInputDialog("Geben Sie Ihren Benutzernamen ein:");
                String serverIp = JOptionPane.showInputDialog("Geben Sie die IP des Servers ein:");
                client.start(serverIp, 4444, username);
            }
        });
    }
}
