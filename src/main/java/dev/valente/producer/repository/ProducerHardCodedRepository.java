package dev.valente.producer.repository;

import dev.valente.producer.domain.Producer;
import dev.valente.producer.dto.ProducerGetResponse;
import dev.valente.producer.dto.ProducerPostRequest;
import dev.valente.producer.dto.ProducerPutRequest;
import dev.valente.producer.mapper.ProducerMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class ProducerHardCodedRepository {

    private static final List<Producer> PRODUCERS = new ArrayList<>();

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
        return PRODUCERS.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }


}
