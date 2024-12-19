package dev.valente.anime.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Anime {

    private Long id;

    private String name;

    @Getter
    private static List<Anime> animes = new ArrayList<>();

    static {
        animes.add(new Anime(1L, "Boku No Hero"));
        animes.add(new Anime(2L, "Naruto"));
        animes.add(new Anime(3L, "DBZ"));
        animes.add(new Anime(4L, "HXH"));
        animes.add(new Anime(5L, "SNK"));
    }


    public static void save(Anime anime) {
        animes.add(anime);
    }
}
