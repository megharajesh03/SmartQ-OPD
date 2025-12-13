package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.example.demo.bean.ConsultationQueue;
import com.example.demo.bean.Doctor;
import com.example.demo.service.DoctorService;
import com.example.demo.service.QueueService;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private QueueService queueService;

    // Show the Doctor Login page
    @GetMapping("/doctorlogin")
    public String showDoctorLoginPage() {
        return "doctorlogin";  
    }

    // Handle Doctor Login form submission
    @PostMapping("/doctorlogin")
    public String handleDoctorLogin(@RequestParam String username,
                                    @RequestParam String password,
                                    Model model) {

        Doctor doctor = doctorService.validateDoctor(username, password);

        if (doctor == null) {
            model.addAttribute("error", "Invalid Username or Password");
            return "doctorlogin"; 
        }

        return "redirect:/doctor/doctorhome?doctorId=" + doctor.getId();
    }

    // Doctor home page
    @GetMapping("/doctorhome")
    public String doctorHome(@RequestParam Long doctorId, Model model) {
        
        // 1. Get Doctor Details
        Doctor doctor = doctorService.getDoctorById(doctorId);
        model.addAttribute("doctor", doctor);

        // 2. Get Current Patient (if any)
        ConsultationQueue currentPatient = queueService.getCurrentPatient(doctorId);
        model.addAttribute("currentPatient", currentPatient != null ? currentPatient.getUser() : null);
        model.addAttribute("currentToken", currentPatient != null ? currentPatient.getTokenNumber() : "-");

        // 3. Get Waiting Count
        int waitingCount = queueService.getWaitingCount(doctorId);
        model.addAttribute("waitingCount", waitingCount);

        // ---------------------------------------------------------
        // 4. FIX: ADD SERVED COUNT HERE
        // ---------------------------------------------------------
        int servedCount = queueService.getServedCount(doctorId); // Fetch from service
        model.addAttribute("servedCount", servedCount);          // Add to Model
        // ---------------------------------------------------------

        return "doctorhome"; // This matches your HTML filename
    }

    // Mark doctor as available
    @PostMapping("/markAvailable")
    public String markAvailable(@RequestParam Long doctorId) {
        doctorService.setDoctorStatus(doctorId, Doctor.Status.AVAILABLE);
        return "redirect:/doctor/doctorhome?doctorId=" + doctorId;  
    }

    // Mark doctor as not available
    @PostMapping("/markNotAvailable")
    public String markNotAvailable(@RequestParam Long doctorId) {
        doctorService.setDoctorStatus(doctorId, Doctor.Status.NOT_AVAILABLE);
        return "redirect:/doctor/doctorhome?doctorId=" + doctorId;  
    }
    
    @GetMapping("/editprofile")
    public String showEditProfilePage(@RequestParam Long doctorId, Model model) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        
        if (doctor != null) {
            model.addAttribute("doctor", doctor);
            return "doctor_edit_profile"; 
        }
        
        return "redirect:/doctor/doctorlogin";
    }

    @PostMapping("/updateprofile")
    public String updateProfile(@ModelAttribute Doctor doctor) {
        doctorService.updateDoctor(doctor);
        return "redirect:/doctor/doctorhome?doctorId=" + doctor.getId();
    }
    
    // ACTION: Call Next Patient
    @PostMapping("/callNext")
    public String callNextPatient(@RequestParam Long doctorId) {
        
        // 1. Call the service and capture the result (True/False)
        boolean patientServed = queueService.callNextPatient(doctorId);
        
        // 2. Ensure Doctor status is AVAILABLE
        doctorService.setDoctorStatus(doctorId, Doctor.Status.AVAILABLE);
        
        // 3. ONLY increment the counter if a patient was actually marked as COMPLETED
        if (patientServed) {
            doctorService.incrementSessionServedCount(doctorId);
        }
        
        return "redirect:/doctor/doctorhome?doctorId=" + doctorId;
    }
}