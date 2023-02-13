package com.assemblyvoting.services;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.models.converters.ScheduleConverter;
import com.assemblyvoting.models.requests.ScheduleRequest;
import com.assemblyvoting.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * @author leandro-bezerra
 */
@Service
public class ScheduleService {
  private final ScheduleRepository scheduleRepository;
  private final ScheduleConverter scheduleConverter;

  public ScheduleService(
      ScheduleRepository scheduleRepository, ScheduleConverter scheduleConverter) {
    this.scheduleRepository = scheduleRepository;
    this.scheduleConverter = scheduleConverter;
  }

  public Optional<Schedule> getSchedule(Long id) {
    return scheduleRepository.findById(id);
  }

  public Optional<Schedule> createSchedule(ScheduleRequest scheduleRequest) {

    Schedule schedule = scheduleConverter.fromRequestToDomain(scheduleRequest);
    return Optional.of(scheduleRepository.save(schedule));
  }
}
