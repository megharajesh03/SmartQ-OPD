package com.example.demo.bean;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT behavior in SQL
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;  // Foreign Key to Patient

    @Column(unique = true, nullable = false)
    private String insuranceNumber;

    @Column(nullable = false)
    private String providerName;

    private String coverageType;

    private Date validityStart;

    private Date validityEnd;

    private Double premiumAmount;

    @Column(nullable = false)
    private boolean status = true;  // Default value is true

    // Constructors, Getters, and Setters

    public Insurance() {
    }

    public Insurance(User user, String insuranceNumber, String providerName, String coverageType, Date validityStart, Date validityEnd, Double premiumAmount, boolean status) {
        this.user = user;
        this.insuranceNumber = insuranceNumber;
        this.providerName = providerName;
        this.coverageType = coverageType;
        this.validityStart = validityStart;
        this.validityEnd = validityEnd;
        this.premiumAmount = premiumAmount;
        this.status = status;
    }

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

}
