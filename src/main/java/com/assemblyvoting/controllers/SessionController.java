package com.assemblyvoting.controllers;

import com.assemblyvoting.domain.Session;
import com.assemblyvoting.models.requests.SessionRequest;
import com.assemblyvoting.services.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
/**
 * @author leandro-bezerra
 */
@RestController
@RequestMapping(value = "/v1", produces = "application/json")
public class SessionController {

  private final SessionService sessionService;

  public SessionController(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @PostMapping("/session")
  public ResponseEntity<Session> createSession(@RequestBody SessionRequest sessionRequest) {
    final var session = sessionService.openSession(sessionRequest, LocalDateTime.now());
    return session
        .map(it -> new ResponseEntity<>(it, HttpStatus.CREATED))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }
}
