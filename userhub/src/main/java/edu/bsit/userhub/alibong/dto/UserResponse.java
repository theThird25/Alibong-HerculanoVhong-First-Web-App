package edu.bsit.userhub.alibong.dto;

import edu.bsit.userhub.alibong.model.Role;
import edu.bsit.userhub.alibong.model.User;

public record UserResponse(Long id, String fullName, String email, Role role) {

    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole());
    }
}
