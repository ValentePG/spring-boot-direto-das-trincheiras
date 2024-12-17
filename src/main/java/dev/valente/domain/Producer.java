package dev.valente.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Producer {

    private Long id;
    private String name;

    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
        producers.add(new Producer(1L, "Mappa"));
        producers.add(new Producer(2L, "Kyoto Animation"));
        producers.add(new Producer(3L, "Madhouse"));
    }

    public static void save(Producer producer){
        producers.add(producer);
    }
}
