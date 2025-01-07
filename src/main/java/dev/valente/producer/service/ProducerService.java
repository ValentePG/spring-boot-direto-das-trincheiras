package dev.valente.producer.service;

import dev.valente.producer.domain.Producer;
import dev.valente.producer.repository.ProducerHardCodedRepository;
import dev.valente.user_service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerHardCodedRepository producerRepository;

    public List<Producer> findAll() {
        return producerRepository.getProducers();
    }

    public Producer save(Producer producer) {

        return producerRepository.save(producer);
    }

    public Producer findByNameOrThrowNotFound(String name) {
        return producerRepository.findProducerByName(name)
                .orElseThrow(() -> new NotFoundException("Producer not found"));
    }

    public Producer findByIdOrThrowNotFound(Long id) {
        return producerRepository.findProducerById(id)
                .orElseThrow(() -> new NotFoundException("Producer not found"));
    }

    public void delete(Long producerId) {

        var producerToDelete = findByIdOrThrowNotFound(producerId);

        producerRepository.remove(producerToDelete);
    }

    public void replace(Producer producer) {

        var producerToRemove = findByIdOrThrowNotFound(producer.getId());
        producer.setCreatedAt(producerToRemove.getCreatedAt());

        producerRepository.replace(producerToRemove, producer);

    }


}
