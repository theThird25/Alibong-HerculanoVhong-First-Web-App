package edu.bsit.userhub.alibong;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.bsit.userhub.alibong.service.UserService;

@SpringBootApplication
public class UserhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserhubApplication.class, args);
	}

	@Bean
	CommandLineRunner seedAdmin(UserService service) {
		return args -> {
			service.seedAdmin("Admin", "admin@bsit.edu", "admin12345");
			System.out.println(">>> ADMIN SEEDED: admin@bsit.edu / admin12345");
		};
	}
}