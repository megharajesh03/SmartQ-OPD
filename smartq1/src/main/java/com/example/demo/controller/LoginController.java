package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.bean.User;
import com.example.demo.service.UserService;

import org.springframework.ui.Model;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // Welcome page where the user selects their role
    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome"; // will show welcome.jsp
    }

    // Admin login page
    @GetMapping("/adminlogin")
    public String adminLoginPage() {
        return "adminlogin";  // will show adminlogin.jsp
    }

    @PostMapping("/adminlogin")
    public String adminLogin(@RequestParam String username,
                             @RequestParam String password,
                             Model model) {

        User user = userService.validateUser(username, password, "ADMIN");
        if (user == null) {
            model.addAttribute("error", "Invalid Username or Password");
            return "adminlogin";
        }

        return "redirect:/adminhome";  // Redirect to admin home page
    }

    // Doctor login page
    @GetMapping("/doctorlogin")
    public String doctorLoginPage() {
        return "doctorlogin";  // will show doctorlogin.jsp
    }

    @PostMapping("/doctorlogin")
    public String doctorLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {

        User user = userService.validateUser(username, password, "DOCTOR");
        if (user == null) {
            model.addAttribute("error", "Invalid Username or Password");
            return "doctorlogin";
        }

        return "redirect:/doctorhome";  // Redirect to doctor home page
    }

    // User login page
    @GetMapping("/userlogin")
    public String userLoginPage() {
        return "userlogin";  // will show userlogin.jsp
    }

    @PostMapping("/userlogin")
    public String userLogin(@RequestParam String username,
                            @RequestParam String password,
                            Model model) {

        User user = userService.validateUser(username, password, "USER");
        if (user == null) {
            model.addAttribute("error", "Invalid Username or Password");
            return "userlogin";
        }

        return "redirect:/userhome";  // Redirect to user home page
    }

    // Admin home page
    @GetMapping("/adminhome")
    public String adminHome() {
        return "adminhome";  // maps to /WEB-INF/views/admin/admin_home.jsp
    }

    // Doctor home page
    @GetMapping("/doctorhome")
    public String doctorHome() {
        return "doctorhome";  // maps to /WEB-INF/views/doctor/doctor_home.jsp
    }

    // User home page
    @GetMapping("/userhome")
    public String userHome() {
        return "userhome";  // maps to /WEB-INF/views/user/user_home.jsp
    }
}
