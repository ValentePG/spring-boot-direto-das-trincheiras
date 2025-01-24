package dev.valente.anime;

import dev.valente.domain.Anime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

  Optional<Anime> findAnimeByName(String name);

  Optional<Anime> findAnimeByNameAndIdNot(String name, Long id);
}
