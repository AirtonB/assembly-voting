package com.assemblyvoting.repositories;

import com.assemblyvoting.domain.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author leandro-bezerra
 */
@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {
  List<Vote> findAll();

  @Query("SELECT count(v.id) FROM Vote v WHERE v.schedule.id = ?1 AND v.registeredVote = true")
  Long registeredYesVoteBySchedule(Long id);

  @Query("SELECT count(v.id) FROM Vote v WHERE v.schedule.id = ?1 AND v.registeredVote is not null")
  Long totalRegisteredVotes(Long id);

  Boolean existsByUserIdentification(String userIdentification);
}
