package dev.valente.common;

import dev.valente.anime.domain.Anime;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeDataUtil implements DataUtil<Anime> {

    private final List<Anime> ANIME_LIST = new ArrayList<>();

    {
        ANIME_LIST.add(Anime.builder().id(1L).name("Naruto").build());
        ANIME_LIST.add(Anime.builder().id(2L).name("DBZ").build());
        ANIME_LIST.add(Anime.builder().id(3L).name("HXH").build());
    }

    @Override
    public List<Anime> getList() {
        return ANIME_LIST;
    }
}
