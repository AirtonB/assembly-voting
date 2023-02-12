package com.assemblyvoting.services;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * @author leandro-bezerra
 */
@Service
public class ScheduleService {
  final ScheduleRepository scheduleRepository;

  public ScheduleService(ScheduleRepository scheduleRepository) {
    this.scheduleRepository = scheduleRepository;
  }

  public List<Schedule> getAllSchedules() {
    return scheduleRepository.findAll();
  }

  public Optional<Schedule> getSchedule(Long id) {
    return scheduleRepository.findById(id);
  }

  public Optional<Schedule> saveSchedule(Schedule schedule) {
    try {
      scheduleRepository.save(schedule);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.of(schedule);
  }
}
