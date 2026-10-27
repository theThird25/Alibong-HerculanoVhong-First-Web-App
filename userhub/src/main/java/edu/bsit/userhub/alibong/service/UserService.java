package edu.bsit.userhub.alibong.service;

import java.util.Map;

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
}
