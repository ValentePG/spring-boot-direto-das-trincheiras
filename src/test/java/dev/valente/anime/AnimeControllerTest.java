package dev.valente.anime;

import dev.valente.domain.Anime;
import dev.valente.common.AnimeDataUtil;
import dev.valente.common.FileUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"dev.valente.anime","dev.valente.common","dev.valente.securityconfig"})
@WithMockUser
class AnimeControllerTest {

    private final String URL = "/v1/animes";

    @InjectMocks
    private AnimeController controller;

    @MockBean
    private AnimeRepositoryJPA animeRepository;

    @Autowired
    private AnimeDataUtil dataUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private AnimeDataUtil animeDataUtil;

    @BeforeEach
    void setUp() {
        mockList();
    }

    @Test
    @DisplayName("GET /v1/animes should return a list of all animes")
    @Order(1)
    void findAll_shouldReturnListOfAllAnimes_whenSuccessful() throws Exception {

        var response = fileUtil.readResourceFile("anime/get/response/get_allanimes_200.json");



        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /v1/animes should return 403 when role is not USER")
    @Order(1)
    @WithMockUser(roles = "ADMIN")
    void findAll_shouldReturnForbidden_whenRoleIsNotUSER() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("GET /v1/animes/paginated should return a paginated list of animes")
    @Order(2)
    void findAllPaginated_shouldReturnListPaginatedAnimes_whenSuccessful() throws Exception {

        var response = fileUtil.readResourceFile("anime/get/response/get_animespaginated_200.json");

        var pageRequest = PageRequest.of(0, animeDataUtil.getList().size());

        var animePage = new PageImpl<>(animeDataUtil.getList(), pageRequest, 1);

        BDDMockito.when(animeRepository.findAll(BDDMockito.any(Pageable.class))).thenReturn(animePage);

        mockMvc.perform(MockMvcRequestBuilders.get(URL +"/paginated")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /v1/animes/{id} should return anime by given id")
    @Order(3)
    void findById_shouldReturnAnime_whenSuccessful() throws Exception {

        var expectedUser = dataUtil.getAnimeToFind();

        BDDMockito.when(animeRepository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

        var response = fileUtil.readResourceFile("anime/get/request/get_findbyid_200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", expectedUser.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /v1/animes/{id} should return NOT FOUND")
    @Order(4)
    void findById_shouldReturnNotFound_whenFailed() throws Exception {

        var response = fileUtil.readResourceFile("exception/anime_notfoundexception_404.json");

        var inexistentId = 50L;

        BDDMockito.when(animeRepository.findById(inexistentId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", inexistentId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /v1/animes/find?name=Naruto should return anime by given name")
    @Order(5)
    void findByName_shouldReturnAnime_whenSuccessful() throws Exception {

        var response = fileUtil.readResourceFile("anime/get/request/get_findbyid_200.json");

        var expectedUser = dataUtil.getAnimeToFind();

        BDDMockito.when(animeRepository.findAnimeByName(expectedUser.getName())).thenReturn(Optional.of(expectedUser));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/find")
                        .param("name", "Naruto"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /v1/animes/find?name=JOJO should return NOT FOUND")
    @Order(6)
    void findByName_shouldReturnNotFound_whenFailed() throws Exception {

        var response = fileUtil.readResourceFile("exception/anime_notfoundexception_404.json");

        var expectedUser = dataUtil.getAnimeToFind();

        BDDMockito.when(animeRepository.findAnimeByName(expectedUser.getName())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/find")
                        .param("name", "JOJO"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST /v1/animes should save and return anime")
    @Order(7)
    void create_shouldCreateAnimeAndReturn_whenSuccessfull() throws Exception {

        var request = fileUtil.readResourceFile("anime/post/request/post_createanime_200.json");

        var animeToSave = Anime.builder().name("JoJo").build();

        var animeWithId = dataUtil.getAnimeToSave();

        var responseFile = fileUtil.readResourceFile("anime/post/response/post_responsecreateanime_201.json");

        BDDMockito.when(animeRepository.save(animeToSave)).thenReturn(animeWithId);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(responseFile));
    }

    @ParameterizedTest
    @MethodSource("postParameterizedTest")
    @DisplayName("POST v1/animes with invalid data should return BAD REQUEST")
    @Order(8)
    void create_shouldReturnNotFoundWithInvalidData_whenFailed(String fileName, String error) throws Exception {
        var request = fileUtil.readResourceFile(fileName);

        var mockResponse = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var mockResponseResolvedException = mockResponse.getResolvedException();
        Assertions.assertThat(mockResponseResolvedException).isNotNull();

        Assertions.assertThat(mockResponseResolvedException.getMessage()).contains(error);
    }

    private static Stream<Arguments> postParameterizedTest() {
        var error = "O nome não pode estar em branco";

        return Stream.of(
                Arguments.of("/anime/post/request/post_createanime-empty_400.json",
                        error),
                Arguments.of("/anime/post/request/post_createanime-blank_400.json",
                        error),
                Arguments.of("/anime/post/request/post_createanime-null_400.json",
                        error)
        );
    }

    @Test
    @DisplayName("DELETE /v1/animes/{id} should remove anime")
    @Order(9)
    void delete_shouldDeleteAnime_whenSuccessfull() throws Exception {

        var expectedUserToDelete = dataUtil.getAnimeToFind();

        BDDMockito.when(animeRepository.findById(expectedUserToDelete.getId())).thenReturn(Optional.of(expectedUserToDelete));


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", expectedUserToDelete.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /v1/animes/{id} should return NOT FOUND")
    @Order(10)
    void delete_shouldReturnNotFound_whenFailed() throws Exception {

        var response = fileUtil.readResourceFile("exception/anime_notfoundexception_404.json");

        var id = 50L;

        BDDMockito.when(animeRepository.findById(id)).thenReturn(Optional.empty());


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT /v1/animes should replace anime")
    @Order(11)
    void update_shouldReplaceAnime_whenSuccessfull() throws Exception {

        var request = fileUtil.readResourceFile("anime/put/request/put_replaceanime_200.json");

        var expectedUserToUpdate = dataUtil.getThird();

        BDDMockito.when(animeRepository.findById(expectedUserToUpdate.getId())).thenReturn(Optional.of(expectedUserToUpdate));

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @DisplayName("PUT /v1/animes should return NOT FOUND")
    @Order(12)
    void update_shouldReturnNotFound_whenFailed() throws Exception {

        var request = fileUtil.readResourceFile("anime/put/request/put_replaceanime_404.json");

        var response = fileUtil.readResourceFile("exception/anime_notfoundexception_404.json");

        var id = 50L;

        BDDMockito.when(animeRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("putParameterizedTest")
    @DisplayName("PUT v1/animes should return BAD REQUEST when fields are blank,empty or null")
    @Order(13)
    void update_shouldReturnBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtil.readResourceFile(fileName);

        var mockResponse = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var mockResponseResolvedException = mockResponse.getResolvedException();

        Assertions.assertThat(mockResponseResolvedException).isNotNull();

        Assertions.assertThat(mockResponseResolvedException.getMessage()).contains(errors);
    }

    private static Stream<Arguments> putParameterizedTest() {

        var error = "O nome não pode estar em branco";
        var errorId = "O id não pode ser nulo";
        var listOfErrors = List.of(error, errorId);


        return Stream.of(
                Arguments.of("/anime/put/request/put_replace-empty_400.json", listOfErrors),
                Arguments.of("/anime/put/request/put_replace-blank_400.json", listOfErrors)
        );
    }

    private void mockList() {
        BDDMockito.when(animeRepository.findAll()).thenReturn(dataUtil.getList());
    }
}