package com.assemblyvoting.controllers;

import com.assemblyvoting.domain.Session;
import com.assemblyvoting.services.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
/**
 * @author leandro-bezerra
 */
@RestController
@RequestMapping(value = "/session", produces = "application/json")
public class SessionController {

    final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        Optional<Session> _session = sessionService.saveSession(session);

        return _session.map(it -> new ResponseEntity<>(it, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}