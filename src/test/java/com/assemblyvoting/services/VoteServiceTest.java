package com.assemblyvoting.services;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.domain.Vote;
import com.assemblyvoting.exceptions.ExceptionMessages;
import com.assemblyvoting.models.converters.VoteConverter;
import com.assemblyvoting.models.requests.VoteRequest;
import com.assemblyvoting.models.responses.ScheduleResponse;
import com.assemblyvoting.models.responses.VoteReponse;
import com.assemblyvoting.repositories.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Random;

import static com.assemblyvoting.utils.CPFUtils.genInvalidCPF;
import static com.assemblyvoting.utils.CPFUtils.genValidCPF;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class VoteServiceTest {
  @Autowired @InjectMocks VoteService voteService;

  @Mock VoteRepository voteRepository;
  @Mock VoteConverter voteConverter;

  VoteRequest voteRequest;
  Schedule schedule;
  Vote vote;

  VoteServiceTest() {
    final var userIndetification = genValidCPF();
    schedule = Schedule.builder().id(1L).description("Schedule #1").build();
    voteRequest =
        VoteRequest.builder()
            .scheduleId(schedule.getId())
            .registeredVote(new Random().nextBoolean())
            .build();
    vote =
        Vote.builder().id(1L).schedule(schedule).registeredVote(new Random().nextBoolean()).build();
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve criar e retornar um voto SIM válido")
  void shouldCreateAndReturnYesValidVote() {

    final var validCpf = genValidCPF();
    voteRequest.setUserIdentification(validCpf);
    voteRequest.setRegisteredVote(true);
    vote.setRegisteredVote(true);
    vote.setUserIdentification(validCpf);
    when(voteConverter.fromRequestToDomain(voteRequest)).thenReturn(vote);
    when(voteRepository.save(vote)).thenReturn(vote);

    var actual = voteService.saveVote(voteRequest);

    assertEquals(actual, Optional.of(vote));
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve criar e retornar um voto NÃO válido")
  void shouldCreateAndReturnNoValidVote() {
    final var validCpf = genValidCPF();
    voteRequest.setUserIdentification(validCpf);
    voteRequest.setRegisteredVote(false);
    vote.setRegisteredVote(false);
    vote.setUserIdentification(validCpf);

    when(voteConverter.fromRequestToDomain(voteRequest)).thenReturn(vote);
    when(voteRepository.save(vote)).thenReturn(vote);

    var actual = voteService.saveVote(voteRequest);

    assertEquals(actual, Optional.of(vote));
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Valida a busca dos resultados de uma votação")
  void validateResultVoteResponse() {
    final var scheduleMockSessionFinished =
        Schedule.builder().id(2L).description("Schedule #2").build();
    final var voteMockList =
        asList(
            Vote.builder().registeredVote(false).schedule(scheduleMockSessionFinished).build(),
            Vote.builder().registeredVote(true).schedule(scheduleMockSessionFinished).build(),
            Vote.builder().registeredVote(false).schedule(scheduleMockSessionFinished).build(),
            Vote.builder().registeredVote(true).schedule(scheduleMockSessionFinished).build(),
            Vote.builder().registeredVote(true).schedule(scheduleMockSessionFinished).build());

    final var yesVote = (long) 3;
    final var noVote = (long) 2;
    final var total = (long) 5;
    final var scheduleAproved = true;

    final var voteReponse =
        Optional.of(
            VoteReponse.builder()
                .schedule(new ScheduleResponse(scheduleMockSessionFinished.getDescription()))
                .yes(yesVote)
                .no(noVote)
                .total(total)
                .isScheduleAproved(scheduleAproved)
                .build());

    when(voteRepository.totalRegisteredVotesByScheduleId(scheduleMockSessionFinished.getId()))
        .thenReturn(voteReponse.get().getTotal());
    when(voteRepository.registeredYesVoteBySchedule(scheduleMockSessionFinished.getId()))
        .thenReturn(voteReponse.get().getYes());
    when(voteRepository.saveAll(voteMockList)).thenReturn(voteMockList);
    when(voteConverter.toResponse(
            scheduleMockSessionFinished, yesVote, noVote, total, scheduleAproved))
        .thenReturn(voteReponse.get());

    var actual = voteService.findResultByScheduleId(2L);
    assertEquals(voteReponse, actual);

    verifyNoMoreInteractions(voteRepository);
    verifyNoMoreInteractions(voteConverter);
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve lançar uma exceção pois o usuário já votou na pauta")
  void shouldThrowUserAlreadyVoteException() {
    voteRequest.setUserIdentification("91093350059");
    assertThatThrownBy(() -> voteService.saveVote(voteRequest))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining(ExceptionMessages.HAS_USER_ALREADY_VOTE);
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName(
      "Deve lançar uma exceção ao buscar o resultado de uma votação, pois a sessão está em andamento")
  void shouldThrow403ResultUnavailableExeption() {

    long scheduleId = schedule.getId();
    assertThatThrownBy(() -> voteService.findResultByScheduleId(scheduleId))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining(ExceptionMessages.OPENED_SESSION);
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve lançar uma exceção pois o CPF fornecido é inválido")
  void shouldThrowInvalidCPFExeption() {
    voteRequest.setUserIdentification(genInvalidCPF());
    assertThatThrownBy(() -> voteService.saveVote(voteRequest))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining(ExceptionMessages.INVALID_CPF);
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve lançar uma exceção pois a a votação está encerrada")
  void shouldThrowClosedSessionExeption() {
    voteRequest.setScheduleId(2L);
    voteRequest.setUserIdentification(genValidCPF());
    assertThatThrownBy(() -> voteService.saveVote(voteRequest))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining(ExceptionMessages.CLOSED_SESSION);
  }
}
