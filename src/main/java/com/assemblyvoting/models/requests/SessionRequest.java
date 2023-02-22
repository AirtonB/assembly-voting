package com.assemblyvoting.models.requests;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionRequest {
  private Long scheduleId;
  private LocalDateTime sessionEndTime;

  @Builder(toBuilder = true)
  public SessionRequest(Long scheduleId, LocalDateTime sessionEndTime) {
    this.scheduleId = scheduleId;
    this.sessionEndTime = sessionEndTime;
  }
}
