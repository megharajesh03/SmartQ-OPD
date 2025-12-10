package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.example.demo.bean.User;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Show the User Login page
    @GetMapping("/userlogin")
    public String showUserLoginPage() {
        return "userlogin";  // Shows userlogin.jsp page
    }

    // Handle User Login form submission
    @PostMapping("/userlogin")
    public String handleUserLogin(@RequestParam String username,
                                  @RequestParam String password,
                                  Model model) {

        // Validate user credentials for user role
        User user = userService.validateUser(username, password, "USER");

        if (user == null) {
            // If credentials are incorrect, show an error message
            model.addAttribute("error", "Invalid Username or Password");
            return "userlogin";  // Return to the login page if validation fails
        }

        // If login is successful, redirect to user home page
        return "redirect:/userhome";  // Redirect to /user/userhome
    }

    // User home page
    @GetMapping("/userhome")
    public String userHome() {
        return "userhome";  // Return the user home view
    }
}
