package edu.bsit.userhub.alibong.security;

import java.util.Optional;

public interface SessionStore {
    String create(Long userId);

    Optional<Long> findUserId(String token);

    void invalidate(String token);
}