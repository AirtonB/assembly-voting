package com.assemblyvoting.models.converters;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.domain.Vote;
import com.assemblyvoting.models.requests.VoteRequest;
import com.assemblyvoting.models.responses.ScheduleResponse;
import com.assemblyvoting.models.responses.VoteReponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VoteConverter {
  private final ModelMapper modelMapper = new ModelMapper();

  public Vote fromRequestToDomain(VoteRequest voteRequest) {
    return modelMapper.map(voteRequest, Vote.class);
  }

  public VoteReponse toResponse(
      Schedule schedule, Long yesVote, Long noVote, Long totalVotes, boolean scheduleAproved) {
    ScheduleResponse scheduleResponse = new ScheduleResponse(schedule.getDescription());

    var voteReponse =
        VoteReponse.builder()
            .schedule(scheduleResponse)
            .yes(yesVote)
            .no(noVote)
            .total(totalVotes)
            .isScheduleAproved(scheduleAproved)
            .build();

    return modelMapper.map(voteReponse, VoteReponse.class);
  }
}
