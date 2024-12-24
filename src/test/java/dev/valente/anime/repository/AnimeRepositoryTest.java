package dev.valente.anime.repository;

import dev.valente.anime.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeRepositoryTest {

    @InjectMocks
    private AnimeRepository animeRepository;

    @Mock
    private AnimeData animeData;

    private final List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        var anime1 = Anime.builder().id(1L).name("Naruto").build();
        var anime2 = Anime.builder().id(2L).name("DBZ").build();
        var anime3 = Anime.builder().id(3L).name("HXH").build();
        animeList.addAll(List.of(anime1, anime2, anime3));
    }

    @Test
    @Order(1)
    @DisplayName("Should return all animes")
    void findAll_ReturnsAllAnimes_whenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var listAnimes = animeRepository.findAll();

        Assertions.assertThat(listAnimes)
                .hasSameSizeAs(animeList);

    }

    @Test
    @Order(2)
    @DisplayName("Should return anime by given ID")
    void findById_ReturnsAnime_whenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var firstAnime = animeList.getFirst();

        var anime = animeRepository.findById(firstAnime.getId());

        Assertions.assertThat(anime)
                .isPresent()
                .contains(firstAnime);
    }

    @Test
    @Order(3)
    @DisplayName("Should return anime by given name")
    void findByName_ReturnsAnime_whenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var firstAnime = animeList.getFirst();

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
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var anime = animeRepository.save(animeToSave);

        Assertions.assertThat(anime)
                .hasFieldOrPropertyWithValue("id", anime.getId())
                .hasFieldOrPropertyWithValue("name", anime.getName())
                .isIn(animeList);
    }

    @Test
    @Order(5)
    @DisplayName("Should remove Anime")
    void remove_RemovesAnime_whenSuccessful() {
        var animeToRemove = animeList.getFirst();

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        animeRepository.remove(animeToRemove);

        Assertions.assertThat(animeToRemove)
                .isNotIn(animeList);
    }

    @Test
    @Order(6)
    @DisplayName("Should replace Anime")
    void replace_ReplaceAnime_whenSuccessful() {

        String newName = "Orbe";

        var animeToReplace = animeList.getFirst();

        var animeToSave = Anime.builder().id(animeToReplace.getId()).name(newName).build();

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        animeRepository.replace(animeToReplace, animeToSave);

        Assertions.assertThat(animeToReplace)
                        .isNotIn(animeList);

        Assertions.assertThat(animeToSave)
                .doesNotMatch(n -> animeToReplace.getName().equalsIgnoreCase(n.getName()))
                .isIn(animeList);
    }
}