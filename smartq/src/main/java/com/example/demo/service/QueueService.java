package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bean.ConsultationQueue;
import com.example.demo.bean.Doctor;
import com.example.demo.bean.User;
import com.example.demo.dao.ConsultationQueueRepository;
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

    // =========================================================
    //  PART 1: RESET LOGIC (Queue + Doctor Stats)
    // =========================================================
    
    @Transactional
    public void resetDailyQueue() {
        System.out.println("⚠️ Action: Resetting System for a new day...");
        
        // 1. Clear the Waiting Queue
        queueRepository.deleteAll();
        
        // 2. Reset "Served Patients" count for ALL Doctors to 0
        List<Doctor> allDoctors = doctorRepository.findAll();
        
        for (Doctor doc : allDoctors) {
            doc.setServedCount(0); // <--- This will now work!
        }
        
        doctorRepository.saveAll(allDoctors);
        
        System.out.println("✅ Queue cleared & Doctor stats reset to 0.");
    }

    /**
     * Automatic Reset: Runs every night at Midnight (00:00).
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanQueueAtMidnight() {
        resetDailyQueue();
    }

    // =========================================================
    //  PART 2: QUEUE OPERATIONS
    // =========================================================

    public ConsultationQueue joinQueue(Long userId, Long doctorId) {
        User user = userRepository.findById(userId).orElse(null);
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        // Logic: 0 + 1 = Token #1
        int nextTokenNum = queueRepository.countByDoctorId(doctorId) + 1;

        ConsultationQueue queue = new ConsultationQueue();
        queue.setUser(user);
        queue.setDoctor(doctor);
        queue.setTokenNumber(nextTokenNum); 
        queue.setConsultationStatus(ConsultationQueue.ConsultationStatus.WAITING);
        
        return queueRepository.save(queue);
    }
    
    public ConsultationQueue getUserToken(Long userId) {
        return queueRepository.findTopByUserIdAndConsultationStatusNotOrderByIdDesc(
                userId, ConsultationQueue.ConsultationStatus.COMPLETED);
    }

    public ConsultationQueue getLastToken(Long userId) {
        return queueRepository.findTopByUserIdOrderByIdDesc(userId);
    }

    // =========================================================
    //  PART 3: DOCTOR ACTIONS
    // =========================================================

    public boolean callNextPatient(Long doctorId) {
        boolean patientWasServed = false;

        // 1. Mark current patient as COMPLETED
        ConsultationQueue current = queueRepository.findByDoctorIdAndConsultationStatus(
                doctorId, ConsultationQueue.ConsultationStatus.IN_CONSULTATION);
        
        if (current != null) {
            current.setConsultationStatus(ConsultationQueue.ConsultationStatus.COMPLETED);
            queueRepository.save(current);
            patientWasServed = true; // Signal that we served someone
            
            // Increment the Doctor's personal counter manually here
            Doctor doc = doctorRepository.findById(doctorId).orElse(null);
            if(doc != null) {
                doc.setServedCount(doc.getServedCount() + 1);
                doctorRepository.save(doc);
            }
        }

        // 2. Pull next patient in
        ConsultationQueue next = queueRepository.findFirstByDoctorIdAndConsultationStatusOrderByIdAsc(
                doctorId, ConsultationQueue.ConsultationStatus.WAITING);
        
        if (next != null) {
            next.setConsultationStatus(ConsultationQueue.ConsultationStatus.IN_CONSULTATION);
            queueRepository.save(next);
        }
        
        return patientWasServed;
    }
    
    // =========================================================
    //  PART 4: STATISTICS & CALCULATIONS
    // =========================================================

    public int calculateWaitTime(Long doctorId, Long currentTokenId) {
        int peopleAhead = queueRepository.countByDoctorIdAndConsultationStatusAndIdLessThan(
                doctorId, ConsultationQueue.ConsultationStatus.WAITING, currentTokenId);
        
        boolean isDoctorBusy = queueRepository.existsByDoctorIdAndConsultationStatus(
                doctorId, ConsultationQueue.ConsultationStatus.IN_CONSULTATION);
        
        if(isDoctorBusy) {
            peopleAhead++; 
        }

        return peopleAhead * 10; 
    }

    public int getWaitingCount(Long doctorId) {
        return queueRepository.countByDoctorIdAndConsultationStatus(doctorId, ConsultationQueue.ConsultationStatus.WAITING);
    }

    // UPDATED: Now we just return the variable stored in the Doctor table
    public int getServedCount(Long doctorId) {
        Doctor doc = doctorRepository.findById(doctorId).orElse(null);
        return (doc != null) ? doc.getServedCount() : 0;
    }
    
    public ConsultationQueue getCurrentPatient(Long doctorId) {
        return queueRepository.findByDoctorIdAndConsultationStatus(doctorId, ConsultationQueue.ConsultationStatus.IN_CONSULTATION);
    }
}