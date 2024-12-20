package dev.valente.anime.service;

import dev.valente.anime.domain.Anime;
import dev.valente.anime.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return animeRepository.findByIdOrThrowNotFound(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not Found"));
    }

    public Anime findByNameOrThrowNotFound(String name) {
        return animeRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not Found"));
    }

    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }

    public void deleteById(Long id) {
        var anime = assertAnimeExists(id);
        animeRepository.remove(anime);
    }

    public void replace(Anime newAnime) {

        var oldAnime = assertAnimeExists(newAnime.getId());

        animeRepository.replace(oldAnime, newAnime);
    }

    private Anime assertAnimeExists(Long id) {
        return findByIdOrThrowNotFound(id);
    }
}
