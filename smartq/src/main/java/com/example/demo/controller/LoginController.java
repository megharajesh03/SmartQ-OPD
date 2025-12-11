package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.bean.User;
import com.example.demo.service.UserService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // Welcome page where the user selects their role
    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome"; // will show welcome.html
    }

    // Admin login page
    @GetMapping("/adminlogin")
    public String adminLoginPage() {
        return "adminlogin";  // will show adminlogin.html
    }


    // Doctor login page
    @GetMapping("/doctorlogin")
    public String doctorLoginPage() {
        return "doctorlogin";  // will show doctorlogin.html
    }


    // User login page
    @GetMapping("/userlogin")
    public String userLoginPage() {
        return "userlogin";  // will show userlogin.html
    }

    // Handle User login
    @PostMapping("/userlogin")
    public String userLogin(@RequestParam String username,
                            @RequestParam String password,
                            RedirectAttributes redirectAttributes) {

        User user = userService.validateUser(username, password, "USER");

        // If login fails, show error message and stay on login page
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid Username or Password");
            return "redirect:/userlogin";  // Redirect back to user login page
        }

        // Successful login, redirect to user home page
        return "redirect:/userhome";
    }

    // Admin home page
    @GetMapping("/adminhome")
    public String adminHome() {
        return "adminhome";  // maps to /WEB-INF/views/admin/admin_home.html
    }

    // Doctor home page
    @GetMapping("/doctorhome")
    public String doctorHome() {
        return "doctorhome";  // maps to /WEB-INF/views/doctor/doctor_home.html
    }

    // User home page
    @GetMapping("/userhome")
    public String userHome() {
        return "userhome";  // maps to /WEB-INF/views/user/user_home.html
    }
}
