package dev.valente.anime;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/animes")
@SecurityRequirement(name = "basicAuth")
public class AnimeController {

    private final AnimeService animeService;

    private final AnimeMapperService mapperService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AnimeGetResponse>> findAll() {

        var animeGetResponse = animeService.findAll().stream().map(mapperService::toAnimeGetResponse).toList();

        return ResponseEntity.ok(animeGetResponse);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<AnimeGetResponse>> findAllPaginated(@ParameterObject Pageable pageable) {

        var animePage = animeService.findAllPaginated(pageable).map(mapperService::toAnimeGetResponse);

        return ResponseEntity.ok(animePage);
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
    public ResponseEntity<AnimePostResponse> create(@RequestBody @Valid AnimePostRequest animePostRequest) {

        var anime = mapperService.toAnime(animePostRequest);
        var savedAnime = animeService.save(anime);
        var animePostResponse = mapperService.toAnimePostResponse(savedAnime);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        animeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {

        log.debug("Request to update anime : {}", request);

        var animeToRemove = mapperService.toAnime(request);
        animeService.replace(animeToRemove);

        return ResponseEntity.noContent().build();
    }

}
