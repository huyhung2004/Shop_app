package com.example.shop.app.repositories;

import com.example.shop.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByPhoneNumber (String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
