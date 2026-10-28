package edu.bsit.userhub.alibong.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.bsit.userhub.alibong.dto.DashboardResponse;
import edu.bsit.userhub.alibong.service.UserService;

@RestController
@RequestMapping ("/api/dashboard")
@CrossOrigin (origins = "http://localhost:5173")
public class DashboardController {

    private static final String AUTH = "Authorization";
    private final UserService userService;

    @GetMapping 
    public DashboardResponse home(
        @RequestHeader (value = AUTH, required = false) String token) {
            return userService.dashboard(userService.authenticate(token));
        }
    
    @GetMapping ("/users")
    public List<UserResponse> users(
        @RequestHeader (value = AUTH, required = false) String token) {
            userService.requireRole(userService.authenticate(token), "ADMIN");
            return userService.findAll();
        }
    
}
