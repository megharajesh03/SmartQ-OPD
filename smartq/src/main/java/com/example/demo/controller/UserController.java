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

    @PostMapping("/userlogin")
    public String handleUserLogin(@RequestParam String username,
                                  @RequestParam String password,
                                  Model model) {

        User user = userService.validateUser(username, password, "USER");

        if (user == null) {
            model.addAttribute("error", "Invalid Username or Password");
            return "userlogin";
        }

        // --- FIX STARTS HERE ---
        
        // 1. We must redirect to /user/userhome (because of the class mapping)
        // 2. We must append the userId so the next method can catch it
        return "redirect:/user/userhome?userId=" + user.getId();
        
        // --- FIX ENDS HERE ---
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
        // 1. Try to get active token first
        ConsultationQueue token = queueService.getUserToken(userId);
        
        // 2. If no active token, check if they just finished (for the Billing screen)
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
                 // Calculate Time
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
}
