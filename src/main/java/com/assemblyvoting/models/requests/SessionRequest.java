package com.assemblyvoting.models.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionRequest {
  private Long scheduleId;
  private LocalDateTime sessionEndTime;
}
