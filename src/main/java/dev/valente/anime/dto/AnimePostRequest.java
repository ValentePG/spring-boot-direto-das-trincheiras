package dev.valente.anime.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePostRequest {

    @NotBlank(message = "O nome não pode estar em branco")
    private String name;

}
