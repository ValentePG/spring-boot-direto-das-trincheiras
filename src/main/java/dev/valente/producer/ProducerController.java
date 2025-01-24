package dev.valente.producer;

import dev.valente.api.ProducerControllerApi;
import dev.valente.dto.ProducerGetResponse;
import dev.valente.dto.ProducerPostRequest;
import dev.valente.dto.ProducerPutRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/v1/producers")
@SecurityRequirement(name = "basicAuth")
public class ProducerController implements ProducerControllerApi {

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
  public ResponseEntity<ProducerGetResponse> createProducer(@RequestBody @Valid ProducerPostRequest producerPostRequest) {

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
