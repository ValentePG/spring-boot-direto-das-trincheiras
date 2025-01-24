package dev.valente.producer;

import dev.valente.domain.Producer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

  Optional<Producer> findProducerByName(String name);

  Optional<Producer> findProducerById(Long id);
}
