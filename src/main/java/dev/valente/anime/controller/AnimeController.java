package dev.valente.anime.controller;

import dev.valente.anime.domain.Anime;
import dev.valente.anime.dto.AnimeGetResponse;
import dev.valente.anime.dto.AnimePostRequest;
import dev.valente.anime.dto.AnimePostResponse;
import dev.valente.anime.dto.AnimePutRequest;
import dev.valente.anime.mapper.AnimeMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/v1/animes")
public class AnimeController {

    private static final AnimeMapper MAPPER = Mappers.getMapper(AnimeMapper.class);

//    @GetMapping
//    public ResponseEntity<AnimeDTO> listAll(HttpServletRequest request) {
//        List<String> list = List.of("Naruto", "DBZ", "Inuyasha", "Digimon");
//        var uri = request.getRequestURI();
//        var dto = new AnimeDTO(list, uri);
//        log.info(Thread.currentThread().getName());
//        return ResponseEntity.ok().body(dto);
//    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AnimeGetResponse>> findAll() {

        var lista = Anime.getAnimes();

        var animeGetResponse = lista.stream().map(MAPPER::toAnimeGetResponse).toList();

        return ResponseEntity.ok(animeGetResponse);
    }

    @GetMapping("find")
    public ResponseEntity<AnimeGetResponse> findFiltered(@RequestParam String name) {

        Optional<Anime> anime = Anime.getAnimes().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name)).findFirst();


        return anime.map(a -> ResponseEntity.ok(MAPPER.toAnimeGetResponse(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("{animeId}")
    public ResponseEntity<AnimeGetResponse> findVariable(@PathVariable Long animeId) {

        Optional<Anime> anime = Anime.getAnimes().stream()
                .filter(a -> a.getId().equals(animeId)).findFirst();

        return anime.map(a -> ResponseEntity.ok(MAPPER.toAnimeGetResponse(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> create(@RequestBody AnimePostRequest animePostRequest) {

        var anime = MAPPER.toAnime(animePostRequest);
        Anime.save(anime);
        var animePostResponse = MAPPER.toAnimePostResponse(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @DeleteMapping("{animeId}")
    public ResponseEntity<Void> delete(@PathVariable Long animeId) {

        var anime = Anime.getAnimes().stream()
                .filter(a -> a.getId().equals(animeId)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));

        Anime.getAnimes().remove(anime);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest request) {

        log.debug("Request to update anime : {}", request);

        var animeToRemove = Anime.getAnimes().stream()
                .filter(a -> a.getId().equals(request.getId())).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));

        var animeUpdated = MAPPER.toAnime(request);

        Anime.getAnimes().remove(animeToRemove);
        Anime.getAnimes().add(animeUpdated);

        return ResponseEntity.noContent().build();
    }

}
