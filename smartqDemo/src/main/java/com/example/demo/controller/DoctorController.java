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
    private QueueService queueService; // Injected QueueService

    @GetMapping("/doctorlogin")
    public String showDoctorLoginPage() {
        return "doctorlogin";  
    }

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

    @GetMapping("/doctorhome")
    public String doctorHome(@RequestParam Long doctorId, Model model) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        model.addAttribute("doctor", doctor);
        return "doctorhome";  
    }

    // ACTION: Call Next Patient
    @PostMapping("/callNext")
    public String callNextPatient(@RequestParam Long doctorId) {
        // 1. QueueService moves the line (Marks current DONE, next IN_CONSULTATION)
        queueService.callNextPatient(doctorId);
        
        // 2. Ensure Doctor status is AVAILABLE
        doctorService.setDoctorStatus(doctorId, Doctor.Status.AVAILABLE);
        
        return "redirect:/doctor/doctorhome?doctorId=" + doctorId;
    }

    @PostMapping("/markNotAvailable")
    public String markNotAvailable(@RequestParam Long doctorId) {
        doctorService.setDoctorStatus(doctorId, Doctor.Status.NOT_AVAILABLE);
        return "redirect:/doctor/doctorhome?doctorId=" + doctorId;  
    }
    @PostMapping("/markAvailable")
    public String markAvailable(@RequestParam Long doctorId) {
        doctorService.setDoctorStatus(doctorId, Doctor.Status.AVAILABLE);
        return "redirect:/doctor/doctorhome?doctorId=" + doctorId;  
    }
    
    
}