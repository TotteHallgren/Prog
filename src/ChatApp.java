import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
public class ChatApp {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            new ChatParticipant(socket).start();
        } catch (IOException e) {
            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                System.out.println("Server started, waiting for clients...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                new ChatParticipant(socket).start();
            } catch (IOException ex) {
                System.err.println("Could not start server or connect to existing one.");
            }
        }
    }
}


