package stream_api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamApi {

    public static void main(String[] args) {
        // filter map reduce
        // reduce - sum, min, max, collect, length
        // map <- Function a -> b
        // filter <- Predicate

        Integer res = Stream.of(1, 2, 3, 4, 5, 6)
                .reduce(0, Integer::sum);

        String s = "ffew fw efw fe  wf we  f w";
        List<String> strings = Stream.of(s)
                .flatMap(a -> Arrays.stream(a.split(" +")))
                .collect(Collectors.toList());

        System.out.println(strings);
        System.out.println(res);

        ArrayList<Integer> list = new ArrayList<>(
                Arrays.asList(1, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 5, 6, 2, 8));

        int cnt = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            boolean flag = false;
            while (i < list.size() - 1 && list.get(i).equals(list.get(i + 1))) {
                list.remove(i);
                flag = true;
            }
            if (flag) {
                list.remove(i);
                i--;
            }
        }

        System.out.println(list);

    }
}
