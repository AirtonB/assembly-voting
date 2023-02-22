package com.assemblyvoting.repositories;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.domain.Vote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Random;

import static com.assemblyvoting.utils.CPFUtils.genValidCPF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SqlGroup({@Sql(value = "classpath:data.sql")})
class VoteRepositoryTest {
  @Autowired VoteRepository voteRepository;

  @Test
  @DisplayName("Deve criar e retornar um voto")
  void shouldCreateAndReturnVote() {
    final var vote =
        Vote.builder()
            .registeredVote(new Random().nextBoolean())
            .schedule(Schedule.builder().id(1L).build())
            .userIdentification(genValidCPF())
            .build();

    var actual = voteRepository.save(vote);
    assertThat(actual.getId()).isNotNull().isPositive();
    assertThat(actual).isInstanceOf(Vote.class);
  }

  @Test
  @DisplayName("Deve retornar a quantidade de votos sim por id da pauta")
  void shouldReturnRegisteredYesVoteByScheduleId() {
    var actual = voteRepository.registeredYesVoteBySchedule(1L);
    assertThat(actual).isNotNull().isPositive();
  }

  @Test
  @DisplayName("Deve retornar o total de votos por id")
  void shouldReturnTotalRegisteredVotes() {
    var actual = voteRepository.totalRegisteredVotesByScheduleId(1L);
    assertThat(actual).isNotNull().isPositive();
  }

  @Test
  @DisplayName("Deve verificar se o usuário existe pela identificação do usuário")
  void shouldSeeExistenceByUserIdentification() {
    final var userIdentification = "91093350059";
    var exists = voteRepository.existsByUserIdentification(userIdentification);
    assertTrue(exists);
  }
}
