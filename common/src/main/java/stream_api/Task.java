package stream_api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.SneakyThrows;

public class Task {

    @SneakyThrows
    public List<Map.Entry<String, Integer>> getTextEntries()  {
        Path path = Paths.get(getClass().getResource("1.txt").toURI());
        return Files.lines(path)
                .flatMap(s -> Arrays.stream(s.trim().split(" +")))
                .map(String::trim)
                .map(String::toLowerCase)
                .map(s -> s.replaceAll("\\W+", ""))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toMap(
                        Function.identity(),
                        value -> 1,
                        Integer::sum
                ))
                .entrySet().stream()
                .sorted((o1, o2) -> o2.getValue() - o1.getValue())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(new Task().getTextEntries());
    }

}
