package com.assemblyvoting.controllers;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.models.requests.ScheduleRequest;
import com.assemblyvoting.services.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
/**
 * @author leandro-bezerra
 */
@RestController()
@RequestMapping(value = "/schedule", produces = "application/json")
public class ScheduleController {

  private final ScheduleService scheduleService;

  public ScheduleController(ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @PostMapping
  public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleRequest scheduleRequest) {

    Optional<Schedule> _schedule = scheduleService.createSchedule(scheduleRequest);

    return _schedule
        .map(it -> new ResponseEntity<>(it, HttpStatus.CREATED))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }
}
