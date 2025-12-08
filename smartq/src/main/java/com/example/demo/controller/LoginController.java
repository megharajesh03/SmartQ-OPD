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
    UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          Model model) {

        User user = userService.validateUser(username, password);

        if (user == null) {
            model.addAttribute("error", "Invalid Username or Password");
            return "login";
        }

        // Redirect based on ROLE
        switch (user.getAccountType().toUpperCase()) {
            case "ADMIN":
                return "redirect:/admin/home";
            case "DOCTOR":
                return "redirect:/doctor/home";
            case "USER":
                return "redirect:/user/home";
            default:
                model.addAttribute("error", "Unknown role assigned!");
                return "login";
        }
    }
}
