package edu.bsit.userhub.alibong.dto;

import java.time.LocalDate;

import edu.bsit.userhub.alibong.model.Role;

public record DashboardResponse(String fullName, String email, Role role,
                LocalDate memberSince, long totalUsers) {
}