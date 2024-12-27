package dev.valente.anime.repository;

import dev.valente.anime.domain.Anime;
import dev.valente.common.AnimeDataUtil;
import dev.valente.common.DataUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeRepositoryTest {

    @InjectMocks
    private AnimeRepository animeRepository;

    @Mock
    private AnimeData animeData;

    private DataUtil<Anime> dataUtil;

    @BeforeEach
    void setUp() {
        dataUtil = new AnimeDataUtil();
    }

    @Test
    @Order(1)
    @DisplayName("Should return all animes")
    void findAll_ReturnsAllAnimes_whenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(dataUtil.getList());

        var listAnimes = animeRepository.findAll();

        Assertions.assertThat(listAnimes)
                .hasSameSizeAs(dataUtil.getList());

    }

    @Test
    @Order(2)
    @DisplayName("Should return anime by given ID")
    void findById_ReturnsAnime_whenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(dataUtil.getList());

        var firstAnime = dataUtil.getList().getFirst();

        var anime = animeRepository.findById(firstAnime.getId());

        Assertions.assertThat(anime)
                .isPresent()
                .contains(firstAnime);
    }

    @Test
    @Order(3)
    @DisplayName("Should return anime by given name")
    void findByName_ReturnsAnime_whenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(dataUtil.getList());

        var firstAnime = dataUtil.getList().getFirst();

        var anime = animeRepository.findByName(firstAnime.getName());

        Assertions.assertThat(anime)
                .isPresent()
                .contains(firstAnime);
    }

    @Test
    @Order(4)
    @DisplayName("Should save and return Anime")
    void save_SaveAndReturnsAnime_whenSuccessful() {
        var animeToSave = Anime.builder().id(55L).name("SNK").build();
        BDDMockito.when(animeData.getAnimes()).thenReturn(dataUtil.getList());

        var anime = animeRepository.save(animeToSave);

        Assertions.assertThat(anime)
                .hasFieldOrPropertyWithValue("id", anime.getId())
                .hasFieldOrPropertyWithValue("name", anime.getName())
                .isIn(dataUtil.getList());
    }

    @Test
    @Order(5)
    @DisplayName("Should remove Anime")
    void remove_RemovesAnime_whenSuccessful() {
        var animeToRemove = dataUtil.getList().getFirst();

        BDDMockito.when(animeData.getAnimes()).thenReturn(dataUtil.getList());

        animeRepository.remove(animeToRemove);

        Assertions.assertThat(animeToRemove)
                .isNotIn(dataUtil.getList());
    }

    @Test
    @Order(6)
    @DisplayName("Should replace Anime")
    void replace_ReplaceAnime_whenSuccessful() {

        String newName = "Orbe";

        var animeToReplace = dataUtil.getList().getFirst();

        var animeToSave = Anime.builder().id(animeToReplace.getId()).name(newName).build();

        BDDMockito.when(animeData.getAnimes()).thenReturn(dataUtil.getList());

        animeRepository.replace(animeToReplace, animeToSave);

        Assertions.assertThat(animeToReplace)
                        .isNotIn(dataUtil.getList());

        Assertions.assertThat(animeToSave)
                .doesNotMatch(n -> animeToReplace.getName().equalsIgnoreCase(n.getName()))
                .isIn(dataUtil.getList());
    }
}