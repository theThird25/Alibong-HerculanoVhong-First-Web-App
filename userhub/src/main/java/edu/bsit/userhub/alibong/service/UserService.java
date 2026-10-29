package edu.bsit.userhub.alibong.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import edu.bsit.userhub.alibong.dto.DashboardResponse;
import edu.bsit.userhub.alibong.dto.LoginRequest;
import edu.bsit.userhub.alibong.dto.LoginResponse;
import edu.bsit.userhub.alibong.dto.RegisterRequest;
import edu.bsit.userhub.alibong.dto.UserResponse;
import edu.bsit.userhub.alibong.exception.AuthException;
import edu.bsit.userhub.alibong.exception.ForbiddenException;
import edu.bsit.userhub.alibong.model.User;
import edu.bsit.userhub.alibong.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Map<String, Long> sessions = new ConcurrentHashMap<>();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email()))
            throw new IllegalArgumentException("Email is already used");
        User user = new User();
        user.setFullName(req.fullName());
        user.setEmail(req.email());
        user.setPassword(hash(req.password()));
        return toResponse(userRepository.save(user));
    }

    private String hash(String raw) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256")
                    .digest(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(d);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new AuthException("Invalid email or password"));
        if (!user.getPassword().equals(hash(req.password())))
            throw new AuthException("Invalid email or password");
        String token = UUID.randomUUID().toString();
        sessions.put(token, user.getId());
        return new LoginResponse(token, toResponse(user));
    }

    public User authenticate(String header) {
        if (header == null)
            throw new AuthException("Please login");
        Long id = sessions.get(header.replace("Bearer ", ""));
        if (id == null)
            throw new AuthException("Session expired, please login again");
        return userRepository.findById(id).orElseThrow();
    }

    public DashboardResponse dashboard(User user) {
        return new DashboardResponse(user.getFullName(), user.getEmail(), user.getRole(),
                user.getCreatedAt(), userRepository.count());
    }

    public void requireRole(User user, String role) {
        if (!role.equals(user.getRole()))
            throw new ForbiddenException("Access Denied");
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

}
