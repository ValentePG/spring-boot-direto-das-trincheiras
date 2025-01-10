package dev.valente.anime;

import dev.valente.domain.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    Anime toAnime(AnimePostRequest animePostRequest);

    Anime toAnime(AnimePutRequest request);

    AnimePostResponse toAnimePostResponse(Anime anime);


}
