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
        return "doctorlogin";  // Shows doctorlogin.jsp page
    }

    // Handle Doctor Login form submission
    @PostMapping("/doctorlogin")
    public String handleDoctorLogin(@RequestParam String username,
                                    @RequestParam String password,
                                    Model model) {

        // Validate doctor credentials for doctor role (you can adjust your method to check roles)
        Doctor doctor = doctorService.validateDoctor(username, password);

        if (doctor == null) {
            // If credentials are incorrect, show an error message
            model.addAttribute("error", "Invalid Username or Password");
            return "doctorlogin";  // Return to the login page if validation fails
        }

        // If login is successful, redirect to doctor home page
        return "redirect:/doctor/doctorhome";  // Redirect to doctor home page
    }

    // Doctor home page
    @GetMapping("/doctorhome")
    public String doctorHome(@RequestParam Long doctorId, Model model) {
        // Get the doctor by ID to show their current status and details
        Doctor doctor = doctorService.getDoctorById(doctorId);
        model.addAttribute("doctor", doctor);
        return "doctorhome";  // maps to /WEB-INF/views/doctor/doctor_home.jsp
    }

    // Mark doctor as available
    @PostMapping("/markAvailable")
    public String markAvailable(@RequestParam Long doctorId) {
        doctorService.setDoctorStatus(doctorId, Doctor.Status.AVAILABLE);
        return "redirect:/doctor/doctorhome?doctorId=" + doctorId;  // Redirect back to doctor home page
    }

    // Mark doctor as not available
    @PostMapping("/markNotAvailable")
    public String markNotAvailable(@RequestParam Long doctorId) {
        doctorService.setDoctorStatus(doctorId, Doctor.Status.NOT_AVAILABLE);
        return "redirect:/doctor/doctorhome?doctorId=" + doctorId;  // Redirect back to doctor home page
    }
}
