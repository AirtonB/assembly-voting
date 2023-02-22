package com.assemblyvoting.models.requests;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleRequest {
  private String description;

  @Builder(toBuilder = true)
  public ScheduleRequest(String description) {
    this.description = description;
  }
}
