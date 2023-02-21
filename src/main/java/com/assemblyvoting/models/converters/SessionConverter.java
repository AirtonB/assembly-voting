package com.assemblyvoting.models.converters;

import com.assemblyvoting.domain.Session;
import com.assemblyvoting.models.requests.SessionRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class SessionConverter {
  private final ModelMapper modelMapper = new ModelMapper();
  private static final long DEFAULT_SESSION_TIME_IN_MINUTES = 1;

  public Session fromRequestToDomain(SessionRequest sessionRequest, LocalDateTime startSession) {

    var session = modelMapper.map(sessionRequest, Session.class);
    session.setStartSession(startSession);

    if (Objects.isNull(sessionRequest.getSessionEndTime()))
      session.setEndSession(startSession.plusMinutes(DEFAULT_SESSION_TIME_IN_MINUTES));

    return session;
  }
}
