package edu.bsit.userhub.alibong.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
                @NotBlank(message = "Full name is required") String fullName,
                @NotBlank(message = "Email is required") @Email(message = "Email is not valid") String email,
                @Size(min = 6, message = "Password must be at least 6 characters") String password) {

}
