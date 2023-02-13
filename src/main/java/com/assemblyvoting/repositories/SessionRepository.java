package com.assemblyvoting.repositories;

import com.assemblyvoting.domain.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author leandro-bezerra
 */
public interface SessionRepository extends CrudRepository<Session, Long> {
  Optional<Session> findSessionByScheduleId(Long id);
}
