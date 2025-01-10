package dev.valente.anime;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePostResponse {

    private Long id;
    private String name;
}
