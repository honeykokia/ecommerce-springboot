package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.UserBean;

@Repository
public interface UserRepository extends JpaRepository<UserBean, Long> {
    // Define methods for user operations, e.g., find by email, save user, etc.
    Optional<UserBean> findByEmail(String email);
    Boolean existsByEmail(String email);
    // Additional methods can be defined as needed

}
