package com.assemblyvoting.services;

import com.assemblyvoting.domain.Vote;
import com.assemblyvoting.repositories.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * @author leandro-bezerra
 */
@Service
public class VoteService {
  final VoteRepository voteRepository;

  public VoteService(VoteRepository voteRepository) {
    this.voteRepository = voteRepository;
  }

  public Optional<Vote> getVote(Long id) {
    return voteRepository.findById(id);
  }

  public Optional<Vote> saveVote(Vote vote) {
    try {
      voteRepository.save(vote);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.of(vote);
  }
}
