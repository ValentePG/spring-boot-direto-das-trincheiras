package dev.valente.producer.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producer {

    private Long id;
    private String name;
    private LocalDateTime createdAt;

    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
        producers.add(Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build());
        producers.add(Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build());
        producers.add(Producer.builder().id(3L).name("Mad House").createdAt(LocalDateTime.now()).build());
    }

    public static void save(Producer producer){
        producers.add(producer);
    }

    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
