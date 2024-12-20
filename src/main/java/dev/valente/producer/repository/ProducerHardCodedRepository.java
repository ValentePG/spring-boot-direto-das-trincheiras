package dev.valente.producer.repository;

import dev.valente.producer.domain.Producer;
import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
@Log4j2
public class ProducerHardCodedRepository {

    private static final List<Producer> PRODUCERS = new ArrayList<>();
//    @Qualifier(value = "connectionMongoDB")
    private final Connection connectionMongoDB;

    static {
        PRODUCERS.add(Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build());
        PRODUCERS.add(Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build());
        PRODUCERS.add(Producer.builder().id(3L).name("Mad House").createdAt(LocalDateTime.now()).build());
    }


    public List<Producer> getProducers() {
        return PRODUCERS;
    }

    public Producer save(Producer producer) {
        PRODUCERS.add(producer);
        return producer;
    }

    public void remove(Producer producer) {
        PRODUCERS.remove(producer);
    }

    public void replace(Producer oldProducer, Producer newProducer) {
        remove(oldProducer);
        save(newProducer);
    }

    public Optional<Producer> findProducerById(Long producerId) {
        return PRODUCERS.stream()
                .filter(a -> a.getId().equals(producerId))
                .findFirst();

    }

    public Optional<Producer> findProducerByName(String name) {
        log.debug(connectionMongoDB);
        return PRODUCERS.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }


}
