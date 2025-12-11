package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.bean.ConsultationQueue;
import com.example.demo.bean.ConsultationQueue.ConsultationStatus;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationQueueRepository extends JpaRepository<ConsultationQueue, Long> {
    
    // Find the patient currently inside with the doctor (IN_CONSULTATION)
    Optional<ConsultationQueue> findByDoctorIdAndConsultationStatus(Long doctorId, ConsultationStatus status);

    // Find all patients waiting for a specific doctor, ordered by time (First Come First Serve)
    List<ConsultationQueue> findByDoctorIdAndConsultationStatusOrderByScheduledTimeAsc(Long doctorId, ConsultationStatus status);

    // Find the latest token for a specific user to show on their dashboard
    Optional<ConsultationQueue> findTopByUserIdOrderByScheduledTimeDesc(Long userId);
}
