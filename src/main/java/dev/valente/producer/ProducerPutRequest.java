package dev.valente.producer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProducerPutRequest {

    @NotNull(message = "O id não pode ser nulo")
    private Long id;
    @NotBlank(message = "O nome não pode estar em branco")
    private String name;
}
