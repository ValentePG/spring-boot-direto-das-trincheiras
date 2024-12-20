package dev.valente.anime.service;

import dev.valente.anime.domain.Anime;
import dev.valente.anime.dto.AnimeGetResponse;
import dev.valente.anime.dto.AnimePostRequest;
import dev.valente.anime.dto.AnimePostResponse;
import dev.valente.anime.dto.AnimePutRequest;
import dev.valente.anime.mapper.AnimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimeMapperService {

    private final AnimeMapper mapper;

    public AnimeGetResponse toAnimeGetResponse(Anime anime) {
        return mapper.toAnimeGetResponse(anime);
    }

    public Anime toAnime(AnimePostRequest animePostRequest) {
        return mapper.toAnime(animePostRequest);
    }

    public Anime toAnime(AnimePutRequest request) {
        return mapper.toAnime(request);
    }

    public AnimePostResponse toAnimePostResponse(Anime anime) {
        return mapper.toAnimePostResponse(anime);
    }
}
