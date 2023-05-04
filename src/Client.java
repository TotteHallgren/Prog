import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {
    private final String serverAddress;
    private final int serverPort;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server: " + socket.getInetAddress().getHostAddress());
            new Thread(new ChatParticipant(socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}