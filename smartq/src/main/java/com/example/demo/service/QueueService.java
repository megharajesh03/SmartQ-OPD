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

    // --- PART 1: RESET LOGIC ---

    @Transactional
    public void resetDailyQueue() {
        System.out.println("⚠️ Action: Resetting System for a new day...");
        queueRepository.deleteAll();
        
        List<Doctor> allDoctors = doctorRepository.findAll();
        for (Doctor doc : allDoctors) {
            doc.setServedCount(0);
        }
        doctorRepository.saveAll(allDoctors);
        System.out.println("✅ Queue cleared & Doctor stats reset to 0.");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanQueueAtMidnight() {
        resetDailyQueue();
    }

    // --- PART 2: QUEUE OPERATIONS ---

    public ConsultationQueue joinQueue(Long userId, Long doctorId) {
        User user = userRepository.findById(userId).orElse(null);
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

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

    // --- PART 3: DOCTOR ACTIONS ---

    public boolean callNextPatient(Long doctorId) {
        boolean patientWasServed = false;

        // 1. Mark current patient as COMPLETED
        ConsultationQueue current = queueRepository.findByDoctorIdAndConsultationStatus(
                doctorId, ConsultationQueue.ConsultationStatus.IN_CONSULTATION);
        
        if (current != null) {
            current.setConsultationStatus(ConsultationQueue.ConsultationStatus.COMPLETED);
            queueRepository.save(current);
            patientWasServed = true; 
            
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
    
    // --- PART 4: STATISTICS & CALCULATIONS (UPDATED FOR YOUR LOGIC) ---

    public int calculateWaitTime(Long doctorId, Long currentTokenId) {
        // 1. Count ONLY the people who are currently 'WAITING' and joined before me (Id < MyId)
        int peopleAhead = queueRepository.countByDoctorIdAndConsultationStatusAndIdLessThan(
                doctorId, ConsultationQueue.ConsultationStatus.WAITING, currentTokenId);
        
        // 2. Calculate Time: Just multiply people ahead by 10.
        // If I am the 1st person waiting -> peopleAhead is 0 -> Time is 0 mins.
        // If I am the 2nd person waiting -> peopleAhead is 1 -> Time is 10 mins.
        return peopleAhead * 10; 
    }

    public int getWaitingCount(Long doctorId) {
        return queueRepository.countByDoctorIdAndConsultationStatus(doctorId, ConsultationQueue.ConsultationStatus.WAITING);
    }

    public int getServedCount(Long doctorId) {
        Doctor doc = doctorRepository.findById(doctorId).orElse(null);
        return (doc != null) ? doc.getServedCount() : 0;
    }
    
    public ConsultationQueue getCurrentPatient(Long doctorId) {
        return queueRepository.findByDoctorIdAndConsultationStatus(doctorId, ConsultationQueue.ConsultationStatus.IN_CONSULTATION);
    }
}