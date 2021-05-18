package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {

    private static int counter = 0;
    private final ByteBuffer buffer;
    private final ServerSocketChannel serverChannel;
    private final Selector selector;
    private final DateTimeFormatter formatter;

    public Server() throws IOException {
        buffer = ByteBuffer.allocate(256);
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");
        serverChannel = ServerSocketChannel.open();
        log.debug("Server started...");
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(8189));
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (serverChannel.isOpen()) {
            selector.select(); // block
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    handleAccept(selectionKey);
                }
                if (selectionKey.isReadable()) {
                    handleRead(selectionKey);
                }
                iterator.remove();
            }
        }
    }

    private void handleRead(SelectionKey selectionKey) {
        try {
            if (selectionKey.isValid()) {
                SocketChannel channel = (SocketChannel) selectionKey.channel();
                String attachment = (String) selectionKey.attachment();

                StringBuilder reader = new StringBuilder();
                int read = 0;

                while (true) {
                    read = channel.read(buffer);
                    if (read == 0) {
                        break;
                    }
                    if (read == -1) {
                        channel.close();
                    }
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        reader.append((char) buffer.get());
                    }
                    buffer.clear();
                }

                String message = reader.toString();
                log.debug("received from {} msg: {}", attachment, message);
                String date = formatter.format(LocalDateTime.now());
                for (SelectionKey key : selector.keys()) {
                    if (key.isValid() && key.channel() instanceof SocketChannel) {
                        SocketChannel ch = (SocketChannel) key.channel();
                        ch.write(ByteBuffer.wrap((date + " " + attachment + ": " + message)
                                .getBytes(StandardCharsets.UTF_8)));
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Client {} disconnected!", selectionKey.attachment());
        }

    }

    private void handleAccept(SelectionKey selectionKey) throws IOException {
        counter++;
        log.debug("Client {} accepted...", "user" + counter);
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, "user" + counter);
    }

}
