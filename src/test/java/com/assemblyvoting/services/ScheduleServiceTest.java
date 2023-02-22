package com.assemblyvoting.services;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.models.converters.ScheduleConverter;
import com.assemblyvoting.models.requests.ScheduleRequest;
import com.assemblyvoting.repositories.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ScheduleServiceTest {

  @InjectMocks ScheduleService scheduleService;
  @Mock ScheduleRepository scheduleRepository;
  @Mock ScheduleConverter scheduleConverter;

  Schedule scheduleMock;
  ScheduleRequest scheduleRequestMock;

  ScheduleServiceTest() {
    scheduleMock = Schedule.builder().id(1L).description("Schedule #1").build();
    scheduleRequestMock = ScheduleRequest.builder().description("Schedule #1").build();
  }

  @Test
  @DisplayName("Deve criar e retornar uma pauta de votação")
  void shouldCreateAndReturnSchedule() {
    final var scheduleOptional = Optional.of(scheduleMock);

    when(scheduleConverter.fromRequestToDomain(scheduleRequestMock))
        .thenReturn(scheduleOptional.get());
    when(scheduleRepository.save(scheduleOptional.get())).thenReturn(scheduleOptional.get());

    var actual = scheduleService.createSchedule(scheduleRequestMock);
    assertEquals(actual, scheduleOptional);

    verify(scheduleConverter).fromRequestToDomain(scheduleRequestMock);
    verify(scheduleRepository).save(scheduleOptional.get());
    verifyNoMoreInteractions(scheduleRepository);
    verifyNoMoreInteractions(scheduleConverter);
  }

  @Test
  @DisplayName("Deve retornar uma pauta quando for encontrada")
  void shouldReturnScheduleFound() {
    final var scheduleOptional = Optional.of(scheduleMock);
    when(scheduleRepository.findById(scheduleOptional.get().getId())).thenReturn(scheduleOptional);

    var scheduleFound = scheduleService.getSchedule(scheduleOptional.get().getId());

    assertEquals(scheduleFound, scheduleOptional);
  }
}
