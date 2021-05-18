package nio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class NioUtils {

    public static void main(String[] args) throws Exception {
        NioUtils utils = new NioUtils();
        utils.testWatchService("21");
        Path path = Paths.get("cloud-may-server/src/main/resources/nio/1.txt");
        Path dir = Paths.get("cloud-may-server/src/main/resources");
        utils.write(path, "QQQQQ");
        System.out.println(utils.readAsString(path));
        utils.goDeep(dir);
    }

    private void goDeep(Path path) throws IOException {
        Files.walk(path)
                .filter(p -> !Files.isDirectory(p))
                .forEach(System.out::println);
    }

    private String readAsString(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private void write(Path path, String data) throws IOException {
        Files.write(path,
                data.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.APPEND
        );
    }

    private void testWatchService(String dir) throws Exception {

        Path path = Paths.get("cloud-may-server/src/main/resources/nio");

        WatchService watchService = FileSystems.getDefault().newWatchService();

        new Thread(() -> {
            try {
                while (true) {
                    WatchKey watchKey = watchService.take();
                    List<WatchEvent<?>> events = watchKey.pollEvents();
                    for (WatchEvent<?> event : events) {
                        System.out.println(event.kind());
                        System.out.println(event.context());
                    }
                    watchKey.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE
        );
    }

    private void pathTest(String fileName) throws Exception {

        Path path = Paths.get(getClass().getResource(fileName).toURI());
        Files.lines(path).forEach(System.out::println);
        System.out.println(path.getFileSystem());
        System.out.println(path.getFileName().toString());

        Path pathI = Paths.get("cloud-may-server", "src/main/resources/nio/2.txt");
        System.out.println(pathI.toAbsolutePath());

//        Files.lines(pathI).forEach(System.out::println);
    }
}
