package dev.valente.domain;



import lombok.Getter;

import java.util.List;

@Getter
public class Anime {

    private Long id;

    private String name;

    public Anime(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Anime() {

    }

    public static List<Anime> getAnimes() {
        return List.of(new Anime(1L, "Boku No Hero"),
                new Anime(2L, "Naruto"),
                new Anime(3L, "Chainsaw Man"),
                new Anime(4L, "DBZ"),
                new Anime(5L, "Tokyo Ghoul"));
    }
}
