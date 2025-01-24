package dev.valente.anime;

import dev.valente.api.AnimeControllerApi;
import dev.valente.dto.AnimeGetResponse;
import dev.valente.dto.AnimePostRequest;
import dev.valente.dto.AnimePostResponse;
import dev.valente.dto.AnimePutRequest;
import dev.valente.dto.PageAnimeGetResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/animes")
@SecurityRequirement(name = "basicAuth")
public class AnimeController implements AnimeControllerApi {

  private final AnimeService animeService;

  private final AnimeMapperService mapperService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<AnimeGetResponse>> findAllAnimes() {

    var animeGetResponse = animeService.findAll().stream().map(mapperService::toAnimeGetResponse).toList();

    return ResponseEntity.ok(animeGetResponse);
  }

  @Override
  @GetMapping("/paginated")
  public ResponseEntity<PageAnimeGetResponse> findAllAnimesPaginated(
      @Min(0) @Parameter(name = "page", description = "Zero-based page index (0..N)",
          in = ParameterIn.QUERY) @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @Min(1) @Parameter(name = "size", description = "The size of the page to be returned",
          in = ParameterIn.QUERY) @Valid @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
      @Parameter(name = "sort", description = "Sorting criteria in the format: property,(asc|desc). Default sort order is ascending."
          + " Multiple sort criteria are supported.",
          in = ParameterIn.QUERY) @Valid @RequestParam(value = "sort", required = false) List<String> sort,
      @ParameterObject final Pageable pageable
  ) {
    var jpaPageAnimeGetResponse = animeService.findAllPaginated(pageable);

    var pageAnimeGetResponse = mapperService.toPageAnimeGetResponse(jpaPageAnimeGetResponse);

    return ResponseEntity.ok(pageAnimeGetResponse);
  }

  @GetMapping("find")
  public ResponseEntity<AnimeGetResponse> findAnimeByName(@RequestParam String name) {

    var anime = animeService.findByNameOrThrowNotFound(name);

    var animeGetResponse = mapperService.toAnimeGetResponse(anime);

    return ResponseEntity.ok(animeGetResponse);

  }

  @GetMapping("{id}")
  public ResponseEntity<AnimeGetResponse> findAnimeById(@PathVariable Long id) {

    var anime = animeService.findByIdOrThrowNotFound(id);

    var animeGetResponse = mapperService.toAnimeGetResponse(anime);

    return ResponseEntity.ok(animeGetResponse);
  }

  @PostMapping
  public ResponseEntity<AnimePostResponse> createAnime(@RequestBody @Valid AnimePostRequest animePostRequest) {

    var anime = mapperService.toAnime(animePostRequest);
    var savedAnime = animeService.save(anime);
    var animePostResponse = mapperService.toAnimePostResponse(savedAnime);

    return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteAnimeById(@PathVariable Long id) {

    animeService.deleteById(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> updateAnime(@RequestBody @Valid AnimePutRequest request) {

    log.debug("Request to update anime : {}", request);

    var animeToRemove = mapperService.toAnime(request);
    animeService.replace(animeToRemove);

    return ResponseEntity.noContent().build();
  }

}
