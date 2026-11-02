package main.java.edu.bsit.userhub.alibong.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import edu.bsit.userhub.alibong.service.UserService;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserService service;

    public DataSeeder(UserService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        service.seedAdmin("Admin", "admin@bsit.edu", "admin12345");
        System.out.println(">>> ADMIN SEEDED: admin@bsit.edu / admin12345");
    }
}