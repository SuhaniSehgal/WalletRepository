package com.example.demospringboot.repo;

import com.example.demospringboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for User Management
 */
public interface UserRepo extends JpaRepository<User, Integer> {
}
