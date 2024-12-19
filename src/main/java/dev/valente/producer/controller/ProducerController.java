package dev.valente.producer.controller;

import dev.valente.producer.dto.ProducerGetResponse;
import dev.valente.producer.dto.ProducerPostRequest;
import dev.valente.producer.dto.ProducerPutRequest;
import dev.valente.producer.service.MapperService;
import dev.valente.producer.service.ProducerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/v1/producers")
public class ProducerController {

    private final ProducerService producerService;
    private final MapperService mapperService;

    public ProducerController(ProducerService producerService, MapperService mapperService) {
        this.producerService = producerService;
        this.mapperService = mapperService;
    }


    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> findAll() {

        var producerGetResponse = producerService.findAll()
                .stream()
                .map(mapperService::toProducerGetResponse)
                .toList();

        return ResponseEntity.ok(producerGetResponse);
    }

    @GetMapping("find")
    public ResponseEntity<ProducerGetResponse> findByName(@RequestParam String name) {


        var producer = producerService.findByNameOrThrowNotFound(name);
        var producerGetResponse = mapperService.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);

    }

    @GetMapping("{producerId}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long producerId) {

        var producer = producerService.findByIdOrThrowNotFound(producerId);

        var producerGetResponse = mapperService.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = {"x-api-key", "x-api-teste"})
    public ResponseEntity<ProducerGetResponse> create(@RequestBody ProducerPostRequest producerPostRequest,
                                                      @RequestHeader HttpHeaders headers,
                                                      HttpServletRequest request) {

        var producer = mapperService.toProducer(producerPostRequest);
        var producerSaved = producerService.save(producer);
        var producerGetResponse = mapperService.toProducerGetResponse(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerGetResponse);
    }

    @DeleteMapping("{producerId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long producerId) {
        log.info("Deleting producer with id {}", producerId);

        producerService.delete(producerId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody ProducerPutRequest producerPutRequest) {
        log.info("Updating producer with id {}", producerPutRequest.getId());

        var producerToRemove = mapperService.toProducer(producerPutRequest);

        producerService.replace(producerToRemove);

        return ResponseEntity.noContent().build();
    }

}
