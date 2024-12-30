package dev.valente.producer.controller;

import dev.valente.common.DataUtil;
import dev.valente.producer.domain.Producer;
import dev.valente.producer.repository.ProducerData;
import dev.valente.producer.repository.ProducerHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Paths;

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan("dev.valente")
//@Import({...})
class ProducerControllerTest {

    @InjectMocks
    private ProducerController producerController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerData producerData;

    @Autowired
    private DataUtil<Producer> dataUtil;

    @Test
    @DisplayName("GET v1/producers should return list of all producers")
    @Order(1)
    void findAll_shouldReturnListOfproducers() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(dataUtil.getList());

        var texto = Files.readString(Paths.get("src/test/resources/get_allproducers_200.json"));


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(texto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(dataUtil.getList().size()));

    }
}