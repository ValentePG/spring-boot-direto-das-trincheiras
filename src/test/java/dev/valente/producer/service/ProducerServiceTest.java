package dev.valente.producer.service;


import dev.valente.common.DataUtil;
import dev.valente.common.ProducerDataUtil;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService producerService;

    @Mock
    private ProducerHardCodedRepository repository;

    private DataUtil<Producer> producerDataUtil;

    @BeforeEach
    void setUp() {
        producerDataUtil = new ProducerDataUtil();
    }

    @Test
    @Order(1)
    @DisplayName("Should return list of producers")
    void getAll_ReturnsListOfProducers_whenSuccessfull() {
        BDDMockito.when(repository.getProducers()).thenReturn(producerDataUtil.getList());

        var listProducers = producerService.findAll();

        Assertions.assertThat(listProducers).hasSameSizeAs(producerDataUtil.getList());
    }

    @Test
    @Order(2)
    @DisplayName("Should save producer")
    void save_ShouldReturnProducer_whenSuccessfull() {

        var producerToSave = Producer.builder().id(15L).name("Mappa").createdAt(LocalDateTime.now()).build();

        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

        var producer = producerService.save(producerToSave);

        Assertions.assertThat(producer)
                .isEqualTo(producerToSave);
    }

    @Test
    @Order(3)
    @DisplayName("Should replace existent producer by new producer")
    void replace_ShouldReplaceProducer_whenSuccessfull() {

        var producerToReplace = producerDataUtil.getList().getFirst();

        var newProducer = Producer.builder().id(producerToReplace.getId())
                .name("Kyoto")
                .build();

        BDDMockito.when(repository.findProducerById(producerToReplace.getId()))
                .thenReturn(Optional.of(producerToReplace));

        BDDMockito.doNothing().when(repository).replace(producerToReplace, newProducer);

        Assertions.assertThatNoException()
                .isThrownBy(() -> producerService.replace(newProducer));

        Mockito.verify(repository, Mockito.times(1)).findProducerById(producerToReplace.getId());
        Mockito.verify(repository, Mockito.times(1)).replace(producerToReplace, newProducer);
    }

    @Test
    @Order(4)
    @DisplayName("Should throw not found exception")
    void replace_ShouldThrowNotFound_whenFailure() {

        var producerToReplace = producerDataUtil.getList().getFirst();

        var newProducer = Producer.builder().id(producerToReplace.getId())
                .name("Kyoto")
                .build();

        BDDMockito.when(repository.findProducerById(producerToReplace.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> producerService.replace(newProducer))
                .isInstanceOf(ResponseStatusException.class);
//                .hasMessage("404 NOT_FOUND \"Producer not found\"");

        Mockito.verify(repository, Mockito.times(1)).findProducerById(producerToReplace.getId());
        Mockito.verify(repository, Mockito.times(0)).replace(producerToReplace, newProducer);
    }

    @Test
    @Order(5)
    @DisplayName("Should remove producer by given ID")
    void remove_ShouldRemoveProducer_whenSuccessfull() {
        var producerToRemove = producerDataUtil.getList().getFirst();

        BDDMockito.when(repository.findProducerById(producerToRemove.getId()))
                .thenReturn(Optional.of(producerToRemove));

        BDDMockito.doNothing().when(repository).remove(producerToRemove);

        Assertions.assertThatNoException()
                .isThrownBy(() -> producerService.delete(producerToRemove.getId()));

        Mockito.verify(repository, Mockito.times(1)).remove(producerToRemove);
        Mockito.verify(repository, Mockito.times(1)).findProducerById(producerToRemove.getId());
    }

    @Test
    @Order(6)
    @DisplayName("Should throw not found exception")
    void remove_ShouldRemoveProducer_whenFailure() {
        var producerToRemove = producerDataUtil.getList().getFirst();

        BDDMockito.when(repository.findProducerById(producerToRemove.getId()))
                .thenReturn(Optional.empty());


        Assertions.assertThatThrownBy(() -> producerService.delete(producerToRemove.getId()))
                .isInstanceOf(ResponseStatusException.class);
//                .hasMessage("404 NOT_FOUND \"Producer not found\"");

        Mockito.verify(repository, Mockito.times(1)).findProducerById(producerToRemove.getId());
        Mockito.verify(repository, Mockito.times(0)).remove(producerToRemove);
    }

    @Test
    @Order(7)
    @DisplayName("Should return producer by given ID")
    void findByIdOrThrowNotFound_ShouldReturnProducer_whenSuccessfull() {

        var expectedProducer = producerDataUtil.getList().getFirst();

        BDDMockito.when(repository.findProducerById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));

        var producer = producerService.findByIdOrThrowNotFound(expectedProducer.getId());

        Assertions.assertThat(producer)
                .isEqualTo(expectedProducer);
    }

    @Test
    @Order(8)
    @DisplayName("Should throw not found exception")
    void findByIdOrThrowNotFound_ShouldThrowNotFound_whenFailure() {

        var expectedProducer = producerDataUtil.getList().getFirst();

        BDDMockito.when(repository.findProducerById(expectedProducer.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> producerService.findByIdOrThrowNotFound(expectedProducer.getId()))
                .isInstanceOf(ResponseStatusException.class);
//                .hasMessage("404 NOT_FOUND \"Producer not found\"");

    }

    @Test
    @Order(9)
    @DisplayName("Should return producer by given name")
    void findByNameOrThrowNotFound_ShouldReturnProducer_whenSuccessfull() {

        var expectedProducer = producerDataUtil.getList().getFirst();

        BDDMockito.when(repository.findProducerByName(expectedProducer.getName())).thenReturn(Optional.of(expectedProducer));

        var producer = producerService.findByNameOrThrowNotFound(expectedProducer.getName());

        Assertions.assertThat(producer)
                .isEqualTo(expectedProducer);
    }

    @Test
    @Order(10)
    @DisplayName("Should throw not found exception")
    void findByNameOrThrowNotFound_ShouldThrowNotFound_whenFailure() {

        var expectedProducer = producerDataUtil.getList().getFirst();

        BDDMockito.when(repository.findProducerByName(expectedProducer.getName())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> producerService.findByNameOrThrowNotFound(expectedProducer.getName()))
                .isInstanceOf(ResponseStatusException.class);
//                .hasMessage("404 NOT_FOUND \"Producer not found\"");

    }
}