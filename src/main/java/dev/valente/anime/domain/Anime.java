package dev.valente.anime.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;


}
