package dev.valente.anime.controller;

import dev.valente.anime.dto.AnimeGetResponse;
import dev.valente.anime.dto.AnimePostRequest;
import dev.valente.anime.dto.AnimePostResponse;
import dev.valente.anime.dto.AnimePutRequest;
import dev.valente.anime.service.AnimeMapperService;
import dev.valente.anime.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/animes")
public class AnimeController {

    private final AnimeService animeService;

    private final AnimeMapperService mapperService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AnimeGetResponse>> findAll() {

        var animeGetResponse = animeService.findAll().stream().map(mapperService::toAnimeGetResponse).toList();

        return ResponseEntity.ok(animeGetResponse);
    }

    @GetMapping("find")
    public ResponseEntity<AnimeGetResponse> findByName(@RequestParam String name) {

        var anime = animeService.findByNameOrThrowNotFound(name);

        var animeGetResponse = mapperService.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);

    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {

        var anime = animeService.findByIdOrThrowNotFound(id);

        var animeGetResponse = mapperService.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> create(@RequestBody AnimePostRequest animePostRequest) {

        var anime = mapperService.toAnime(animePostRequest);
        animeService.save(anime);
        var animePostResponse = mapperService.toAnimePostResponse(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        animeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest request) {

        log.debug("Request to update anime : {}", request);

        var animeToRemove = mapperService.toAnime(request);
        animeService.replace(animeToRemove);

        return ResponseEntity.noContent().build();
    }

}
