package com.assemblyvoting.models.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class VoteReponse {
  private ScheduleResponse schedule;
  private Long yes;
  private Long no;
  private Long total;
  private boolean isScheduleAproved;

  @Builder(toBuilder = true)
  public VoteReponse(
      ScheduleResponse schedule, Long yes, Long no, Long total, boolean isScheduleAproved) {
    this.schedule = schedule;
    this.yes = yes;
    this.no = no;
    this.total = total;
    this.isScheduleAproved = isScheduleAproved;
  }
}
