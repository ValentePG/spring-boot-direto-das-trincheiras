package dev.valente.anime.service;

import dev.valente.anime.domain.Anime;
import dev.valente.anime.repository.AnimeRepositoryJPA;
import dev.valente.user_service.exception.NotFoundException;
import dev.valente.user_service.exception.UserNameAlreadyExists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepositoryJPA animeRepository;

    public List<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anime not Found"));
    }

    public Anime findByNameOrThrowNotFound(String name) {
        return animeRepository.findAnimeByName(name)
                .orElseThrow(() -> new NotFoundException("Anime not Found"));
    }

    public Anime save(Anime anime) {
        assertAnimeExists(anime.getName());
        return animeRepository.save(anime);
    }

    public void deleteById(Long id) {
        var anime = assertAnimeExists(id);
        animeRepository.delete(anime);
    }

    public void replace(Anime newAnime) {
        var oldAnime = assertAnimeExists(newAnime.getId());
        assertNameExists(newAnime.getName(),newAnime.getId());
        oldAnime.setName(newAnime.getName());
        animeRepository.save(oldAnime);
    }

    private Anime assertAnimeExists(Long id) {
        return findByIdOrThrowNotFound(id);
    }

    private void assertAnimeExists(String name) {
        animeRepository.findAnimeByName(name)
                .ifPresent(this::throwsNameAlreadyExists);
    }

    private void assertNameExists(String name, Long id) {
        animeRepository.findAnimeByNameAndIdNot(name, id)
                .ifPresent(this::throwsNameAlreadyExists);
    }

    private void throwsNameAlreadyExists(Anime anime) {
        throw new UserNameAlreadyExists("O nome %s já está cadastrado".formatted(anime.getName()));
    }
}
