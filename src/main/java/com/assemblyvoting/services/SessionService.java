package com.assemblyvoting.services;

import com.assemblyvoting.domain.Schedule;
import com.assemblyvoting.domain.Session;
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

  public Optional<Session> openSession(SessionRequest sessionRequest) {

    Optional<Schedule> schedule = scheduleService.getSchedule(sessionRequest.getScheduleId());

    if (schedule.isEmpty()) throw new RuntimeException("Erro: Pauta não encontrada!");

    sessionRequest.setScheduleId(schedule.get().getId());

    Session session = sessionConverter.fromRequestToDomain(sessionRequest, LocalDateTime.now());

    return Optional.of(sessionRepository.save(session));
  }

  public boolean isSessionOpened(Long scheduleId) {

    Optional<Session> session = sessionRepository.findSessionByScheduleId(scheduleId);
    if (session.isEmpty()) throw new RuntimeException("Erro: Sessão de votação não encontrada!");

    if (LocalDateTime.now().isAfter(session.get().getEndSession())) return false;

    return true;
  }
}
