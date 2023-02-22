package com.assemblyvoting.services;

import com.assemblyvoting.domain.Session;
import com.assemblyvoting.exceptions.ExceptionMessages;
import com.assemblyvoting.exceptions.NotFoundException;
import com.assemblyvoting.models.converters.SessionConverter;
import com.assemblyvoting.models.requests.SessionRequest;
import com.assemblyvoting.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
/**
 * @author leandro-bezerra
 */
@Service
public class SessionService {

  private final SessionRepository sessionRepository;
  private final ScheduleService scheduleService;
  private final SessionConverter sessionConverter;

  public SessionService(
      SessionRepository sessionRepository,
      ScheduleService scheduleService,
      SessionConverter sessionConverter) {
    this.sessionRepository = sessionRepository;
    this.scheduleService = scheduleService;
    this.sessionConverter = sessionConverter;
  }

  public Optional<Session> openSession(SessionRequest sessionRequest, LocalDateTime startSession) {

    var schedule = scheduleService.getSchedule(sessionRequest.getScheduleId());

    if (schedule.isEmpty()) throw new NotFoundException(ExceptionMessages.SCHEDULE_NOT_FOUND);

    sessionRequest.setScheduleId(schedule.get().getId());

    final var session = sessionConverter.fromRequestToDomain(sessionRequest, startSession);

    return Optional.of(sessionRepository.save(session));
  }

  public boolean isSessionOpened(Long scheduleId) {

    var session = sessionRepository.findSessionByScheduleId(scheduleId);
    if (session.isEmpty()) throw new NotFoundException(ExceptionMessages.SESSION_NOT_FOUND);

    return !LocalDateTime.now().isAfter(session.get().getEndSession());
  }
}
