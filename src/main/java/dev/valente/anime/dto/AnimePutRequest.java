package dev.valente.anime.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimePutRequest {

    @NotNull(message = "O id não pode ser nulo")
    private Long id;
    @NotBlank(message = "O nome não pode estar em branco")
    private String name;


}
