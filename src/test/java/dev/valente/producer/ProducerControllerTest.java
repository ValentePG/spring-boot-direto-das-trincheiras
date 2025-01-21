package dev.valente.producer;

import dev.valente.common.FileUtil;
import dev.valente.common.ProducerDataUtil;
import dev.valente.domain.Producer;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"dev.valente.producer","dev.valente.common","dev.valente.securityconfig"})
@WithMockUser
//@ActiveProfiles("test")
//@Import({...})
class ProducerControllerTest {

    private final String URL = "/v1/producers";

    @InjectMocks
    private ProducerController producerController;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerRepositoryJPA producerRepository;

    @Autowired
    private ProducerDataUtil dataUtil;


    @BeforeEach
    void setUp() {
        mockList();
    }


    @Test
    @DisplayName("GET v1/producers should return list of all producers")
    @Order(1)
    void findAll_shouldReturnListOfAllproducers() throws Exception {

        var response = fileUtil.readResourceFile("/producer/get/request/get_allproducers_200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers should return forbidden when role is not USER")
    @Order(1)
    @WithMockUser(roles = "ADMIN")
    void findAll_shouldReturnForbidden_WhenRoleIsNotUSER() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    @DisplayName("GET v1/producers/find?name=Animation should return a producer named Animation")
    @Order(2)
    void findByName_shouldReturnProducer_whenSuccessfull() throws Exception {

        var response = fileUtil.readResourceFile("/producer/get/request/get_findbyname_200.json");

        var producerToFind = dataUtil.getProducerToFind();

        BDDMockito.when(producerRepository.findProducerByName(producerToFind.getName())).thenReturn(Optional.of(producerToFind));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/find")
                        .param("name", "Animation"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers/find?name=Mappa should return NOT FOUND")
    @Order(3)
    void findByName_shouldReturnNotFound_whenFailed() throws Exception {

        var response = fileUtil.readResourceFile("/exception/producer_notfoundexception_404.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/find")
                        .param("name", "Mappa"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));


    }

    @Test
    @DisplayName("GET v1/producers/{id} should return a producer named Animation")
    @Order(4)
    void findById_shouldReturnProducer_whenSuccessfull() throws Exception {

        var producerToFind = dataUtil.getList().getFirst();

        BDDMockito.when(producerRepository.findProducerById(producerToFind.getId())).thenReturn(Optional.of(producerToFind));

        var response = fileUtil.readResourceFile("/producer/get/request/get_findbyid_200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", producerToFind.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers/{id} should return NOT FOUND")
    @Order(5)
    void findById_shouldReturnNotFound_whenFailed() throws Exception {

        var response = fileUtil.readResourceFile("/exception/producer_notfoundexception_404.json");

        Long id = 50L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));


    }

    @Test
    @DisplayName("POST v1/producers should save a producer")
    @Order(6)
    void create_shouldSaveProducer_whenSuccessfull() throws Exception {

        var request = fileUtil.readResourceFile("/producer/post/request/post_createproducer_200.json");

        var producerToSave = Producer.builder().name("Mappa").build();
        var response = dataUtil.getProducerToSave("Mappa");

        var responseFile = fileUtil.readResourceFile("/producer/post/response/post_responsecreateproducer_201.json");

        BDDMockito.when(producerRepository.save(producerToSave)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header("x-api-key", 1234)
                        .header("x-api-teste", 1234)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(responseFile));


    }

    @ParameterizedTest
    @MethodSource("postParameterizedTest")
    @DisplayName("POST v1/producers with invalid data should return NOT FOUND")
    @Order(7)
    void create_shouldReturnNotFoundWithInvalidData_whenFailed(String fileName, String error) throws Exception {
        var request = fileUtil.readResourceFile(fileName);

        var mockResponse = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header("x-api-key", 1234)
                        .header("x-api-teste", 1234)
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
                Arguments.of("/producer/post/request/post_createproducer-empty_400.json",
                        error),
                Arguments.of("/producer/post/request/post_createproducer-blank_400.json",
                        error),
                Arguments.of("/producer/post/request/post_createproducer-null_400.json",
                        error)
        );
    }

    @Test
    @DisplayName("DELETE v1/producers/{id} should remove a producer")
    @Order(8)
    void deleteById_shouldRemoveProducer_whenSuccessfull() throws Exception {
        var userToDelete = dataUtil.getProducerToFind();
        BDDMockito.when(producerRepository.findProducerById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", userToDelete.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


    }

    @Test
    @DisplayName("DELETE v1/producers/{id} should return NOT FOUND")
    @Order(9)
    void deleteById_shouldReturnNotFound_whenFailed() throws Exception {
        var id = 50L;

        var response = fileUtil.readResourceFile("/exception/producer_notfoundexception_404.json");

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("PUT v1/producers should replace a producer")
    @Order(10)
    void replace_shouldReplaceProducer_whenSuccessfull() throws Exception {

        var request = fileUtil.readResourceFile("producer/put/request/put_replaceproducer_200.json");

        var userToReplace = dataUtil.getThird();

        BDDMockito.when(producerRepository.findProducerById(userToReplace.getId())).thenReturn(Optional.of(userToReplace));

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


    }

    @Test
    @DisplayName("PUT v1/producers should return NOT FOUND")
    @Order(11)
    void replace_shouldReturnNotFound_whenFailed() throws Exception {

        var request = fileUtil.readResourceFile("producer/put/request/put_replaceproducer_404.json");

        var response = fileUtil.readResourceFile("/exception/producer_notfoundexception_404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));


    }

    @ParameterizedTest
    @MethodSource("putParameterizedTest")
    @DisplayName("PUT v1/producers should return BAD REQUEST when fields are blank,empty or null")
    @Order(12)
    void replace_shouldReturnBadRequest_whenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
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
                Arguments.of("/producer/put/request/put_replace-empty_400.json", listOfErrors),
                Arguments.of("/producer/put/request/put_replace-blank_400.json", listOfErrors)
        );
    }

    private void mockList() {
        BDDMockito.when(producerRepository.findAll()).thenReturn(dataUtil.getList());
    }


}