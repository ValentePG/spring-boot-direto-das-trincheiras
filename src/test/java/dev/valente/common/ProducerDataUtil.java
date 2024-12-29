package dev.valente.common;

import dev.valente.producer.domain.Producer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ProducerDataUtil implements DataUtil<Producer> {

    private final List<Producer> PRODUCER_LIST = new ArrayList<>();

    private final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

    private final LocalDateTime DATA = LocalDateTime.parse("2024-12-29T20:00:54.0151691", DATA_FORMATTER);

    {
        PRODUCER_LIST.add(Producer.builder().id(1L).name("Animation").createdAt(DATA).build());
        PRODUCER_LIST.add(Producer.builder().id(2L).name("Kyoto").createdAt(DATA).build());
        PRODUCER_LIST.add(Producer.builder().id(3L).name("House").createdAt(DATA).build());
    }

    @Override
    public List<Producer> getList() {
        return PRODUCER_LIST;
    }
}
