package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class BuffersUtils {

    public static void main(String[] args) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(5);

        RandomAccessFile raf = new RandomAccessFile("cloud-may-server/src/main/resources/nio/1.txt", "rw");
        FileChannel fileChannel = raf.getChannel();

        int read = 0;

        while ((read = fileChannel.read(buffer)) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            buffer.clear();
        }
        buffer.clear();

        //fileChannel.position(0);
        fileChannel.write(ByteBuffer.wrap("Hello".getBytes(StandardCharsets.UTF_8)));


    }
}
