package dev.valente.producer;

import dev.valente.domain.Producer;
import dev.valente.user_service.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerService {

  private final ProducerRepository producerRepository;

  public List<Producer> findAll() {
    return producerRepository.findAll();
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

    producerRepository.delete(producerToDelete);
  }

  public void replace(Producer producer) {

    var producerToRemove = findByIdOrThrowNotFound(producer.getId());
    producer.setCreatedAt(producerToRemove.getCreatedAt());

    producerRepository.save(producerToRemove);

  }


}
