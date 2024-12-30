package dev.valente.anime.controller;

import dev.valente.anime.dto.AnimePostRequest;
import dev.valente.anime.repository.AnimeData;
import dev.valente.anime.service.AnimeMapperService;
import dev.valente.common.AnimeDataUtil;
import dev.valente.common.FileUtil;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "dev.valente")
class AnimeControllerTest {

    private final String URL = "/v1/animes";

    @InjectMocks
    private AnimeController controller;

    @MockBean
    private AnimeData animeData;

    @Autowired
    private AnimeDataUtil dataUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtil fileUtil;

    @SpyBean
    private AnimeMapperService animeMapperService;

    @BeforeEach
    void setUp() {
        mockList();
    }

    @Test
    @DisplayName("GET /v1/animes should return a list of all animes")
    @Order(1)
    void findAll_shouldReturnAllAnimes_whenSuccessful() throws Exception {

        var response = fileUtil.readResourceFile("anime/get/request/get_allanimes_200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /v1/animes/{id} should return anime by given id")
    @Order(2)
    void findById_shouldReturnAnime_whenSuccessful() throws Exception {

        var id = dataUtil.getAnimeToFind().getId();

        var response = fileUtil.readResourceFile("anime/get/request/get_findbyid_200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /v1/animes/{id} should return NOT FOUND")
    @Order(3)
    void findById_shouldReturnNotFound_whenFailed() throws Exception {

        Long id = 50L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }

    @Test
    @DisplayName("GET /v1/animes/find?name=Naruto should return anime by given name")
    @Order(4)
    void findByName_shouldReturnAnime_whenSuccessful() throws Exception {

        var response = fileUtil.readResourceFile("anime/get/request/get_findbyid_200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/find")
                        .param("name", "Naruto"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /v1/animes/find?name=JOJO should return NOT FOUND")
    @Order(5)
    void findByName_shouldReturnNotFound_whenFailed() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/find")
                        .param("name", "JOJO"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }

    @Test
    @DisplayName("POST /v1/animes should return NOT FOUND")
    @Order(6)
    void create_shouldCreateAnimeAndReturn_whenSuccessfull() throws Exception {

        var request = fileUtil.readResourceFile("anime/post/request/post_createanime_200.json");

        var response = dataUtil.getAnimeToSave();

        var responseFile = fileUtil.readResourceFile("anime/post/response/post_responsecreateanime_201.json");

        BDDMockito.when(animeMapperService.toAnime(ArgumentMatchers.any(AnimePostRequest.class)))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(responseFile));
    }

    @Test
    @DisplayName("DELETE /v1/animes/{id} should remove anime")
    @Order(7)
    void delete_shouldDeleteAnime_whenSuccessfull() throws Exception {

        var id = dataUtil.getAnimeToFind().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /v1/animes/{id} should return NOT FOUND")
    @Order(8)
    void delete_shouldReturnNotFound_whenFailed() throws Exception {

        var id = 50L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }

    @Test
    @DisplayName("PUT /v1/animes should replace anime")
    @Order(9)
    void update_shouldReplaceAnime_whenSuccessfull() throws Exception {

        var request = fileUtil.readResourceFile("anime/put/request/put_replaceanime_200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @DisplayName("PUT /v1/animes should return NOT FOUND")
    @Order(10)
    void update_shouldReturnNotFound_whenFailed() throws Exception {

        var request = fileUtil.readResourceFile("anime/put/request/put_replaceanime_404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private void mockList() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(dataUtil.getList());
    }
}