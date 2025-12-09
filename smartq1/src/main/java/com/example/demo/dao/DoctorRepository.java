package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.bean.Doctor;
import com.example.demo.bean.User;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Doctor findByUsername(String username);
}
