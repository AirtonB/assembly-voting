package com.assemblyvoting.models.converters;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.models.responses.ScheduleResponse;
import com.assemblyvoting.models.responses.VoteReponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VoteConverter {
  private final ModelMapper modelMapper = new ModelMapper();

  public VoteReponse toResponse(
      Schedule schedule, Long yesVote, Long noVote, Long totalVotes, boolean scheduleAproved) {

    VoteReponse voteReponse = new VoteReponse();
    ScheduleResponse scheduleResponse = new ScheduleResponse(schedule.getDescription());

    voteReponse.setSchedule(scheduleResponse);
    voteReponse.setYes(yesVote);
    voteReponse.setNo(noVote);
    voteReponse.setTotal(totalVotes);
    voteReponse.setScheduleAproved(scheduleAproved);

    return modelMapper.map(voteReponse, VoteReponse.class);
  }
}
