package dev.valente.common;

import dev.valente.anime.domain.Anime;
import dev.valente.producer.domain.Producer;
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

    public Anime getAnimeToSave() {
        return Anime.builder()
                .id(15L)
                .name("JoJo").build();
    }

    public Anime getNewAnime() {
        return Anime.builder()
                .id(getFirst().getId())
                .name("ORBE").build();
    }

    public Anime getAnimeToFind() {
        return getFirst();
    }

    public Anime getAnimeToReplace() {
        return getFirst();
    }

    public Anime getAnimeToRemove() {
        return getFirst();
    }

    private Anime getFirst() {
        return ANIME_LIST.getFirst();
    }
}
