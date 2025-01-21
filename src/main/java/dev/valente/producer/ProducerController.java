package dev.valente.producer;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/producers")
@SecurityRequirement(name = "basicAuth")
public class ProducerController {

    private final ProducerService producerService;
    private final ProducerMapperService producerMapperService;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> findAllProducers() {

        var producerGetResponse = producerService.findAll()
                .stream()
                .map(producerMapperService::toProducerGetResponse)
                .toList();

        return ResponseEntity.ok(producerGetResponse);
    }

    @GetMapping("find")
    public ResponseEntity<ProducerGetResponse> findProducerByName(@RequestParam String name) {


        var producer = producerService.findByNameOrThrowNotFound(name);
        var producerGetResponse = producerMapperService.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);

    }

    @GetMapping("{producerId}")
    public ResponseEntity<ProducerGetResponse> findProducerById(@PathVariable Long producerId) {

        var producer = producerService.findByIdOrThrowNotFound(producerId);

        var producerGetResponse = producerMapperService.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = {"x-api-key", "x-api-teste"})
    public ResponseEntity<ProducerGetResponse> createProducer(@RequestBody @Valid ProducerPostRequest producerPostRequest,
                                                      @RequestHeader HttpHeaders headers,
                                                      HttpServletRequest request) {

        var producer = producerMapperService.toProducer(producerPostRequest);
        var producerSaved = producerService.save(producer);
        var producerGetResponse = producerMapperService.toProducerGetResponse(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerGetResponse);
    }

    @DeleteMapping("{producerId}")
    public ResponseEntity<Void> deleteProducerById(@PathVariable Long producerId) {
        log.info("Deleting producer with id {}", producerId);

        producerService.delete(producerId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> replaceProducer(@RequestBody @Valid ProducerPutRequest producerPutRequest) {
        log.info("Updating producer with id {}", producerPutRequest.getId());

        var producerToRemove = producerMapperService.toProducer(producerPutRequest);

        producerService.replace(producerToRemove);

        return ResponseEntity.noContent().build();
    }

}
