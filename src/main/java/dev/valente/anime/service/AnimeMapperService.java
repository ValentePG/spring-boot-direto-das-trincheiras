package dev.valente.anime.service;

import dev.valente.anime.domain.Anime;
import dev.valente.anime.dto.AnimeGetResponse;
import dev.valente.anime.dto.AnimePostRequest;
import dev.valente.anime.dto.AnimePostResponse;
import dev.valente.anime.dto.AnimePutRequest;
import dev.valente.anime.mapper.AnimeMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
public class AnimeMapperService {

    private static final AnimeMapper MAPPER = Mappers.getMapper(AnimeMapper.class);

    public AnimeGetResponse toAnimeGetResponse(Anime anime){
        return MAPPER.toAnimeGetResponse(anime);
    }
    public Anime toAnime(AnimePostRequest animePostRequest){
        return MAPPER.toAnime(animePostRequest);
    }

    public Anime toAnime(AnimePutRequest request){
        return MAPPER.toAnime(request);
    }

    public AnimePostResponse toAnimePostResponse(Anime anime){
        return MAPPER.toAnimePostResponse(anime);
    }
}
