package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.bean.User;

public interface AdminRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
