package com.example.demo.service;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.ConsultationQueue;
import com.example.demo.bean.ConsultationQueue.ConsultationStatus;
import com.example.demo.bean.Doctor;
import com.example.demo.bean.User;
import com.example.demo.dao.ConsultationQueueRepository;
import com.example.demo.dao.DoctorRepository;
import com.example.demo.dao.UserRepository;

@Service
public class QueueService {

    @Autowired
    private ConsultationQueueRepository queueRepo;
    
    @Autowired
    private DoctorRepository doctorRepo;
    
    @Autowired
    private UserRepository userRepo;

    // 1. Patient joins the queue
    public ConsultationQueue joinQueue(Long userId, Long doctorId) {
        User user = userRepo.findById(userId).orElse(null);
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        
        // Only allow joining if user and doctor exist
        if(user != null && doctor != null) {
            ConsultationQueue queue = new ConsultationQueue();
            queue.setUser(user);
            queue.setDoctor(doctor);
            queue.setConsultationStatus(ConsultationStatus.PENDING); // Start as Pending
            queue.setScheduledTime(new Timestamp(System.currentTimeMillis()));
            return queueRepo.save(queue);
        }
        return null;
    }

    // 2. Doctor calls the next patient
    public ConsultationQueue callNextPatient(Long doctorId) {
        // Step A: Find the patient currently inside (if any) and mark them as DONE
        Optional<ConsultationQueue> current = queueRepo.findByDoctorIdAndConsultationStatus(doctorId, ConsultationStatus.IN_CONSULTATION);
        if (current.isPresent()) {
            ConsultationQueue donePatient = current.get();
            donePatient.setConsultationStatus(ConsultationStatus.DONE);
            queueRepo.save(donePatient);
        }

        // Step B: Find the next pending patient in line
        List<ConsultationQueue> pendingList = queueRepo.findByDoctorIdAndConsultationStatusOrderByScheduledTimeAsc(doctorId, ConsultationStatus.PENDING);
        
        if (!pendingList.isEmpty()) {
            ConsultationQueue nextPatient = pendingList.get(0);
            nextPatient.setConsultationStatus(ConsultationStatus.IN_CONSULTATION); // Mark as Inside
            return queueRepo.save(nextPatient);
        }
        
        return null; // Queue is empty
    }
    
    // 3. Get User's Current Token
    public ConsultationQueue getUserToken(Long userId) {
        return queueRepo.findTopByUserIdOrderByScheduledTimeDesc(userId).orElse(null);
    }
}
