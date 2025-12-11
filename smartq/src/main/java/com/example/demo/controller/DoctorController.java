package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.example.demo.bean.Doctor;
import com.example.demo.service.DoctorService;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

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

        // Validate doctor credentials for doctor role
        Doctor doctor = doctorService.validateDoctor(username, password);

        if (doctor == null) {
            // If credentials are incorrect, show an error message
            model.addAttribute("error", "Invalid Username or Password");
            return "doctorlogin";  // Return to the login page if validation fails
        }

        // If login is successful, redirect to doctor home page
        // Include doctorId in the redirect URL
        return "redirect:/doctor/doctorhome?doctorId=" + doctor.getId();
    }

    // Doctor home page
    @GetMapping("/doctorhome")
    public String doctorHome(@RequestParam Long doctorId, Model model) {
        // Get the doctor by ID to show their current status and details
        Doctor doctor = doctorService.getDoctorById(doctorId);
        
        // Add the doctor object to the model
        model.addAttribute("doctor", doctor);
        return "doctorhome";  
    }

    // Mark doctor as available
    @PostMapping("/markAvailable")
    public String markAvailable(@RequestParam Long doctorId) {
        doctorService.setDoctorStatus(doctorId, Doctor.Status.AVAILABLE);
        // Redirect back to doctor home page with doctorId as a query parameter
        return "redirect:/doctor/doctorhome?doctorId=" + doctorId;  
    }

    // Mark doctor as not available
    @PostMapping("/markNotAvailable")
    public String markNotAvailable(@RequestParam Long doctorId) {
        doctorService.setDoctorStatus(doctorId, Doctor.Status.NOT_AVAILABLE);
        // Redirect back to doctor home page with doctorId as a query parameter
        return "redirect:/doctor/doctorhome?doctorId=" + doctorId;  
    }
}
