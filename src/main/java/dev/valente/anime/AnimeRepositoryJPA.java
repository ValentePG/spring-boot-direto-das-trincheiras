package dev.valente.anime;

import dev.valente.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimeRepositoryJPA extends JpaRepository<Anime, Long> {
    Optional<Anime> findAnimeByName(String name);
    Optional<Anime> findAnimeByNameAndIdNot(String name, Long id);
}
