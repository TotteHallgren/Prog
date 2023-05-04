import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatParticipant extends JFrame implements ActionListener, Runnable {

    private static final long serialVersionUID = 1L;
    private Socket socket;
    private ObjectStreamManager objManager;
    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    private String username;

    public ChatParticipant(Socket socket, String username) {
        this.socket = socket;
        this.username = username;

        // Skapa input- och output-strömmar
        try {
            objManager = new ObjectStreamManager(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Skapa GUI-komponenter
        textArea = new JTextArea();
        textField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        // Lägg till komponenter i fönstret
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Konfigurera fönstret
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);

        // Starta en ny tråd för att lyssna på meddelanden från servern
        new Thread(this).start();
    }

    public void objectReceived(Object obj) {
        if (obj instanceof String) {
            String message = (String) obj;
            textArea.append(message + "\n");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String message = textField.getText().trim();
            if (!message.equals("")) {
                try {
                    writeToStream(msg);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                textField.setText("");
            }
        }
    }

    public void displayMessage(String message) {
        textArea.append(message + "\n");
    }

    public void sendMessage(String message) {
        try {
            objManager.writeToStream(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                Object obj = objManager.readFromStream();
                objectReceived(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
