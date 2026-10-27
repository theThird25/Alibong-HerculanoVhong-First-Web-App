package edu.bsit.userhub.alibong.dto;

import java.time.LocalDate;

public record DashboardResponse (String fullName, String email, String role, LocalDate memberSince, long totalUsers){
    
}
