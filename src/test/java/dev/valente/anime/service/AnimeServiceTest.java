package dev.valente.anime.service;

import dev.valente.anime.domain.Anime;
import dev.valente.anime.repository.AnimeRepository;
import dev.valente.common.AnimeDataUtil;
import dev.valente.common.DataUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    private DataUtil<Anime> dataUtil;

    @BeforeEach
    public void setUp() {
        dataUtil = new AnimeDataUtil();
    }

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
    @Order(2)
    @DisplayName("Should return anime by given ID")
    void findByIdOrThrowException_shouldReturnProducer_whenSuccessfull() {
        var expectedAnime = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));

        var anime = animeService.findByIdOrThrowNotFound(expectedAnime.getId());

        Assertions.assertThat(anime)
                .isEqualTo(expectedAnime);
    }

    @Test
    @Order(3)
    @DisplayName("Should throw not found exception")
    void findByIdOrThrowException_shouldThrowException_whenAnimeNotFound() {
        var expectedAnime = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findById(expectedAnime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> animeService.findByIdOrThrowNotFound(expectedAnime.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(4)
    @DisplayName("Should return anime by given name")
    void findByNameOrThrowException_shouldReturnProducer_whenSuccessfull() {
        var expectedAnime = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findByName(expectedAnime.getName())).thenReturn(Optional.of(expectedAnime));

        var anime = animeService.findByNameOrThrowNotFound(expectedAnime.getName());

        Assertions.assertThat(anime)
                .isEqualTo(expectedAnime);
    }

    @Test
    @Order(5)
    @DisplayName("Should throw not found exception")
    void findByNameOrThrowException_shouldThrowException_whenAnimeNotFound() {
        var expectedAnime = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findByName(expectedAnime.getName())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> animeService.findByNameOrThrowNotFound(expectedAnime.getName()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(6)
    @DisplayName("Should save anime and return anime saved")
    void save_ShouldSaveAnime_whenSuccessfull() {

        var animeToSave = Anime.builder().id(14L).name("AlgumAnime").build();

        BDDMockito.when(animeRepository.save(animeToSave)).thenReturn(animeToSave);

        var anime = animeService.save(animeToSave);

        Assertions.assertThat(anime)
                .isEqualTo(animeToSave);
    }

    @Test
    @Order(7)
    @DisplayName("Should remove anime")
    void remove_ShouldRemoveAnime_whenSuccessfull() {
        var animeToRemove = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findById(animeToRemove.getId())).thenReturn(Optional.of(animeToRemove));

        BDDMockito.doNothing().when(animeRepository).remove(animeToRemove);

        Assertions.assertThatNoException()
                .isThrownBy(() -> animeService.deleteById(animeToRemove.getId()));

        Mockito.verify(animeRepository, Mockito.times(1)).findById(animeToRemove.getId());
        Mockito.verify(animeRepository, Mockito.times(1)).remove(animeToRemove);
    }

    @Test
    @Order(8)
    @DisplayName("Should throw not found exception")
    void remove_ShouldThrowNotFoundException_whenSuccessfull() {
        var animeToRemove = dataUtil.getList().getFirst();

        BDDMockito.when(animeRepository.findById(animeToRemove.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> animeService.deleteById(animeToRemove.getId()))
                .isInstanceOf(ResponseStatusException.class);

        Mockito.verify(animeRepository, Mockito.times(1)).findById(animeToRemove.getId());
        Mockito.verify(animeRepository, Mockito.times(0)).remove(animeToRemove);
    }

    @Test
    @Order(9)
    @DisplayName("Should replace existent anime by new anime")
    void replace_ShouldReplaceAnime_whenSuccessfull() {
        var animeToReplace = dataUtil.getList().getFirst();
        var newAnime = Anime.builder().id(animeToReplace.getId())
                .name("AlgumAnime")
                .build();

        BDDMockito.when(animeRepository.findById(animeToReplace.getId())).thenReturn(Optional.of(animeToReplace));
        BDDMockito.doNothing().when(animeRepository).replace(animeToReplace, newAnime);


        Assertions.assertThatNoException()
                .isThrownBy(() -> animeService.replace(newAnime));

        Mockito.verify(animeRepository, Mockito.times(1)).findById(animeToReplace.getId());
        Mockito.verify(animeRepository, Mockito.times(1)).replace(animeToReplace, newAnime);
    }

    @Test
    @Order(10)
    @DisplayName("Should throw not found exception")
    void replace_ShouldThrowNotFoundException_whenSuccessfull() {

        var animeToReplace = dataUtil.getList().getFirst();
        var newAnime = Anime.builder().id(animeToReplace.getId())
                .name("AlgumAnime")
                .build();

        BDDMockito.when(animeRepository.findById(animeToReplace.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> animeService.replace(newAnime))
                .isInstanceOf(ResponseStatusException.class);

        Mockito.verify(animeRepository, Mockito.times(1)).findById(animeToReplace.getId());
        Mockito.verify(animeRepository, Mockito.times(0)).replace(animeToReplace, newAnime);
    }
}