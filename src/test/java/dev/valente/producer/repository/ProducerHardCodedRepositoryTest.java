package dev.valente.producer.repository;

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

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {


    @InjectMocks
    private ProducerHardCodedRepository repo;

    @Mock
    private ProducerData data;

    private final ProducerDataUtil producerDataUtil = new ProducerDataUtil();

    @Test
    @DisplayName("findAll returns a list with all producers")
    @Order(1)
    void getProducers_ReturnsAllProducers_WhenSuccessfull() {

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        var lista = repo.getProducers();

        Assertions.assertThat(lista)
                .isNotEmpty()
                .hasSameSizeAs(producerDataUtil.getList());
    }

    @Test
    @DisplayName("Should save a producer")
    @Order(2)
    void save_CreatesProducer_WhenSuccessfull() {

        var producerToSave = producerDataUtil.getProducerToSave();

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        var savedProducer = repo.save(producerToSave);

        Assertions.assertThat(savedProducer)
                .isIn(producerDataUtil.getList())
                .hasFieldOrPropertyWithValue("id", producerToSave.getId())
                .hasFieldOrPropertyWithValue("name", producerToSave.getName())
                .hasNoNullFieldsOrProperties();
    }


    @Test
    @DisplayName("Should remove a existent producer")
    @Order(3)
    void remove_RemoveProducer_WhenSuccessfull() {
        var producerToRemove = producerDataUtil.getProducerToRemove();

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        repo.remove(producerToRemove);

        Assertions.assertThat(producerToRemove)
                .isNotIn(producerDataUtil.getList());

    }

    @Test
    @DisplayName("should replace existent producer")
    @Order(4)
    void replace_ReplaceProducer_WhenSuccessfull() {


        var producerToReplace = producerDataUtil.getProducerToReplace();
        var newProducer = producerDataUtil.getNewProducerWithCreatedAt();

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        repo.replace(producerToReplace, newProducer);

        Assertions.assertThat(newProducer)
                .isIn(producerDataUtil.getList())
                .hasNoNullFieldsOrProperties()
                .doesNotMatch(n -> producerToReplace.getName().equalsIgnoreCase(n.getName()));

        Assertions.assertThat(producerToReplace)
                .isNotIn(producerDataUtil.getList());

    }

    @Test
    @DisplayName("findById returns a producer with given id")
    @Order(5)
    void findById_ReturnsProducerWithGivenId_WhenSuccessfull() {

        var expectedProducer = producerDataUtil.getProducerToFind();

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        var producer = repo.findProducerById(expectedProducer.getId());

        Assertions.assertThat(producer)
                .isPresent().contains(expectedProducer)
                .get().isIn(producerDataUtil.getList());


    }

    @Test
    @DisplayName("findByName returns a producer with given name")
    @Order(6)
    void findByName_ReturnsProducerWithGivenName_WhenSuccessfull() {

        var expectedProducer = producerDataUtil.getProducerToFind();

        BDDMockito.when(data.getProducers()).thenReturn(producerDataUtil.getList());

        var producer = repo.findProducerByName(expectedProducer.getName());

        Assertions.assertThat(producer).isPresent().contains(expectedProducer)
                .get().isIn(producerDataUtil.getList());
    }
}