package dev.valente.producer.mapper;

import dev.valente.producer.domain.Producer;
import dev.valente.producer.dto.ProducerGetResponse;
import dev.valente.producer.dto.ProducerPostRequest;
import dev.valente.producer.dto.ProducerPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 10000))")
    Producer toProducer(ProducerPostRequest postRequest);

    Producer toProducer(ProducerPutRequest putRequest);

    ProducerGetResponse toProducerGetResponse(Producer producer);

}
