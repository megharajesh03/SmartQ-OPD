package com.example.demo.bean;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
    
    @Column(nullable = false)
    private int age;

    private String address;

    private String gender;

    private String phone;

    // --- RELATIONSHIP CHANGE START ---
    
    // 1. Removed 'String insuranceId'
    // 2. Added 'Insurance' object
    // 3. mappedBy = "user" tells Hibernate that the Foreign Key is in the Insurance table
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Insurance insurance;
    
    // --- RELATIONSHIP CHANGE END ---

    // --- Constructors ---
    
    public User() {
    }

    // Updated constructor to accept Insurance object instead of String insuranceId
    public User(Long id, String username, String password, String role, int age, String address, String gender, String phone) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.age = age;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
    }

    // Getters and Setters

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // New Getter and Setter for Insurance Entity
    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }
}