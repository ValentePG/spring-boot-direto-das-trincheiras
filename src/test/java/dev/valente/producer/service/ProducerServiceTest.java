package dev.valente.producer.service;

import dev.valente.producer.domain.Producer;
import dev.valente.producer.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService producerService;

    @Mock
    private ProducerHardCodedRepository repository;

    private final List<Producer> producersList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        producersList.add(Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build());
        producersList.add(Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build());
        producersList.add(Producer.builder().id(3L).name("Mad House").createdAt(LocalDateTime.now()).build());
    }

    @Test
    @Order(1)
    void getAll_ReturnsListOfProducers_whenSuccessfull() {
        BDDMockito.when(repository.getProducers()).thenReturn(producersList);

        var listProducers = producerService.findAll();

        Assertions.assertThat(listProducers).hasSize(3);
    }

    @Test
    @Order(2)
    void save_ShouldReturnProducer_whenSuccessfull() {

        var producerToSave = Producer.builder().id(15L).name("Mappa").createdAt(LocalDateTime.now()).build();

        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

        var producer = producerService.save(producerToSave);

        Assertions.assertThat(producer)
                .isEqualTo(producerToSave)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Order(3)
    void replace_ShouldReplaceProducer_whenSuccessfull() {

        var producerToReplace = producersList.getFirst();

        var newProducer = Producer.builder().id(producerToReplace.getId())
                .name("Kyoto")
                .build();

        BDDMockito.when(repository.findProducerById(producerToReplace.getId()))
                .thenReturn(Optional.of(producerToReplace));

        producerService.replace(newProducer);

        Mockito.verify(repository, Mockito.times(1)).findProducerById(producerToReplace.getId());
        Mockito.verify(repository, Mockito.times(1)).replace(producerToReplace, newProducer);
    }

    @Test
    @Order(4)
    void replace_ShouldThrowNotFound_whenFailure() {

        var producerToReplace = producersList.getFirst();

        var newProducer = Producer.builder().id(producerToReplace.getId())
                .name("Kyoto")
                .build();

        BDDMockito.when(repository.findProducerById(producerToReplace.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> producerService.replace(newProducer))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("404 NOT_FOUND \"Producer not found\"");

        Mockito.verify(repository, Mockito.times(1)).findProducerById(producerToReplace.getId());
        Mockito.verify(repository, Mockito.times(0)).replace(producerToReplace, newProducer);
    }

    @Test
    @Order(5)
    void remove_ShouldRemoveProducer_whenSuccessfull() {
        var producerToRemove = producersList.getFirst();

        BDDMockito.when(repository.findProducerById(producerToRemove.getId()))
                .thenReturn(Optional.of(producerToRemove));

        producerService.delete(producerToRemove.getId());

        Mockito.verify(repository, Mockito.times(1)).remove(producerToRemove);
        Mockito.verify(repository, Mockito.times(1)).findProducerById(producerToRemove.getId());
    }

    @Test
    @Order(6)
    void remove_ShouldRemoveProducer_whenFailure() {
        var producerToRemove = producersList.getFirst();

        BDDMockito.when(repository.findProducerById(producerToRemove.getId()))
                .thenReturn(Optional.empty());


        Assertions.assertThatThrownBy(() -> producerService.delete(producerToRemove.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("404 NOT_FOUND \"Producer not found\"");
    }

    @Test
    @Order(7)
    void findByIdOrThrowNotFound_ShouldReturnProducer_whenSuccessfull() {

        var firstProducer = producersList.getFirst();

        BDDMockito.when(repository.findProducerById(firstProducer.getId())).thenReturn(Optional.of(firstProducer));

        var producer = producerService.findByIdOrThrowNotFound(firstProducer.getId());

        Assertions.assertThat(producer)
                .isEqualTo(firstProducer)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Order(8)
    void findByIdOrThrowNotFound_ShouldThrowNotFound_whenFailure() {

        var firstProducer = producersList.getFirst();

        BDDMockito.when(repository.findProducerById(firstProducer.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> producerService.findByIdOrThrowNotFound(firstProducer.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("404 NOT_FOUND \"Producer not found\"");

    }

    @Test
    @Order(9)
    void findByNameOrThrowNotFound_ShouldReturnProducer_whenSuccessfull() {

        var firstProducer = producersList.getFirst();

        BDDMockito.when(repository.findProducerByName(firstProducer.getName())).thenReturn(Optional.of(firstProducer));

        var producer = producerService.findByNameOrThrowNotFound(firstProducer.getName());

        Assertions.assertThat(producer)
                .isEqualTo(firstProducer)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Order(10)
    void findByNameOrThrowNotFound_ShouldThrowNotFound_whenFailure() {

        var firstProducer = producersList.getFirst();

        BDDMockito.when(repository.findProducerByName(firstProducer.getName())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> producerService.findByNameOrThrowNotFound(firstProducer.getName()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("404 NOT_FOUND \"Producer not found\"");

    }
}