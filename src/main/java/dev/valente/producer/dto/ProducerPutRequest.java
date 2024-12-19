package dev.valente.producer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProducerPutRequest {

    private Long id;
    private String name;
}
