package dev.valente.producer;

import dev.valente.domain.Producer;
import dev.valente.dto.ProducerGetResponse;
import dev.valente.dto.ProducerPostRequest;
import dev.valente.dto.ProducerPutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerMapperService {

  private final ProducerMapper mapper;

  public Producer toProducer(ProducerPutRequest putRequest) {
    return mapper.toProducer(putRequest);
  }

  public Producer toProducer(ProducerPostRequest postRequest) {
    return mapper.toProducer(postRequest);
  }

  public ProducerGetResponse toProducerGetResponse(Producer producer) {
    return mapper.toProducerGetResponse(producer);
  }

}
