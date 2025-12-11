package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.example.demo.bean.ConsultationQueue;
import com.example.demo.bean.User;
import com.example.demo.service.AdminService;
import com.example.demo.service.QueueService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/user") 
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AdminService adminService; // To get the list of doctors
    
    @Autowired
    private QueueService queueService; // To handle queue actions

    // ==========================================
    // 1. LOGIN SECTION
    // ==========================================

    // Show User Login page
    @GetMapping("/userlogin")
    public String showUserLoginPage() {
        return "userlogin"; 
    }

    // Handle Login
    @PostMapping("/userlogin")
    public String handleUserLogin(@RequestParam String username,
                                  @RequestParam String password,
                                  Model model) {
        
        // Validate user (Ensure your UserService.validateUser matches this signature)
        User user = userService.validateUser(username, password,"USER"); 

        if (user == null) {
            model.addAttribute("error", "Invalid Username or Password");
            return "userlogin";
        }
        
        // Redirect to userhome with userId
        return "redirect:/user/userhome?userId=" + user.getId();
    }

    // ==========================================
    // 2. DASHBOARD SECTION (Select Doctor)
    // ==========================================

    @GetMapping("/userhome")
    public String userHome(@RequestParam Long userId, Model model) {
        model.addAttribute("userId", userId);
        
        // Pass the list of doctors so the user can choose one
        model.addAttribute("doctors", adminService.getAllDoctors());
        
        return "userhome"; // Maps to userhome.html
    }
    
    // ==========================================
    // 3. QUEUE LOGIC (Join & Status)
    // ==========================================

    // ACTION: Join Queue (Called when user clicks "Get Token")
    @PostMapping("/joinQueue")
    public String joinQueue(@RequestParam Long userId, @RequestParam Long doctorId) {
        queueService.joinQueue(userId, doctorId);
        
        // Redirect to the Token page to see status
        return "redirect:/user/token?userId=" + userId;
    }
    
    // PAGE: View Token (The live waiting screen)
    @GetMapping("/token")
    public String showToken(@RequestParam Long userId, Model model) {
        // Fetch the user's latest token
        ConsultationQueue token = queueService.getUserToken(userId);
        
        if (token != null) {
            model.addAttribute("token", token);
            model.addAttribute("doctor", token.getDoctor());
            
            // Logic for status message display
            String statusMsg = "Waiting...";
            if(token.getConsultationStatus() == ConsultationQueue.ConsultationStatus.IN_CONSULTATION) {
                statusMsg = "It is your turn! Please enter.";
            } else if (token.getDoctor().getStatus() == com.example.demo.bean.Doctor.Status.AVAILABLE) {
                 statusMsg = "Doctor is Ready. Waiting for queue to move.";
            } else {
                 statusMsg = "Doctor is currently busy/unavailable.";
            }
            model.addAttribute("statusMsg", statusMsg);
        } else {
            model.addAttribute("statusMsg", "You have no active appointments.");
        }
        
        model.addAttribute("userId", userId);
        return "usertoken"; // Maps to usertoken.html
    }
}