package dev.valente.anime.repository;

import dev.valente.anime.domain.Anime;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class AnimeData {

    private final List<Anime> animes = new ArrayList<>();

    {
        animes.add(new Anime(1L, "Boku No Hero"));
        animes.add(new Anime(2L, "Naruto"));
        animes.add(new Anime(3L, "DBZ"));
        animes.add(new Anime(4L, "HXH"));
        animes.add(new Anime(5L, "SNK"));
    }

}
