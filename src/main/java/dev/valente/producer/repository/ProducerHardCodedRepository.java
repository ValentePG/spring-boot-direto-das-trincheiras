package dev.valente.producer.repository;

import dev.valente.producer.domain.Producer;
import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
@Log4j2
public class ProducerHardCodedRepository {

    private final ProducerData producerData;

    //    @Qualifier(value = "connectionMongoDB")
    private final Connection connectionMongoDB;

    public List<Producer> getProducers() {
        return producerData.getProducers();
    }

    public Producer save(Producer producer) {
        producerData.getProducers().add(producer);
        return producer;
    }

    public void remove(Producer producer) {
        producerData.getProducers().remove(producer);
    }

    public void replace(Producer oldProducer, Producer newProducer) {
        remove(oldProducer);
        save(newProducer);
    }

    public Optional<Producer> findProducerById(Long producerId) {
        return producerData.getProducers().stream()
                .filter(a -> a.getId().equals(producerId))
                .findFirst();

    }

    public Optional<Producer> findProducerByName(String name) {
        log.debug(connectionMongoDB);
        return producerData.getProducers().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }


}
