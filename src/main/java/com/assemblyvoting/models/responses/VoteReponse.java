package com.assemblyvoting.models.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteReponse {
  private ScheduleResponse schedule;
  private Long yes;
  private Long no;
  private Long total;
  private boolean isScheduleAproved;
}
