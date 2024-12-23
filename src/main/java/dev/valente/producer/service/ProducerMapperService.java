package dev.valente.producer.service;

import dev.valente.producer.domain.Producer;
import dev.valente.producer.dto.ProducerGetResponse;
import dev.valente.producer.dto.ProducerPostRequest;
import dev.valente.producer.dto.ProducerPutRequest;
import dev.valente.producer.mapper.ProducerMapper;
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
