package dev.valente.anime.service;

import dev.valente.anime.domain.Anime;
import dev.valente.anime.repository.AnimeRepository;
import dev.valente.user_service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anime not Found"));
    }

    public Anime findByNameOrThrowNotFound(String name) {
        return animeRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Anime not Found"));
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
