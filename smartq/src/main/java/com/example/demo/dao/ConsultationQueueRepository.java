package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.bean.ConsultationQueue;
import com.example.demo.bean.ConsultationQueue.ConsultationStatus;

@Repository
public interface ConsultationQueueRepository extends JpaRepository<ConsultationQueue, Long> {

    // 1. Count total tokens for a specific doctor (used to generate the next Token # 1, 2, 3...)
    int countByDoctorId(Long doctorId);

    // 2. Find the user's current active token (Wait or Active), ignoring Completed ones
    // We order by ID Descending to get the latest one just in case
    ConsultationQueue findTopByUserIdAndConsultationStatusNotOrderByIdDesc(Long userId, ConsultationStatus status);

    // 3. Find the absolute last token for a user (used for the "Billing/Done" screen)
    ConsultationQueue findTopByUserIdOrderByIdDesc(Long userId);

    // 4. Find the patient currently inside the doctor's room (IN_CONSULTATION)
    ConsultationQueue findByDoctorIdAndConsultationStatus(Long doctorId, ConsultationStatus status);

    // 5. Find the next person in the waiting line (WAITING, ordered by time/ID ascending)
    ConsultationQueue findFirstByDoctorIdAndConsultationStatusOrderByIdAsc(Long doctorId, ConsultationStatus status);

    // 7. Check if the doctor is currently busy (used to add extra time to estimate)
    boolean existsByDoctorIdAndConsultationStatus(Long doctorId, ConsultationStatus status);

    // 8. Stats: Count specific statuses (How many Waiting? How many Completed?)
    int countByDoctorIdAndConsultationStatus(Long doctorId, ConsultationStatus status);
    
 // Add this inside the interface
    int countByDoctorIdAndConsultationStatusAndIdLessThan(Long doctorId, ConsultationQueue.ConsultationStatus status, Long id);
}