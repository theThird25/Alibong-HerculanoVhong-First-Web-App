package edu.bsit.userhub.alibong.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.bsit.userhub.alibong.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
