package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.bean.ConsultationQueue;
import com.example.demo.bean.Doctor;
import com.example.demo.bean.User;
import com.example.demo.dao.ConsultationQueueRepository; // You need to create/update this Repo
import com.example.demo.dao.DoctorRepository;
import com.example.demo.dao.UserRepository;

@Service
public class QueueService {

    @Autowired
    private ConsultationQueueRepository queueRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;

    public ConsultationQueue joinQueue(Long userId, Long doctorId) {
        User user = userRepository.findById(userId).orElse(null);
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        // 1. Calculate the next token number for THIS doctor
        // We count how many tokens exist for this doctor today (or total active)
        int nextTokenNum = queueRepository.countByDoctorId(doctorId) + 1;

        ConsultationQueue queue = new ConsultationQueue();
        queue.setUser(user);
        queue.setDoctor(doctor);
        queue.setTokenNumber(nextTokenNum); // Set the friendly number (1, 2, 3...)
        queue.setConsultationStatus(ConsultationQueue.ConsultationStatus.WAITING);
        
        return queueRepository.save(queue);
    }

    public ConsultationQueue getUserToken(Long userId) {
        // Get the latest active token (not completed)
        return queueRepository.findTopByUserIdAndConsultationStatusNotOrderByIdDesc(
                userId, ConsultationQueue.ConsultationStatus.COMPLETED);
    }
    
    // Get the very last token (even if completed) for the "Done" screen
    public ConsultationQueue getLastToken(Long userId) {
        return queueRepository.findTopByUserIdOrderByIdDesc(userId);
    }

    public void callNextPatient(Long doctorId) {
        // 1. Find the patient currently IN_CONSULTATION and mark them COMPLETED
        ConsultationQueue current = queueRepository.findByDoctorIdAndConsultationStatus(
                doctorId, ConsultationQueue.ConsultationStatus.IN_CONSULTATION);
        
        if (current != null) {
            current.setConsultationStatus(ConsultationQueue.ConsultationStatus.COMPLETED);
            queueRepository.save(current);
        }

        // 2. Find the next WAITING patient and mark them IN_CONSULTATION
        ConsultationQueue next = queueRepository.findFirstByDoctorIdAndConsultationStatusOrderByIdAsc(
                doctorId, ConsultationQueue.ConsultationStatus.WAITING);
        
        if (next != null) {
            next.setConsultationStatus(ConsultationQueue.ConsultationStatus.IN_CONSULTATION);
            queueRepository.save(next);
        }
    }
    
    // LOGIC: Calculate Estimated Wait Time
    public int calculateWaitTime(Long doctorId, Long currentTokenId) {
        // Count how many people are WAITING with an ID less than mine
        int peopleAhead = queueRepository.countByDoctorIdAndConsultationStatusAndIdLessThan(
                doctorId, ConsultationQueue.ConsultationStatus.WAITING, currentTokenId);
        
        // If there is someone currently inside (IN_CONSULTATION), add them to the wait time
        boolean isDoctorBusy = queueRepository.existsByDoctorIdAndConsultationStatus(
                doctorId, ConsultationQueue.ConsultationStatus.IN_CONSULTATION);
        
        if(isDoctorBusy) {
            peopleAhead++; 
        }

        return peopleAhead * 10; // 10 minutes per person
    }

    // Doctor Stats
    public int getWaitingCount(Long doctorId) {
        return queueRepository.countByDoctorIdAndConsultationStatus(doctorId, ConsultationQueue.ConsultationStatus.WAITING);
    }

    public int getServedCount(Long doctorId) {
        return queueRepository.countByDoctorIdAndConsultationStatus(doctorId, ConsultationQueue.ConsultationStatus.COMPLETED);
    }
    
    public ConsultationQueue getCurrentPatient(Long doctorId) {
        return queueRepository.findByDoctorIdAndConsultationStatus(doctorId, ConsultationQueue.ConsultationStatus.IN_CONSULTATION);
    }
}