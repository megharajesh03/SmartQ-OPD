package com.example.demo.bean;

import java.util.Date;
import jakarta.persistence.*;

// 1. ADD THIS IMPORT
import org.springframework.format.annotation.DateTimeFormat; 

@Entity
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(unique = true, nullable = false)
    private String insuranceNumber;

    @Column(nullable = false)
    private String providerName;

    private String coverageType;

    // 2. ADD ANNOTATION HERE
    @Temporal(TemporalType.DATE) // Good for SQL compatibility
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Tells Spring how to parse HTML date input
    private Date validityStart;

    // 3. ADD ANNOTATION HERE
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
    private Date validityEnd;

    private Double premiumAmount;

    @Column(nullable = false)
    private boolean status = true;

    
    
    public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public String getInsuranceNumber() {
		return insuranceNumber;
	}



	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}



	public String getProviderName() {
		return providerName;
	}



	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}



	public String getCoverageType() {
		return coverageType;
	}



	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}



	public Date getValidityStart() {
		return validityStart;
	}



	public void setValidityStart(Date validityStart) {
		this.validityStart = validityStart;
	}



	public Date getValidityEnd() {
		return validityEnd;
	}



	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}



	public Double getPremiumAmount() {
		return premiumAmount;
	}



	public void setPremiumAmount(Double premiumAmount) {
		this.premiumAmount = premiumAmount;
	}



	public boolean isStatus() {
		return status;
	}



	public void setStatus(boolean status) {
		this.status = status;
	}



	public Insurance() {
    }

}