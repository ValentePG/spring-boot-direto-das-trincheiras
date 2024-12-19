package dev.valente.producer.service;

import dev.valente.producer.domain.Producer;
import dev.valente.producer.dto.ProducerGetResponse;
import dev.valente.producer.dto.ProducerPostRequest;
import dev.valente.producer.dto.ProducerPutRequest;
import dev.valente.producer.mapper.ProducerMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MapperService {
    private final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    public Producer toProducer(ProducerPutRequest putRequest) {
        return MAPPER.toProducer(putRequest);
    }

    public Producer toProducer(ProducerPostRequest postRequest){
        return MAPPER.toProducer(postRequest);
    };

    public ProducerGetResponse toProducerGetResponse(Producer producer){
        return MAPPER.toProducerGetResponse(producer);
    }
}
