package dev.valente.producer.repository;

import dev.valente.common.DataUtil;
import dev.valente.common.ProducerDataUtil;
import dev.valente.producer.domain.Producer;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {


    @InjectMocks
    private ProducerHardCodedRepository repo;

    @Mock
    private ProducerData data;

    private DataUtil<Producer> producerDataUtil;

    @BeforeEach
    void setUp() {
        producerDataUtil = new ProducerDataUtil();
    }

    @Test
    @DisplayName("findAll returns a list with all producers")
    @Order(1)
    void getProducers_ReturnsAllProducers_WhenSuccessfull() {

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());
        var lista = repo.getProducers();

        Assertions.assertThat(lista).isNotNull()
                .isNotEmpty()
                .hasSameSizeAs(producerDataUtil.getList());
    }

    @Test
    @DisplayName("Should save a producer")
    @Order(2)
    void save_CreatesProducer_WhenSuccessfull() {

        var producer = Producer.builder().id(4L).name("Meipou")
                .createdAt(LocalDateTime.now()).build();

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        var savedProducer = repo.save(producer);

        Assertions.assertThat(savedProducer).isNotNull()
                .isIn(producerDataUtil.getList())
                .hasFieldOrPropertyWithValue("name", producer.getName())
                .hasNoNullFieldsOrProperties();
    }


    @Test
    @DisplayName("Should remove a existent producer")
    @Order(3)
    void remove_RemoveProducer_WhenSuccessfull() {
        var producer = producerDataUtil.getList().getFirst();

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        repo.remove(producer);

        Assertions.assertThat(producer)
                .isInstanceOf(Producer.class)
                .isNotIn(producerDataUtil.getList())
                .hasNoNullFieldsOrProperties();

    }

    @Test
    @DisplayName("should replace existent producer")
    @Order(4)
    void replace_ReplaceProducer_WhenSuccessfull() {

        String newName = "Meipou";

        var producerToReplace = producerDataUtil.getList().getFirst();
        var producerToSave = Producer.builder().id(producerToReplace.getId())
                .name(newName)
                .createdAt(producerToReplace.getCreatedAt()).build();

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        repo.replace(producerToReplace, producerToSave);

        Assertions.assertThat(producerToSave)
                .isIn(producerDataUtil.getList())
                .hasNoNullFieldsOrProperties()
                .doesNotMatch(n -> producerToReplace.getName().equalsIgnoreCase(n.getName()));

        Assertions.assertThat(producerToReplace)
                .isNotIn(producerDataUtil.getList())
                .hasNoNullFieldsOrProperties();

    }

    @Test
    @DisplayName("findById returns a producer with given id")
    @Order(5)
    void findById_ReturnsProducerWithGivenId_WhenSuccessfull() {

        var expectedProducer = producerDataUtil.getList().getFirst();

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        var producer = repo.findProducerById(expectedProducer.getId());

        Assertions.assertThat(producer)
                .isPresent().contains(expectedProducer)
                .get().hasNoNullFieldsOrProperties();


    }

    @Test
    @DisplayName("findByName returns a producer with given name")
    @Order(6)
    void findByName_ReturnsProducerWithGivenName_WhenSuccessfull() {

        var expectedProducer = producerDataUtil.getList().getFirst();

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        var producer = repo.findProducerByName(expectedProducer.getName());

        Assertions.assertThat(producer).isPresent().contains(expectedProducer)
                .get().hasNoNullFieldsOrProperties();
    }
}