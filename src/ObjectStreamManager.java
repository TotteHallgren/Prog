import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectStreamManager {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ObjectStreamManager(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public Object readObject() throws ClassNotFoundException, IOException {
        return in.readObject();
    }

    public void writeObject(Object obj) throws IOException {
        out.writeObject(obj);
        out.flush();
    }

    public void close() throws IOException {
        in.close();
        out.close();
    }
}
