package edu.bsit.userhub.alibong.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.bsit.userhub.alibong.dto.DashboardResponse;
import edu.bsit.userhub.alibong.dto.LoginRequest;
import edu.bsit.userhub.alibong.dto.LoginResponse;
import edu.bsit.userhub.alibong.dto.RegisterRequest;
import edu.bsit.userhub.alibong.dto.UpdateProfileRequest;
import edu.bsit.userhub.alibong.dto.UserResponse;
import edu.bsit.userhub.alibong.exception.AuthException;
import edu.bsit.userhub.alibong.exception.ConflictException;
import edu.bsit.userhub.alibong.exception.ForbiddenException;
import edu.bsit.userhub.alibong.model.Role;
import edu.bsit.userhub.alibong.model.User;
import edu.bsit.userhub.alibong.repository.UserRepository;
import edu.bsit.userhub.alibong.security.PasswordHasher;
import edu.bsit.userhub.alibong.security.SessionStore;

@Service
public class UserService {

    private static final String BEARER = "Bearer ";

    private final UserRepository repo;
    private final PasswordHasher hasher;
    private final SessionStore sessions;

    public UserService(UserRepository repo, PasswordHasher hasher, SessionStore sessions) {
        this.repo = repo;
        this.hasher = hasher;
        this.sessions = sessions;
    }

    public UserResponse register(RegisterRequest req) {
        String email = normalize(req.email());
        if (repo.existsByEmail(email)) {
            throw new ConflictException("Email already used");
        }
        User user = new User(req.fullName().trim(), email,
                hasher.hash(req.password()), Role.USER);
        return UserResponse.from(repo.save(user));
    }

    public LoginResponse login(LoginRequest req) {
        if (req.email() == null || req.password() == null) {
            throw new AuthException("Invalid credentials");
        }
        // same error for wrong email and wrong password
        User user = repo.findByEmail(normalize(req.email()))
                .filter(u -> hasher.matches(req.password(), u.getPasswordHash()))
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        String token = sessions.create(user.getId());
        return new LoginResponse(token, UserResponse.from(user));
    }

    public User authenticate(String header) {
        String token = extractToken(header);
        Long userId = sessions.findUserId(token)
                .orElseThrow(() -> new AuthException("Session expired"));
        return repo.findById(userId)
                .orElseThrow(() -> new AuthException("Session expired"));
    }

    public void logout(String header) {
        if (header != null && !header.isBlank()) {
            sessions.invalidate(extractToken(header));
        }
    }

    public void requireRole(User user, Role required) {
        if (!user.hasRole(required)) {
            throw new ForbiddenException("Access denied");
        }
    }

    public DashboardResponse dashboard(User user) {
        return new DashboardResponse(user.getFullName(), user.getEmail(),
                user.getRole(), user.getCreatedAt(), repo.count());
    }

    public List<UserResponse> findAll() {
        return repo.findAllByOrderByIdAsc().stream()
                .map(UserResponse::from)
                .toList();
    }

    public UserResponse updateProfile(User user, UpdateProfileRequest req) {
        user.rename(req.fullName());
        return UserResponse.from(repo.save(user));
    }

    public void seedAdmin(String fullName, String email, String rawPassword) {
        String normalized = normalize(email);
        if (repo.existsByEmail(normalized))
            return;
        repo.save(new User(fullName, normalized, hasher.hash(rawPassword), Role.ADMIN));
    }

    private String extractToken(String header) {
        if (header == null || header.isBlank()) {
            throw new AuthException("Please log in");
        }
        return header.startsWith(BEARER)
                ? header.substring(BEARER.length()).trim()
                : header.trim();
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
