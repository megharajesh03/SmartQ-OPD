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
    
    @Column(nullable = false)
    private int age;

    private String address;

    private String gender;

    private String phone;

    private String insuranceId;
    
   
    // --- Constructors ---
    
    public User() {
        // Default constructor (for JPA)
    }


	public User(Long id, String username, String password, String role, int age, String address, String gender,
			String phone, String insuranceId) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.age = age;
		this.address = address;
		this.gender = gender;
		this.phone = phone;
		this.insuranceId = insuranceId;
	}


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


	public String getInsuranceId() {
		return insuranceId;
	}


	public void setInsuranceId(String insuranceId) {
		this.insuranceId = insuranceId;
	}
   
}
