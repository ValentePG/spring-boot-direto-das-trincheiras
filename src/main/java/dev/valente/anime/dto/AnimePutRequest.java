package dev.valente.anime.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimePutRequest {

    private Long id;
    private String name;


}
