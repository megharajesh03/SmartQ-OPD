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
    private AdminService adminService;
    
    @Autowired
    private QueueService queueService;
    
    

    // Show the User Login page
    @GetMapping("/userlogin")
    public String showUserLoginPage() {
        return "userlogin";  // Shows userlogin.jsp page
    }

    // ==========================================
    // 1. MODIFIED LOGIN LOGIC
    // ==========================================
    @PostMapping("/userlogin")
    public String handleUserLogin(@RequestParam String username,
                                  @RequestParam String password,
                                  Model model) {

        User user = userService.validateUser(username, password, "USER");

        if (user == null) {
            model.addAttribute("error", "Invalid Username or Password");
            return "userlogin";
        }

        // --- NEW LOGIC STARTS HERE ---
        
        // Check the status of the user's insurance
        String insuranceStatus = userService.checkInsuranceStatus(user.getId());

        // Logic: If Valid -> Home. If Missing/Expired -> Insurance Page
        if ("VALID".equals(insuranceStatus)) {
            return "redirect:/user/userhome?userId=" + user.getId();
        } else {
            // Redirect to insurance page with a status flag (e.g., to show "Expired" alert)
            return "redirect:/user/insurance?userId=" + user.getId() + "&status=" + insuranceStatus;
        }
        
        // --- NEW LOGIC ENDS HERE ---
    }

    // ==========================================
    // 2. NEW INSURANCE PAGE HANDLERS
    // ==========================================

    // SHOW the Insurance Page
    @GetMapping("/insurance")
    public String showInsurancePage(@RequestParam Long userId, 
                                    @RequestParam(required = false) String status, 
                                    Model model) {
        User user = userService.getUserById(userId);
        
        if (user != null) {
            model.addAttribute("user", user); // This contains user.insurance (if exists)
            model.addAttribute("status", status); // "MISSING" or "EXPIRED"
            return "user_insurance"; // You need to create user_insurance.jsp/html
        }
        return "redirect:/user/userlogin";
    }

    // SAVE/UPDATE the Insurance Details
    @PostMapping("/saveInsurance")
    public String saveInsurance(@ModelAttribute User user) {
        // We reuse the updateUser method we wrote in UserService
        // It handles both creating new insurance or updating expired insurance
        userService.updateUser(user);
        
        // After saving, go to dashboard
        return "redirect:/user/userhome?userId=" + user.getId();
    }
    
    // NOTE: The "Skip" logic is handled in the HTML via a simple link:
    // <a href="/user/userhome?userId=${user.id}">Skip for now</a>

    // ==========================================
    // 3. DASHBOARD SECTION (Select Doctor)
    // ==========================================

    @GetMapping("/userhome")
    public String userHome(@RequestParam Long userId, Model model) {
        model.addAttribute("userId", userId);
        
        User user = userService.getUserById(userId);
        
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }
        
        model.addAttribute("doctors", adminService.getAllDoctors());
        
        return "userhome"; 
    }
    
    // ==========================================
    // 4. QUEUE LOGIC (Join & Status)
    // ==========================================

    // ACTION: Join Queue (Called when user clicks "Get Token")
    @PostMapping("/joinQueue")
    public String joinQueue(@RequestParam Long userId, @RequestParam Long doctorId) {
        queueService.joinQueue(userId, doctorId);
        return "redirect:/user/token?userId=" + userId;
    }
    
    // PAGE: View Token (The live waiting screen)
    @GetMapping("/token")
    public String showToken(@RequestParam Long userId, Model model) {
        ConsultationQueue token = queueService.getUserToken(userId);
        
        if (token == null) {
            token = queueService.getLastToken(userId);
        }
        
        if (token != null) {
            model.addAttribute("token", token);
            model.addAttribute("doctor", token.getDoctor());
            
            String statusMsg = "";
            int estTime = 0;

            if(token.getConsultationStatus() == ConsultationQueue.ConsultationStatus.COMPLETED) {
                statusMsg = "Consultation Done. Please proceed to billing.";
            } 
            else if(token.getConsultationStatus() == ConsultationQueue.ConsultationStatus.IN_CONSULTATION) {
                statusMsg = "It is your turn! Please enter the cabin.";
            } 
            else {
                 estTime = queueService.calculateWaitTime(token.getDoctor().getId(), token.getId());
                 statusMsg = "You are in the queue.";
            }
            
            model.addAttribute("statusMsg", statusMsg);
            model.addAttribute("estTime", estTime);
            
        } else {
            model.addAttribute("statusMsg", "You have no appointments.");
        }
        
        model.addAttribute("userId", userId);
        return "usertoken"; 
    }
    
    // ==========================================
    // 5. PROFILE EDITING
    // ==========================================
    
    @GetMapping("/editprofile")
    public String showEditProfilePage(@RequestParam Long userId, Model model) {
        User user = userService.getUserById(userId);
        if (user != null) {
            model.addAttribute("user", user);
            return "user_edit_profile"; 
        }
        return "redirect:/user/userlogin"; 
    }

    @PostMapping("/updateprofile")
    public String updateProfile(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/user/userhome?userId=" + user.getId();
    }
    
}