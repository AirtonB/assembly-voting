package com.assemblyvoting.models.requests;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VoteRequest {
  private Boolean registeredVote;
  private Long scheduleId;
  private String userIdentification;

  @Builder(toBuilder = true)
  public VoteRequest(Boolean registeredVote, Long scheduleId, String userIdentification) {
    this.registeredVote = registeredVote;
    this.scheduleId = scheduleId;
    this.userIdentification = userIdentification;
  }
}
