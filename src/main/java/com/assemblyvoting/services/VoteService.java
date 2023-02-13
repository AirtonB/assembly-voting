package com.assemblyvoting.services;

import com.assemblyvoting.domain.Vote;
import com.assemblyvoting.repositories.VoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author leandro-bezerra
 */
@Service
public class VoteService {
  private final VoteRepository voteRepository;
  private final SessionService sessionService;

  public VoteService(VoteRepository voteRepository, SessionService sessionService) {
    this.voteRepository = voteRepository;
    this.sessionService = sessionService;
  }

  public Optional<Vote> findVoteById(Long id) {
    return voteRepository.findById(id);
  }

  public Optional<Vote> saveVote(Vote vote) {
    final boolean isSessionOpened = sessionService.isSessionOpened(vote.getSchedule().getId());
    final boolean isIdentificationValid =
        Pattern.matches("^\\d{3}\\d{3}\\d{3}\\d{2}$", vote.getUserIdentification());

    if (isSessionOpened) {
      if (isIdentificationValid) {
        return Optional.of(voteRepository.save(vote));
      }
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão de votação encerrada!");
    }

    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão de votação encerrada!");
  }
}
