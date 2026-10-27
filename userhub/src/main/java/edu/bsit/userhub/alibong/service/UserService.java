package edu.bsit.userhub.alibong.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;

import edu.bsit.userhub.alibong.dto.RegisterRequest;
import edu.bsit.userhub.alibong.dto.UserResponse;
import edu.bsit.userhub.alibong.model.User;
import edu.bsit.userhub.alibong.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;
    private final Map<String, Long> sessions = new concurrentHashMap<>();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserResponse register(RegisterRequest req) {
        if(userRepository.existsByEmail(req.email()))
            throw new IllegalArgumentException("Email already used");
        User user = new User();
        user.setFullName(req.fullName());
        user.setEmail(req.email());
        user.setPassword(hash(req.password()));
        return toResponse(userRepository.save(user));
    }

    private String hash(String raw) {
        try{
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
}
