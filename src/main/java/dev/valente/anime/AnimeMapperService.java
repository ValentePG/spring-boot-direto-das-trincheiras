package dev.valente.anime;

import dev.valente.domain.Anime;
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
