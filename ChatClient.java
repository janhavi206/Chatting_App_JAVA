import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient {
    private JFrame frame;
    private JTextField messageField;
    private JTextArea chatArea;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ChatClient(String host, int port) throws IOException {
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(messageField.getText());
                messageField.setText("");
            }
        });
        panel.add(messageField, BorderLayout.NORTH);

        chatArea = new JTextArea(10, 20);
        chatArea.setEditable(false);
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        String message = reader.readLine();
                        chatArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    System.out.println("Error reading from server: " + e.getMessage());
                }
            }
        }).start();
    }

    private void sendMessage(String message) {
        writer.println(message);
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost", 8000);
    }
}