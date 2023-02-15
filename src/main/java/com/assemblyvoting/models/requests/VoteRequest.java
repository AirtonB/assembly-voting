package com.assemblyvoting.models.requests;

import lombok.Data;

@Data
public class VoteRequest {
  private Boolean registeredVote;
  private Long scheduleId;
  private String userIdentification;
}
