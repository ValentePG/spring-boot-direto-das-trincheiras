package dev.valente.common;

import dev.valente.producer.domain.Producer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerDataUtil implements DataUtil<Producer> {

    private final List<Producer> PRODUCER_LIST = new ArrayList<>();

    private final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

    private final LocalDateTime DATA = LocalDateTime.parse("2024-12-29T20:00:54.0151691", DATA_FORMATTER);


    {
        PRODUCER_LIST.add(Producer.builder().id(1L).name("Animation").createdAt(DATA).build());
        PRODUCER_LIST.add(Producer.builder().id(2L).name("Kyoto").createdAt(DATA).build());
        PRODUCER_LIST.add(Producer.builder().id(3L).name("House").createdAt(DATA).build());
    }

    public List<Producer> getList() {
        return PRODUCER_LIST;
    }

    public Producer getProducerToSave() {
        return Producer.builder()
                .id(15L)
                .name("Mappa")
                .createdAt(DATA).build();
    }

    public Producer getNewProducer() {
        return Producer.builder()
                .id(getFirst().getId())
                .name("Kyoto").build();
    }

    public Producer getNewProducerWithCreatedAt() {
        return Producer.builder()
                .id(getFirst().getId())
                .name("Kyoto")
                .createdAt(getFirst().getCreatedAt())
                .build();
    }

    public Producer getProducerToFind() {
        return getFirst();
    }

    public Producer getProducerToReplace() {
        return getFirst();
    }

    public Producer getProducerToRemove() {
        return getFirst();
    }

    private Producer getFirst(){
        return PRODUCER_LIST.getFirst();
    }


}
