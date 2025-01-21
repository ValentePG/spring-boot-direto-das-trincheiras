package dev.valente.anime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimeGetResponse {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "overlord")
    private String name;


}
