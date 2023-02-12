package com.assemblyvoting.repositories;

import com.assemblyvoting.domain.Schedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author leandro-bezerra
 */
@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
  List<Schedule> findAll();
}
