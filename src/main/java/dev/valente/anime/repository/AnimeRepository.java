package dev.valente.anime.repository;

import dev.valente.anime.domain.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnimeRepository {

    private final AnimeData animeData;

    public List<Anime> findAll() {
        return animeData.getAnimes();
    }

    public Anime save(Anime anime) {
        animeData.getAnimes().add(anime);
        return anime;
    }

    public void remove(Anime anime) {
        animeData.getAnimes().remove(anime);
    }

    public void replace(Anime oldAnime, Anime newAnime) {
        remove(oldAnime);
        save(newAnime);
    }

    public Optional<Anime> findById(Long id) {
        return animeData.getAnimes().stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
    }

    public Optional<Anime> findByName(String name) {
        return animeData.getAnimes().stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
