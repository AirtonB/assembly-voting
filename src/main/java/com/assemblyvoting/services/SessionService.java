package com.assemblyvoting.services;

import com.assemblyvoting.domain.Session;
import com.assemblyvoting.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * @author leandro-bezerra
 */
@Service
public class SessionService {

    final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Optional<Session> saveSession(Session session) {
        try{
            sessionRepository.save(session);
        }catch(Exception e){
            e.printStackTrace();
        }

        return Optional.of(session);
    }
}
