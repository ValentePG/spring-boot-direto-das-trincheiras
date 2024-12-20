package dev.valente.anime.mapper;

import dev.valente.anime.domain.Anime;
import dev.valente.anime.dto.AnimeGetResponse;
import dev.valente.anime.dto.AnimePostRequest;
import dev.valente.anime.dto.AnimePostResponse;
import dev.valente.anime.dto.AnimePutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 10000))")
    Anime toAnime(AnimePostRequest animePostRequest);

    Anime toAnime(AnimePutRequest request);

    AnimePostResponse toAnimePostResponse(Anime anime);


}
