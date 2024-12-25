package dev.valente.producer.repository;

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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {


    @InjectMocks
    private ProducerHardCodedRepository repo;

    @Mock
    private ProducerData data;

    private final List<Producer> producersList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        producersList.add(Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build());
        producersList.add(Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build());
        producersList.add(Producer.builder().id(3L).name("Mad House").createdAt(LocalDateTime.now()).build());
    }

    @Test
    @DisplayName("findAll returns a list with all producersList")
    @Order(1)
    void getProducers_ReturnsAllProducers_WhenSuccessfull() {

        BDDMockito.when(data.getProducers()).thenReturn(producersList);
        var lista = repo.getProducers();

        Assertions.assertThat(lista).isNotNull()
                .isNotEmpty()
                .hasSameSizeAs(producersList);
    }

    @Test
    @DisplayName("Should save a producer")
    @Order(2)
    void save_CreatesProducer_WhenSuccessfull() {

        var producer = Producer.builder().id(4L).name("Meipou")
                .createdAt(LocalDateTime.now()).build();

        BDDMockito.when(data.getProducers()).thenReturn(producersList);

        var savedProducer = repo.save(producer);

        Assertions.assertThat(savedProducer).isNotNull()
                .isIn(producersList)
                .hasFieldOrPropertyWithValue("name", producer.getName())
                .hasNoNullFieldsOrProperties();
    }


    @Test
    @DisplayName("Should remove a existent producer")
    @Order(3)
    void remove_RemoveProducer_WhenSuccessfull() {
        var producer = Producer.builder().id(4L).name("Meipou")
                .createdAt(LocalDateTime.now()).build();
        repo.save(producer);

        BDDMockito.when(data.getProducers()).thenReturn(producersList);
        repo.remove(producer);

        Assertions.assertThat(producer)
                .isInstanceOf(Producer.class)
                .isNotIn(producersList)
                .hasNoNullFieldsOrProperties();

    }

    @Test
    @DisplayName("should replace existent producer")
    @Order(4)
    void replace_ReplaceProducer_WhenSuccessfull() {

        String newName = "Meipou";

        var producerToReplace = producersList.getFirst();
        var producerToSave = Producer.builder().id(producerToReplace.getId())
                .name(newName)
                .createdAt(producerToReplace.getCreatedAt()).build();

        BDDMockito.when(data.getProducers()).thenReturn(producersList);

        repo.replace(producerToReplace, producerToSave);

        Assertions.assertThat(producerToSave)
                .isIn(producersList)
                .hasNoNullFieldsOrProperties()
                .doesNotMatch(n -> producerToReplace.getName().equalsIgnoreCase(n.getName()));

        Assertions.assertThat(producerToReplace)
                .isNotIn(producersList)
                .hasNoNullFieldsOrProperties();

    }

    @Test
    @DisplayName("findById returns a producer with given id")
    @Order(5)
    void findById_ReturnsProducerWithGivenId_WhenSuccessfull() {

        var expectedProducer = producersList.getFirst();

        BDDMockito.when(data.getProducers()).thenReturn(producersList);

        var producer = repo.findProducerById(expectedProducer.getId());

        Assertions.assertThat(producer)
                .isPresent().contains(expectedProducer)
                .get().hasNoNullFieldsOrProperties();


    }

    @Test
    @DisplayName("findByName returns a producer with given name")
    @Order(6)
    void findByName_ReturnsProducerWithGivenName_WhenSuccessfull() {

        var expectedProducer = producersList.getFirst();

        BDDMockito.when(data.getProducers()).thenReturn(producersList);

        var producer = repo.findProducerByName(expectedProducer.getName());

        Assertions.assertThat(producer).isPresent().contains(expectedProducer)
                .get().hasNoNullFieldsOrProperties();
    }
}