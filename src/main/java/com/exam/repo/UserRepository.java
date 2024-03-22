package com.exam.repo;

import com.exam.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);
    
    User findByEmail(String email);
    
    User findByToken(String token);
    
}
