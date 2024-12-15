package dev.valente.controller;

import dev.valente.dto.AnimeDTO;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/animes")
public class AnimeController {

    @GetMapping
    public ResponseEntity<AnimeDTO> listAll(HttpServletRequest request) {
        List<String> list = List.of("Naruto", "DBZ", "Inuyasha", "Digimon");
        var uri = request.getRequestURI();
        var dto = new AnimeDTO(list, uri);
        log.info(Thread.currentThread().getName());
        return ResponseEntity.ok().body(dto);
    }

}
