package dev.valente.producer.controller;

import dev.valente.common.FileUtil;
import dev.valente.common.ProducerDataUtil;
import dev.valente.producer.dto.ProducerPostRequest;
import dev.valente.producer.repository.ProducerData;
import dev.valente.producer.service.ProducerMapperService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "dev.valente")
//@ActiveProfiles("test")
//@Import({...})
class ProducerControllerTest {

    private final String URL = "/v1/producers";

    @InjectMocks
    private ProducerController producerController;

    @SpyBean
    private ProducerMapperService producerMapperService;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerData producerData;

    @Autowired
    private ProducerDataUtil dataUtil;

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    @BeforeEach
    void setUp() {
        mockList();
    }


    @Test
    @DisplayName("GET v1/producers should return list of all producers")
    @Order(1)
    void findAll_shouldReturnListOfproducers() throws Exception {

        System.out.println(databaseUrl);
        System.out.println(username);
        System.out.println(password);

        var response = fileUtil.readResourceFile("/producer/get/request/get_allproducers_200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers/find?name=Animation should return a producer named Animation")
    @Order(2)
    void findByName_shouldReturnProducer_whenSuccessfull() throws Exception {

        var response = fileUtil.readResourceFile("/producer/get/request/get_findbyname_200.json");

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


        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/find")
                        .param("name", "Mappa"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));


    }

    @Test
    @DisplayName("GET v1/producers/{id} should return a producer named Animation")
    @Order(4)
    void findById_shouldReturnHouse_whenSuccessfull() throws Exception {

        Long id = dataUtil.getProducerToFind().getId();

        var response = fileUtil.readResourceFile("/producer/get/request/get_findbyid_200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers/{id} should return NOT FOUND")
    @Order(5)
    void findById_shouldReturnNotFound_whenFailed() throws Exception {

        Long id = 50L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));


    }

    @Test
    @DisplayName("POST v1/producers should save a producer")
    @Order(6)
    void create_shouldSaveProducer_whenSuccessfull() throws Exception {

        var request = fileUtil.readResourceFile("/producer/post/request/post_createproducer_200.json");

        var response = dataUtil.getProducerToSave("Mappa");

        var responseFile = fileUtil.readResourceFile("/producer/post/response/post_responsecreateproducer_201.json");

        BDDMockito.when(producerMapperService
                        .toProducer(ArgumentMatchers.any(ProducerPostRequest.class)))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header("x-api-key", 1234)
                        .header("x-api-teste", 1234)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(responseFile));


    }

    @Test
    @DisplayName("DELETE v1/producers/{id} should remove a producer")
    @Order(7)
    void deleteById_shouldRemoveProducer_whenSuccessfull() throws Exception {
        var id = dataUtil.getProducerToFind().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


    }

    @Test
    @DisplayName("DELETE v1/producers/{id} should return NOT FOUND")
    @Order(8)
    void deleteById_shouldReturnNotFound_whenFailed() throws Exception {
        var id = 50L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

    }

    @Test
    @DisplayName("PUT v1/producers should replace a producer")
    @Order(9)
    void replace_shouldReplaceProducer_whenSuccessfull() throws Exception {

        var request = fileUtil.readResourceFile("producer/put/request/put_replaceproducer_200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


    }

    @Test
    @DisplayName("PUT v1/producers should return NOT FOUND")
    @Order(10)
    void replace_shouldReturnNotFound_whenFailed() throws Exception {

        var request = fileUtil.readResourceFile("producer/put/request/put_replaceproducer_404.json");


        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));


    }

    private void mockList() {
        BDDMockito.when(producerData.getProducers()).thenReturn(dataUtil.getList());
    }


}