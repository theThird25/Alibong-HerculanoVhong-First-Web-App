package edu.bsit.userhub.alibong.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.bsit.userhub.alibong.dto.DashboardResponse;
import edu.bsit.userhub.alibong.dto.UserResponse;
import edu.bsit.userhub.alibong.model.Role;
import edu.bsit.userhub.alibong.service.UserService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private static final String AUTH = "Authorization";

    private final UserService service;

    public DashboardController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public DashboardResponse home(
            @RequestHeader(value = AUTH, required = false) String token) {
        return service.dashboard(service.authenticate(token));
    }

    @GetMapping("/users") // ADMIN only
    public List<UserResponse> users(
            @RequestHeader(value = AUTH, required = false) String token) {
        service.requireRole(service.authenticate(token), Role.ADMIN);
        return service.findAll();
    }
}
