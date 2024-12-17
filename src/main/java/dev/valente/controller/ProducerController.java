package dev.valente.controller;

import dev.valente.domain.Producer;
import dev.valente.exceptions.ExceptionTest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@RestController
@RequestMapping("/v1/producers")
public class ProducerController {

//    @GetMapping
//    public ResponseEntity<ProducerDTO> listAll(HttpServletRequest request) {
//        List<String> list = List.of("Naruto", "DBZ", "Inuyasha", "Digimon");
//        var uri = request.getRequestURI();
//        var dto = new ProducerDTO(list, uri);
//        log.info(Thread.currentThread().getName());
//        return ResponseEntity.ok().body(dto);
//    }

    @GetMapping
    public List<Producer> findAll() {
        return Producer.getProducers();
    }

    @GetMapping("find")
    public ResponseEntity<Producer> findFiltered(@RequestParam String name) {

        Optional<Producer> producer = Producer.getProducers().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();



        return producer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("{producerId}")
    public Producer findVariable(@PathVariable Long producerId) {

        Optional<Producer> anime = Producer.getProducers().stream()
                .filter(a -> a.getId().equals(producerId)).findFirst();

        return anime.orElseThrow(() -> new ExceptionTest("Producer not found"));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "x-api-key")
    public ResponseEntity<Producer> create(@RequestBody Producer producer, @RequestHeader HttpHeaders headers, HttpServletRequest request) {

        log.info("{}", headers);
        //              ^^ Informação daqui de cima vem da função aqui de baixo, provavelmente
        // request.getHeaderNames().asIterator().forEachRemaining(name -> log.info("{}: {}", name, request.getHeader(name)));

        producer.setId(ThreadLocalRandom.current().nextLong(1,10000));
        Producer.save(producer);
        return ResponseEntity.status(HttpStatus.CREATED).body(producer);
    }

}
