package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import com.example.demo.bean.Doctor;
import com.example.demo.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Show the Admin Login page
    @GetMapping("/adminlogin")
    public String showAdminLoginPage() {
        return "adminlogin";  // Returns the admin login page (adminlogin.html)
    }

    // Handle Admin Login form submission
    @PostMapping("/adminlogin")
    public String handleAdminLogin(@RequestParam String username, 
                                    @RequestParam String password, 
                                    RedirectAttributes redirectAttributes) {
        // Validate the admin credentials
        boolean isValid = adminService.validateAdmin(username, password);

        if (isValid) {
            // If valid, redirect to the Admin Home page
            return "redirect:/admin/adminhome";
        } else {
            // If invalid, add an error message and stay on the login page
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            return "redirect:/admin/adminlogin";  // Redirect back to login page with error
        }
    }

    // Admin home page
    @GetMapping("/adminhome")
    public String showAdminHomePage() {
        return "adminhome";  // Return the admin home page (adminhome.html)
    }
    
    // View all doctors
    @GetMapping("/doctorlist")
    public String viewAllDoctors(Model model) {
        Iterable<Doctor> doctors = adminService.getAllDoctors();
   
        // Add the doctors list to the model
        model.addAttribute("doctors", doctors);

        return "doctorlist";  
    }


    // Show add doctor page
    @GetMapping("/adddoctor")
    public String showAddDoctorPage() {
        return "adddoctor";  // maps to /WEB-INF/views/admin/adddoctor.jsp
    }

    // Handle add doctor form submission
    @PostMapping("/adddoctor")
    public String addDoctor(Doctor doctor) {
        adminService.addDoctor(doctor);
        System.out.println("created");
        return "redirect:/admin/doctorlist";  // Redirect to doctor list after adding a new doctor
    }

    // Show edit doctor page
    @GetMapping("/editdoctor/{doctorId}")
    public String showEditDoctorPage(@PathVariable Long doctorId, Model model) {
        Doctor doctor = adminService.getDoctorById(doctorId);
        if (doctor != null) {
            model.addAttribute("doctor", doctor);
            return "editdoctor";  // maps to /WEB-INF/views/admin/editdoctor.jsp
        }
        return "redirect:/admin/doctorlist";  // Redirect to doctor list if doctor not found
    }

    // Handle edit doctor form submission
    @PostMapping("/editdoctor/{doctorId}")
    public String editDoctor(@PathVariable Long doctorId, Doctor updatedDoctor) {
        Doctor doctor = adminService.editDoctor(doctorId, updatedDoctor);
        if (doctor != null) {
            return "redirect:/admin/doctorlist";  // Redirect to doctor list after successful update
        }
        return "redirect:/admin/doctorlist";  // Redirect to doctor list if update fails
    }

    // Delete doctor
    @PostMapping("/deletedoctor/{doctorId}")
    public String deleteDoctor(@PathVariable Long doctorId) {
        boolean isDeleted = adminService.deleteDoctor(doctorId);
        return "redirect:/admin/doctorlist";  // Redirect to doctor list after deletion attempt
    }
}
