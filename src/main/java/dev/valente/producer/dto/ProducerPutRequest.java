package dev.valente.producer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProducerPutRequest {

    private Long id;
    private String name;
}
