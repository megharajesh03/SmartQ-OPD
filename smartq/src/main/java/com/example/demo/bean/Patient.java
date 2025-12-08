package com.example.demo.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT behavior in SQL
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    private String address;

    private String gender;

    @Column(unique = true)
    private String email;

    private String phone;

    private String insuranceId;

    // Constructors, Getters, and Setters

    public Patient() {
    }

    public Patient(String name, int age, String address, String gender, String email, String phone, String insuranceId) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.insuranceId = insuranceId;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    @Override
    public String toString() {
        return "Patient{id=" + id + ", name='" + name + "', age=" + age + ", address='" + address + "', gender='" + gender + "', email='" + email + "', phone='" + phone + "', insuranceId='" + insuranceId + "'}";
    }
}
