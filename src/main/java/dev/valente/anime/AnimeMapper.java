package dev.valente.anime;

import dev.valente.domain.Anime;
import dev.valente.dto.AnimeGetResponse;
import dev.valente.dto.AnimePostRequest;
import dev.valente.dto.AnimePostResponse;
import dev.valente.dto.AnimePutRequest;
import dev.valente.dto.PageAnimeGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

  AnimeGetResponse toAnimeGetResponse(Anime anime);

  Anime toAnime(AnimePostRequest animePostRequest);

  Anime toAnime(AnimePutRequest request);

  AnimePostResponse toAnimePostResponse(Anime anime);

  PageAnimeGetResponse toPageAnimeGetResponse(Page<Anime> jpaPageAnimeGetResponse);
}
