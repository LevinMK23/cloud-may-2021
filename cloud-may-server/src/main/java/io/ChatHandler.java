package io;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ChatHandler implements Runnable, Closeable {

    private final Socket socket;
    private final Path serverPath;

    public ChatHandler(Socket socket) {
        this.socket = socket;
        serverPath = Paths.get("cloud-may-server", "server_files");
    }

    @Override
    public void run() {
        System.out.println("Start listening...");
        try (DataInputStream is = new DataInputStream(socket.getInputStream());
             DataOutputStream os = new DataOutputStream(socket.getOutputStream())) {
            while (true) {
                String msg = is.readUTF();
                System.out.println("Received: " + msg);
                if (msg.equals("/getListFiles")) {
                    os.writeUTF("/takeFiles");
                    os.writeUTF(getFiles());
                    os.flush();
                } else {
                    os.writeUTF(msg);
                    os.flush();
                }
            }
        } catch (Exception e) {
            System.err.println("Connection was broken");
            try {
                close();
            } catch (Exception ioException) {
                System.err.println("Exception while socket close");
            }
        } finally {
            try {
                close();
                System.out.println("Finish listening");
            } catch (Exception ioException) {
                System.err.println("Exception while socket close");
            }
        }
    }

    public String getFiles() throws IOException {
        return Files.list(serverPath)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.joining(","));
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
