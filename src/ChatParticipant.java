import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatParticipant extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final ObjectStreamManager streamManager;
    private final ObjectOutputStream outputStream;
    private final JTextArea chatArea;
    private final JTextField inputField;

    public ChatParticipant(Socket socket) throws IOException {
        this.streamManager = new ObjectStreamManager(new ObjectInputStream(socket.getInputStream()));
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.chatArea = new JTextArea();
        this.inputField = new JTextField();
        this.inputField.addActionListener(this);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
        setSize(400, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Starta en tråd som lyssnar på objektmeddelanden från servern
        new Thread(() -> {
            while (true) {
                try {
                    Object obj = streamManager.readObject();
                    objectReceived(obj);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void objectReceived(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            showMessage(str);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = inputField.getText();
        inputField.setText("");
        sendMessage(text);
    }

    public void showMessage(String message) {
        chatArea.append(message + "\n");
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}