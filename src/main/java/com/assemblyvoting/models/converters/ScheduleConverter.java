package com.assemblyvoting.models.converters;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.models.requests.ScheduleRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ScheduleConverter {
  private final ModelMapper modelMapper = new ModelMapper();

  public Schedule fromRequestToDomain(ScheduleRequest scheduleRequest) {
    return modelMapper.map(scheduleRequest, Schedule.class);
  }
}
