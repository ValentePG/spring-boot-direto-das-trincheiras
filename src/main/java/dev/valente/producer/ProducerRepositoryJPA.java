package dev.valente.producer;

import dev.valente.domain.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProducerRepositoryJPA extends JpaRepository<Producer, Long> {
    Optional<Producer> findProducerByName(String name);
    Optional<Producer> findProducerById(Long id);
}
