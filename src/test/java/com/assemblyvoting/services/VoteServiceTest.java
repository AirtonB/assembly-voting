package com.assemblyvoting.services;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.domain.Vote;
import com.assemblyvoting.exceptions.ExceptionMessages;
import com.assemblyvoting.models.converters.VoteConverter;
import com.assemblyvoting.models.requests.VoteRequest;
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

import static com.assemblyvoting.utils.CPFUtils.genInvalidCPF;
import static com.assemblyvoting.utils.CPFUtils.genValidCPF;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
  Vote vote;

  VoteServiceTest() {
    final var userIndetification = genValidCPF();
    var schedule = Schedule.builder().id(1L).description("Schedule #1").build();
    voteRequest =
        VoteRequest.builder()
            .scheduleId(schedule.getId())
            .userIdentification(userIndetification)
            .build();
    vote = Vote.builder().id(1L).schedule(schedule).userIdentification(userIndetification).build();
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve criar e retornar um voto SIM válido")
  void shouldCreateAndReturnYesValidVote() {

    voteRequest.setRegisteredVote(true);
    vote.setRegisteredVote(true);
    when(voteConverter.fromRequestToDomain(voteRequest)).thenReturn(vote);
    when(voteRepository.save(vote)).thenReturn(vote);

    var actual = voteService.saveVote(voteRequest);

    assertEquals(actual, Optional.of(vote));
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve criar e retornar um voto NÃO válido")
  void shouldCreateAndReturnNoValidVote() {
    voteRequest.setRegisteredVote(false);
    vote.setRegisteredVote(false);
    when(voteConverter.fromRequestToDomain(voteRequest)).thenReturn(vote);
    when(voteRepository.save(vote)).thenReturn(vote);

    var actual = voteService.saveVote(voteRequest);

    assertEquals(actual, Optional.of(vote));
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve lançar e retornar um voto válido")
  void shouldThrowInvalidCPFExeption() {

    voteRequest.setUserIdentification(genInvalidCPF());
    assertThatThrownBy(() -> voteService.saveVote(voteRequest))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining(ExceptionMessages.INVALID_CPF);
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve lançar e retornar um voto válido")
  void shouldThrowClosedPFExeption() {

    voteRequest.setScheduleId(2L);
    assertThatThrownBy(() -> voteService.saveVote(voteRequest))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining(ExceptionMessages.CLOSED_SESSION);
  }
}
