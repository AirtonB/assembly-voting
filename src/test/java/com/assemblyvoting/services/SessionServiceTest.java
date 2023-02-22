package com.assemblyvoting.services;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.domain.Session;
import com.assemblyvoting.exceptions.ExceptionMessages;
import com.assemblyvoting.exceptions.NotFoundException;
import com.assemblyvoting.models.converters.SessionConverter;
import com.assemblyvoting.models.requests.SessionRequest;
import com.assemblyvoting.repositories.SessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SessionServiceTest {
  @Autowired @InjectMocks SessionService sessionService;
  @Mock ScheduleService scheduleService;
  @Mock SessionRepository sessionRepository;
  @Mock SessionConverter sessionConverter;

  Session sessionMock;
  private final Schedule scheduleMock;

  private final LocalDateTime now;

  SessionServiceTest() {
    now = LocalDateTime.now();
    scheduleMock = Schedule.builder().id(1L).description("Schedule #1").build();
    sessionMock =
        Session.builder()
            .id(1L)
            .startSession(now)
            .endSession(now.plusMinutes(1))
            .schedule(scheduleMock)
            .build();
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve abrir e retornar uma nova sessão de votação com tempo padrão: 1min")
  void shouldOpenAndReturnSessionWithDefaultTime() {

    final var sessionOptional = Optional.of(sessionMock);

    final var sessionRequest =
        SessionRequest.builder().scheduleId(scheduleMock.getId()).sessionEndTime(null).build();

    when(scheduleService.getSchedule(scheduleMock.getId())).thenReturn(Optional.of(scheduleMock));
    when(sessionConverter.fromRequestToDomain(sessionRequest, null))
        .thenReturn(sessionOptional.get());
    when(sessionRepository.save(sessionOptional.get())).thenReturn(sessionOptional.get());

    var actual = sessionService.openSession(sessionRequest, now);
    assertEquals(actual, sessionOptional);
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve abrir e retornar uma nova sessão de votação com tempo definido")
  void shouldOpenAndReturnSessionWithSessionTime() {
    final var nowPlus30Minutes = now.plusMinutes(30);

    sessionMock.setEndSession(nowPlus30Minutes);
    final var sessionOptional = Optional.of(sessionMock);

    final var sessionRequest =
        SessionRequest.builder()
            .scheduleId(scheduleMock.getId())
            .sessionEndTime(nowPlus30Minutes)
            .build();

    when(scheduleService.getSchedule(scheduleMock.getId())).thenReturn(Optional.of(scheduleMock));
    when(sessionConverter.fromRequestToDomain(sessionRequest, nowPlus30Minutes))
        .thenReturn(sessionOptional.get());
    when(sessionRepository.save(sessionOptional.get())).thenReturn(sessionOptional.get());

    var actual = sessionService.openSession(sessionRequest, now);
    assertEquals(actual, sessionOptional);
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve lançar uma exceção ao tentar abrir uma sessão com uma pauta nula")
  void shouldThrowInvalidDataAccessApiUsageException() {
    final var sessionRequest =
        SessionRequest.builder().scheduleId(null).sessionEndTime(null).build();

    assertThatThrownBy(() -> sessionService.openSession(sessionRequest, null))
        .isInstanceOf(InvalidDataAccessApiUsageException.class)
        .hasMessage(ExceptionMessages.INVALID_DATA_ACCESS);
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve lançar uma exceção ao tentar abrir uma sessão com uma pauta não existente")
  void shouldThrowScheduleNotFoundException() {
    scheduleMock.setId(new Random().nextLong());

    final var sessionRequest =
        SessionRequest.builder().scheduleId(scheduleMock.getId()).sessionEndTime(null).build();

    assertThatThrownBy(() -> sessionService.openSession(sessionRequest, null))
        .isInstanceOf(NotFoundException.class)
        .hasMessage(ExceptionMessages.SCHEDULE_NOT_FOUND);
  }

  @Test
  @SqlGroup({@Sql(value = "classpath:data.sql")})
  @DisplayName("Deve lançar uma exceção quando a sessão não for encontrada")
  void shouldThrowSessionNotFoundException() {
    final var randomScheduleId = new Random().nextLong();
    assertThatThrownBy(() -> sessionService.isSessionOpened(randomScheduleId))
        .isInstanceOf(NotFoundException.class)
        .hasMessage(ExceptionMessages.SESSION_NOT_FOUND);
  }
}
