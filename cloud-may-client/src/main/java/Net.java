import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Net implements Closeable {

    private int port;
    private Socket socket;
    private DataInputStream is;
    private DataOutputStream os;

    private Net(int port) throws IOException {
        socket = new Socket("localhost", port);
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
    }

    public static Net start(int port) throws IOException {
        return new Net(port);
    }

    public void sendMessage(String msg) throws IOException {
        os.writeUTF(msg);
        os.flush();
    }

    public String getMessage() throws IOException {
        return is.readUTF();
    }

    @Override
    public void close() throws IOException {
        os.close();
        is.close();
        socket.close();
    }
}
