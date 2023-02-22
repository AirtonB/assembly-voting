package com.assemblyvoting.repositories;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.domain.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SqlGroup({@Sql(value = "classpath:data.sql")})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SessionRepositoryTest {

  @Autowired SessionRepository sessionRepository;

  @Test
  @DisplayName("Deve criar e retornar uma sessão")
  void shouldCreateAndReturnSession() {
    final var now = LocalDateTime.now();
    final var session =
        Session.builder()
            .schedule(Schedule.builder().id(1L).build())
            .startSession(now)
            .endSession(now.plusMinutes(1))
            .build();
    var actual = sessionRepository.save(session);

    assertThat(actual.getId()).isNotNull().isPositive();
    assertThat(actual).isInstanceOf(Session.class);
  }

  @Test
  @DisplayName("Deve retornar uma sessão buscado pelo id")
  void shouldReturnSessionFindById() {
    var actual = sessionRepository.findById(1L);
    assertThat(actual).isNotNull().get().isInstanceOf(Session.class);
  }

  @Test
  @DisplayName("Deve retornar uma sessão buscada pelo id de uma pauta")
  void shouldReturnSessionByScheduleId() {
    var actual = sessionRepository.findSessionByScheduleId(1L);
    assertThat(actual).isNotNull().get().isInstanceOf(Session.class);
  }
}
