package org.astrobrains.repository;

import org.astrobrains.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find user by email
    Optional<User> findByEmail(String email);
}
