package com.assemblyvoting.services;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.domain.Vote;
import com.assemblyvoting.exceptions.ExceptionMessages;
import com.assemblyvoting.models.converters.VoteConverter;
import com.assemblyvoting.models.requests.VoteRequest;
import com.assemblyvoting.models.responses.CPFResponse;
import com.assemblyvoting.models.responses.VoteReponse;
import com.assemblyvoting.repositories.VoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * @author leandro-bezerra
 */
@Service
public class VoteService {
  private final VoteRepository voteRepository;
  private final SessionService sessionService;
  private final VoteConverter voteConverter;
  private final ScheduleService scheduleService;
  private final CPFValidatorService cpfValidatorService;

  public VoteService(
      VoteRepository voteRepository,
      SessionService sessionService,
      VoteConverter voteConverter,
      ScheduleService scheduleService,
      CPFValidatorService cpfValidatorService) {
    this.voteRepository = voteRepository;
    this.sessionService = sessionService;
    this.voteConverter = voteConverter;
    this.scheduleService = scheduleService;
    this.cpfValidatorService = cpfValidatorService;
  }

  public Optional<VoteReponse> findResultByScheduleId(Long id) {
    Optional<Schedule> schedule = scheduleService.getSchedule(id);
    boolean isSessionOpened = sessionService.isSessionOpened(id);

    if (isSessionOpened) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionMessages.OPENED_SESSION);
    }

    VoteReponse voteReponse = new VoteReponse();

    if (schedule.isPresent()) {
      final Long yesVote = voteRepository.registeredYesVoteBySchedule(schedule.get().getId());
      final Long totalVotes = voteRepository.totalRegisteredVotes(schedule.get().getId());
      final Long noVote = totalVotes - yesVote;
      final boolean scheduleAproved = yesVote > noVote;
      voteReponse =
          voteConverter.toResponse(schedule.get(), yesVote, noVote, totalVotes, scheduleAproved);
    }

    return Optional.of(voteReponse);
  }

  public Optional<Vote> saveVote(VoteRequest voteRequest) {

    Vote vote = voteConverter.fromRequestToDomain(voteRequest);

    final boolean isSessionOpened = sessionService.isSessionOpened(vote.getSchedule().getId());
    final CPFResponse cpfResponse = cpfValidatorService.checkCpf(vote.getUserIdentification());

    if (!isSessionOpened)
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionMessages.CLOSSED_SESSION);

    if (!cpfResponse.isValid()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionMessages.INVALID_CPF);
    }

    final boolean isUserNotAbleToVote =
        voteRepository.existsByUserIdentification(vote.getUserIdentification());

    if (isUserNotAbleToVote) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, ExceptionMessages.HAS_USER_ALREADY_VOTE);
    }

    return Optional.of(voteRepository.save(vote));
  }
}
