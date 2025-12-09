package com.example.demo.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT behavior in SQL
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    @Column(nullable = false)
    private String password;

 

	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // Constructors, Getters, and Setters

    public Doctor() {
    }

    public Doctor(String name, String specialization, String email, String phone, Status status) {
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.phone = phone;
        this.status = status;
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

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	 public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	 @Override
	 public String toString() {
		 return "Doctor{id=" + id + ", name='" + name + "', specialization='" + specialization + "', email='" + email + "', phone='" + phone + "', status=" + status + "}";
	 }
	
	// Enum to represent the consultation status
    public enum Status {
        AVAILABLE,NOT_AVAILABLE
    }

	
}
