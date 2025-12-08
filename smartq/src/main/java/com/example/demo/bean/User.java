package com.example.demo.bean;
import jakarta.persistence.*;

@Entity
@Table(name = "users")  // Ensure it uses the correct table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment for the ID
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;  // Unique username for each user

    @Column(nullable = false)
    private String password;  // Store password (preferably encrypted)

    @Column(nullable = false)
    private String role;  // Roles like ADMIN, DOCTOR, USER
    
    // Optional: you can add more fields such as full name, contact, etc.
    private String fullName;
    private String email;
    private String phone;

    // --- Constructors ---
    
    public User() {
        // Default constructor (for JPA)
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
