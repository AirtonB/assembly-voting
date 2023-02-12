package com.assemblyvoting.repositories;

import com.assemblyvoting.domain.Session;
import org.springframework.data.repository.CrudRepository;
/**
 * @author leandro-bezerra
 */
public interface SessionRepository extends CrudRepository<Session, Long> {}
