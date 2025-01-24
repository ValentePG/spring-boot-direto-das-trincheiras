package dev.valente.producer;

import dev.valente.domain.Producer;
import dev.valente.dto.ProducerGetResponse;
import dev.valente.dto.ProducerPostRequest;
import dev.valente.dto.ProducerPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {

  Producer toProducer(ProducerPostRequest postRequest);

  Producer toProducer(ProducerPutRequest putRequest);

  ProducerGetResponse toProducerGetResponse(Producer producer);

}
