package dev.valente.anime;

import dev.valente.domain.Anime;
import dev.valente.common.AnimeDataUtil;
import dev.valente.user_service.exception.NotFoundException;
import dev.valente.user_service.exception.UserNameAlreadyExists;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    private final AnimeDataUtil dataUtil = new AnimeDataUtil();

    @Test
    @Order(1)
    @DisplayName("Should return List of animes")
    void findAll_shouldReturnAllAnimes_whenSuccessfull() {

        BDDMockito.when(animeRepository.findAll()).thenReturn(dataUtil.getList());

        var anime = animeService.findAll();

        Assertions.assertThat(anime).isNotEmpty()
                .hasSameSizeAs(dataUtil.getList());

    }

    @Test
    @DisplayName("Should return a page of animes")
    @Order(2)
    void findAllPaginated_shouldReturnPageOfAnimes_whenSuccessful() throws Exception {


        var pageRequest = PageRequest.of(0, dataUtil.getList().size());

        var animePage = new PageImpl<>(dataUtil.getList(), pageRequest, 1);

        BDDMockito.when(animeRepository.findAll(BDDMockito.any(Pageable.class))).thenReturn(animePage);

        var animesFound = animeService.findAllPaginated(pageRequest);

        Assertions.assertThat(animesFound).hasSameElementsAs(dataUtil.getList());


    }

    @Test
    @Order(3)
    @DisplayName("Should return anime by given ID")
    void findByIdOrThrowException_shouldReturnProducer_whenSuccessfull() {
        var expectedAnime = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));

        var anime = animeService.findByIdOrThrowNotFound(expectedAnime.getId());

        Assertions.assertThat(anime)
                .isEqualTo(expectedAnime);
    }

    @Test
    @Order(4)
    @DisplayName("Should throw not found exception")
    void findByIdOrThrowException_shouldThrowException_whenAnimeNotFound() {
        var expectedAnime = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findById(expectedAnime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> animeService.findByIdOrThrowNotFound(expectedAnime.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("404 NOT_FOUND \"Anime not Found\"");
    }

    @Test
    @Order(5)
    @DisplayName("Should return anime by given name")
    void findByNameOrThrowException_shouldReturnProducer_whenSuccessfull() {
        var expectedAnime = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findAnimeByName(expectedAnime.getName())).thenReturn(Optional.of(expectedAnime));

        var anime = animeService.findByNameOrThrowNotFound(expectedAnime.getName());

        Assertions.assertThat(anime)
                .isEqualTo(expectedAnime);
    }

    @Test
    @Order(6)
    @DisplayName("Should throw not found exception")
    void findByNameOrThrowException_shouldThrowException_whenAnimeNotFound() {
        var expectedAnime = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findAnimeByName(expectedAnime.getName())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> animeService.findByNameOrThrowNotFound(expectedAnime.getName()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("404 NOT_FOUND \"Anime not Found\"");
    }

    @Test
    @Order(7)
    @DisplayName("Should save anime and return anime saved")
    void save_ShouldSaveAnime_whenSuccessfull() {

        var animeToSave = Anime.builder().name("AlgumAnime").build();

        var animeSaved = Anime.builder().name("AlgumAnime").id(14L).build();

        BDDMockito.when(animeRepository.findAnimeByName(animeToSave.getName())).thenReturn(Optional.empty());

        BDDMockito.when(animeRepository.save(animeToSave)).thenReturn(animeSaved);

        var anime = animeService.save(animeToSave);

        Assertions.assertThat(anime)
                .isEqualTo(animeSaved);
    }

    @Test
    @Order(8)
    @DisplayName("Should return name already exists")
    void save_ShouldReturnNameAlreadyExists_whenFailed() {

        var anime = dataUtil.getAnimeToFind();

        BDDMockito.when(animeRepository.findAnimeByName(anime.getName())).thenReturn(Optional.of(anime));

        Assertions.assertThatThrownBy(() -> animeService.save(anime))
                .isInstanceOf(UserNameAlreadyExists.class)
                .hasMessage("400 BAD_REQUEST \"O nome %s já está cadastrado\"".formatted(anime.getName()));
    }

    @Test
    @Order(9)
    @DisplayName("Should remove anime")
    void remove_ShouldRemoveAnime_whenSuccessfull() {
        var animeToRemove = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findById(animeToRemove.getId())).thenReturn(Optional.of(animeToRemove));

        BDDMockito.doNothing().when(animeRepository).delete(animeToRemove);

        Assertions.assertThatNoException()
                .isThrownBy(() -> animeService.deleteById(animeToRemove.getId()));

        Mockito.verify(animeRepository, Mockito.times(1)).findById(animeToRemove.getId());
        Mockito.verify(animeRepository, Mockito.times(1)).delete(animeToRemove);
    }

    @Test
    @Order(10)
    @DisplayName("Should throw not found exception")
    void remove_ShouldThrowNotFoundException_whenSuccessfull() {
        var animeToRemove = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findById(animeToRemove.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> animeService.deleteById(animeToRemove.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("404 NOT_FOUND \"Anime not Found\"");

        Mockito.verify(animeRepository, Mockito.times(1)).findById(animeToRemove.getId());
        Mockito.verify(animeRepository, Mockito.times(0)).delete(animeToRemove);
    }

    @Test
    @Order(11)
    @DisplayName("Should replace existent anime by new anime")
    void replace_ShouldReplaceAnime_whenSuccessfull() {
        var animeToReplace = dataUtil.getList().getFirst();
        var newAnime = Anime.builder().id(animeToReplace.getId())
                .name("AlgumAnime")
                .build();

        BDDMockito.when(animeRepository.findById(animeToReplace.getId())).thenReturn(Optional.of(animeToReplace));
        BDDMockito.when(animeRepository.save(animeToReplace)).thenReturn(animeToReplace);
        BDDMockito.when(animeRepository.findAnimeByNameAndIdNot(newAnime.getName(), newAnime.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatNoException()
                .isThrownBy(() -> animeService.replace(newAnime));

        Mockito.verify(animeRepository, Mockito.times(1)).findById(animeToReplace.getId());
        Mockito.verify(animeRepository, Mockito.times(1)).save(animeToReplace);
        Mockito.verify(animeRepository, Mockito.times(1)).findAnimeByNameAndIdNot(newAnime.getName(), newAnime.getId());
    }

    @Test
    @Order(12)
    @DisplayName("Should throw name already exists")
    void replace_ShouldThrowNameLreadyExist_whenFailed() {

        var animeToReplace = dataUtil.getAnimeToFind();
        var newAnime = Anime.builder().id(animeToReplace.getId())
                .name("AlgumAnime")
                .build();

        BDDMockito.when(animeRepository.findById(animeToReplace.getId())).thenReturn(Optional.of(animeToReplace));
        BDDMockito.when(animeRepository.findAnimeByNameAndIdNot(newAnime.getName(), newAnime.getId()))
                .thenReturn(Optional.of(animeToReplace));

        Assertions.assertThatThrownBy(() -> animeService.replace(newAnime))
                .isInstanceOf(UserNameAlreadyExists.class)
                .hasMessage("400 BAD_REQUEST \"O nome %s já está cadastrado\"".formatted(animeToReplace.getName()));

        Mockito.verify(animeRepository, Mockito.times(1)).findById(animeToReplace.getId());
        Mockito.verify(animeRepository, Mockito.times(0)).save(animeToReplace);
        Mockito.verify(animeRepository, Mockito.times(1)).findAnimeByNameAndIdNot(newAnime.getName(), newAnime.getId());
    }



    @Test
    @Order(13)
    @DisplayName("Should throw not found exception")
    void replace_ShouldThrowNotFoundException_whenSuccessfull() {

        var animeToReplace = dataUtil.getList().getFirst();
        var newAnime = Anime.builder().id(animeToReplace.getId())
                .name("AlgumAnime")
                .build();

        BDDMockito.when(animeRepository.findById(animeToReplace.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> animeService.replace(newAnime))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("404 NOT_FOUND \"Anime not Found\"");

        Mockito.verify(animeRepository, Mockito.times(1)).findById(animeToReplace.getId());
        Mockito.verify(animeRepository, Mockito.times(0)).save(animeToReplace);
    }
}