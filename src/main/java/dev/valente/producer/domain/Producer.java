package dev.valente.producer.domain;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Producer {

    private Long id;
    private String name;
    private LocalDateTime createdAt;

}
