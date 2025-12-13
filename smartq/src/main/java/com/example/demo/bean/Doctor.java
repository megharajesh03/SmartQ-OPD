package com.example.demo.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT behavior in SQL
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    @Column(nullable = false)
    private String password;

    // --- ADD THIS FIELD ---
    private int servedCount = 0; 
    // ----------------------
	

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



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Status getStatus() {
		return status;
	}



	public void setStatus(Status status) {
		this.status = status;
	}

	// --- ADD THESE GETTERS AND SETTERS ---
    public int getServedCount() {
        return servedCount;
    }

    public void setServedCount(int servedCount) {
        this.servedCount = servedCount;
    }

	// Enum to represent the consultation status
    public enum Status {
        AVAILABLE,NOT_AVAILABLE
    }
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

	
}
