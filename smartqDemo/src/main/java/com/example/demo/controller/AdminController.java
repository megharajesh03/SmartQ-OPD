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

    @GetMapping("/adminlogin")
    public String showAdminLoginPage() {
        return "adminlogin";
    }

    @PostMapping("/adminlogin")
    public String handleAdminLogin(@RequestParam String username, 
                                    @RequestParam String password, 
                                    RedirectAttributes redirectAttributes) {
        boolean isValid = adminService.validateAdmin(username, password);
        if (isValid) {
            return "redirect:/admin/adminhome";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            return "redirect:/admin/adminlogin";
        }
    }

    @GetMapping("/adminhome")
    public String showAdminHomePage() {
        return "adminhome";
    }
    
    @GetMapping("/doctorlist")
    public String viewAllDoctors(Model model) {
        Iterable<Doctor> doctors = adminService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "doctorlist";  
    }

    @GetMapping("/adddoctor")
    public String showAddDoctorPage() {
        return "adddoctor";
    }

    @PostMapping("/adddoctor")
    public String addDoctor(Doctor doctor) {
        adminService.addDoctor(doctor);
        return "redirect:/admin/doctorlist";
    }

    @GetMapping("/editdoctor/{doctorId}")
    public String showEditDoctorPage(@PathVariable Long doctorId, Model model) {
        Doctor doctor = adminService.getDoctorById(doctorId);
        model.addAttribute("doctor", doctor);
        return "editdoctor";
    }

    @PostMapping("/editdoctor/{doctorId}")
    public String editDoctor(@PathVariable Long doctorId, Doctor updatedDoctor) {
        adminService.editDoctor(doctorId, updatedDoctor);
        return "redirect:/admin/doctorlist";
    }

    @PostMapping("/deletedoctor/{doctorId}")
    public String deleteDoctor(@PathVariable Long doctorId) {
        adminService.deleteDoctor(doctorId);
        return "redirect:/admin/doctorlist";
    }
}