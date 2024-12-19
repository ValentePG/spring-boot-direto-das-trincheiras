package dev.valente.anime.repository;

import dev.valente.anime.domain.Anime;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeRepository {

    private static final List<Anime> ANIMES = new ArrayList<>();

    static {
        ANIMES.add(new Anime(1L, "Boku No Hero"));
        ANIMES.add(new Anime(2L, "Naruto"));
        ANIMES.add(new Anime(3L, "DBZ"));
        ANIMES.add(new Anime(4L, "HXH"));
        ANIMES.add(new Anime(5L, "SNK"));
    }

    public List<Anime> findAll() {
        return ANIMES;
    }

    public Anime save(Anime anime) {
        ANIMES.add(anime);
        return anime;
    }

    public void remove(Anime anime) {
        ANIMES.remove(anime);
    }

    public void replace(Anime oldAnime, Anime newAnime) {
        remove(oldAnime);
        save(newAnime);
    }

    public Optional<Anime> findByIdOrThrowNotFound(Long id) {
        return ANIMES.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
    }

    public Optional<Anime> findByName(String name) {
        return ANIMES.stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
