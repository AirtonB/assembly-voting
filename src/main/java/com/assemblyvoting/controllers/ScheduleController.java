package com.assemblyvoting.controllers;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.services.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
/**
 * @author leandro-bezerra
 */
@RestController()
@RequestMapping(value = "/schedule", produces = "application/json")
public class ScheduleController {

  final ScheduleService scheduleService;

  public ScheduleController(ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @GetMapping
  public ResponseEntity<List<Schedule>> getAllSchedules() {
    List<Schedule> schedules = scheduleService.getAllSchedules();
    if (schedules.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return ResponseEntity.ok(schedules);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
    Optional<Schedule> schedule = scheduleService.getSchedule(id);

    return schedule
        .map(ResponseEntity::ok)
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
    Optional<Schedule> _schedule = scheduleService.saveSchedule(schedule);

    return _schedule
        .map(it -> new ResponseEntity<>(it, HttpStatus.CREATED))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }
}
