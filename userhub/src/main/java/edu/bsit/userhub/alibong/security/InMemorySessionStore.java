package edu.bsit.userhub.alibong.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class InMemorySessionStore implements SessionStore {

    private static final Duration TTL = Duration.ofMinutes(30);

    private record Session(Long userId, Instant expiresAt) {
    }

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public String create(Long userId) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, new Session(userId, Instant.now().plus(TTL)));
        return token;
    }

    @Override
    public Optional<Long> findUserId(String token) {
        if (token == null)
            return Optional.empty();
        Session s = sessions.get(token);
        if (s == null)
            return Optional.empty();
        if (Instant.now().isAfter(s.expiresAt())) {
            sessions.remove(token);
            return Optional.empty();
        }
        return Optional.of(s.userId());
    }

    @Override
    public void invalidate(String token) {
        if (token != null)
            sessions.remove(token);
    }
}
