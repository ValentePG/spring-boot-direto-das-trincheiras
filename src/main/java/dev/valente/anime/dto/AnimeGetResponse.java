package dev.valente.anime.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimeGetResponse {

    private Long id;
    private String name;


}
