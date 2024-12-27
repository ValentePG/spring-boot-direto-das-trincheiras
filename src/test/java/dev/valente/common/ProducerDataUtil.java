package dev.valente.common;

import dev.valente.producer.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerDataUtil implements DataUtil<Producer> {

    private final List<Producer> PRODUCER_LIST = new ArrayList<>();

    {
        PRODUCER_LIST.add(Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build());
        PRODUCER_LIST.add(Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build());
        PRODUCER_LIST.add(Producer.builder().id(3L).name("Mad House").createdAt(LocalDateTime.now()).build());
    }

    @Override
    public List<Producer> getList() {
        return PRODUCER_LIST;
    }
}
