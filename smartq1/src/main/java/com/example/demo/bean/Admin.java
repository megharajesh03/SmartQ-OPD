package com.example.demo.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")  // Ensure it uses the correct table name
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment for the ID
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;  // Unique username for each user

    @Column(nullable = false)
    private String password;  // Store password (preferably encrypted)

    @Column(nullable = false)
    private String role;  // Roles like ADMIN, DOCTOR, USER

}