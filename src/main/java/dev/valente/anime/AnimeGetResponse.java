package dev.valente.anime;

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
