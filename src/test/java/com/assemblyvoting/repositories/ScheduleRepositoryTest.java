package com.assemblyvoting.repositories;

import com.assemblyvoting.domain.Schedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Random;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ScheduleRepositoryTest {

  @Autowired ScheduleRepository scheduleRepository;

  @Test
  @DisplayName("Deve criar e retornar uma pauta")
  void shouldCreateAndReturnSchedule() {
    final var schedule = Schedule.builder().description("lorem impsum#").build();
    var actual = scheduleRepository.save(schedule);
    assertThat(actual.getId()).isNotNull().isPositive();
    assertThat(actual).isInstanceOf(Schedule.class);
  }

  @Test
  @DisplayName("Deve retornar uma pauta buscada pelo id")
  void shouldReturnScheduleFindById() {
    final var schedule = Schedule.builder().description("lorem impsum#").build();
    final var savedSchedule = scheduleRepository.save(schedule);

    var scheduleFound =
        scheduleRepository.findById(savedSchedule.getId()).orElse(Schedule.builder().build());

    assertEquals(savedSchedule.getId(), scheduleFound.getId());
    assertThat(scheduleFound).isInstanceOf(Schedule.class);
  }

  @Test
  @DisplayName("Deve retornar todas as pautas existentes")
  void shouldReturnAllSchedules() {
    final var schedulesMock =
        asList(
            Schedule.builder().description("lorem impsum" + new Random().nextLong()).build(),
            Schedule.builder().description("lorem impsum" + new Random().nextLong()).build(),
            Schedule.builder().description("lorem impsum" + new Random().nextLong()).build(),
            Schedule.builder().description("lorem impsum" + new Random().nextLong()).build());

    scheduleRepository.saveAll(schedulesMock);

    var schedules = scheduleRepository.findAll();
    assertThat(schedules).isNotEmpty().containsAll(schedulesMock);
  }
}
