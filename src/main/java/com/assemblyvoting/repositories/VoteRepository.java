package com.assemblyvoting.repositories;

import com.assemblyvoting.domain.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author leandro-bezerra
 */
@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {
  List<Vote> findAll();
}
