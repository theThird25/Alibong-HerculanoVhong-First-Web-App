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

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final SessionStore sessionStore;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher, SessionStore sessionStore) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.sessionStore = sessionStore;
    }

    public UserResponse register(RegisterRequest req) {
        String email = normalize(req.email());

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already used");
        }

        User user = new User(
                req.fullName().trim(),
                email,
                passwordHasher.hash(req.password()),
                Role.USER);

        return UserResponse.from(userRepository.save(user));
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException("Invalid email or password"));

        String inputHash = passwordHasher.hash(request.password());
        if (!user.getPasswordHash().equals(inputHash)) {
            throw new AuthException("Invalid email or password");
        }

        String token = sessionStore.create(user.getId());

        return new LoginResponse(token, UserResponse.from(user));
    }

    public User authenticate(String header) {
        String token = extractToken(header);

        Long userId = sessionStore.findUserId(token)
                .orElseThrow(() -> new AuthException("Session expired"));

        return userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("Session expired"));
    }

    public void logout(String header) {
        if (header != null && !header.isBlank()) {
            sessionStore.invalidate(extractToken(header));
        }
    }

    public void requireRole(User user, Role required) {
        if (!user.hasRole(required)) {
            throw new ForbiddenException("Access denied");
        }
    }

    public DashboardResponse dashboard(User user) {
        return new DashboardResponse(
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                userRepository.count());
    }

    public List<UserResponse> findAll() {
        return userRepository.findAllByOrderByIdAsc()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    public UserResponse updateProfile(User user, UpdateProfileRequest req) {
        user.rename(req.fullName());
        return UserResponse.from(userRepository.save(user));
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

    public void seedAdmin(String fullName, String email, String rawPassword) {
        String normalizedEmail = normalize(email);

        if (userRepository.existsByEmail(normalizedEmail)) {
            return;
        }

        User admin = new User(
                fullName.trim(),
                normalizedEmail,
                passwordHasher.hash(rawPassword),
                Role.ADMIN);

        userRepository.save(admin);
    }
}