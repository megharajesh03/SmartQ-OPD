package com.example.demo.bean;

import java.util.Date;
import jakarta.persistence.*;

@Entity
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- RELATIONSHIP CHANGE START ---
    
    // Changed from @ManyToOne to @OneToOne
    // This side holds the Foreign Key (user_id)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true) // unique=true ensures 1:1 in the DB schema
    private User user;
    
    // --- RELATIONSHIP CHANGE END ---

    @Column(unique = true, nullable = false)
    private String insuranceNumber;

    @Column(nullable = false)
    private String providerName;

    private String coverageType;

    private Date validityStart;

    private Date validityEnd;

    private Double premiumAmount;

    @Column(nullable = false)
    private boolean status = true;

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