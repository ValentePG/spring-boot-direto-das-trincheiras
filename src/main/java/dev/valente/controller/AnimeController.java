package dev.valente.controller;

import dev.valente.domain.Anime;
import dev.valente.exceptions.ExceptionTest;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@RestController
@RequestMapping("/v1/animes")
public class AnimeController {

//    @GetMapping
//    public ResponseEntity<AnimeDTO> listAll(HttpServletRequest request) {
//        List<String> list = List.of("Naruto", "DBZ", "Inuyasha", "Digimon");
//        var uri = request.getRequestURI();
//        var dto = new AnimeDTO(list, uri);
//        log.info(Thread.currentThread().getName());
//        return ResponseEntity.ok().body(dto);
//    }

    @GetMapping
    public List<Anime> findAll() {
        return Anime.getAnimes();
    }

    @GetMapping("find")
    public ResponseEntity<Anime> findFiltered(@RequestParam String name) {

        Optional<Anime> anime = Anime.getAnimes().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();



        return anime.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("{animeId}")
    public Anime findVariable(@PathVariable Long animeId) {

        Optional<Anime> anime = Anime.getAnimes().stream()
                .filter(a -> a.getId().equals(animeId)).findFirst();

        return anime.orElseThrow(() -> new ExceptionTest("Anime not found"));
    }

    @PostMapping
    public ResponseEntity<Anime> create(@RequestBody Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextLong(1,10000));
        Anime.save(anime);
        return ResponseEntity.status(HttpStatus.CREATED).body(anime);
    }

}
