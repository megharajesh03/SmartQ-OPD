package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.bean.Doctor;
import com.example.demo.bean.User;
import com.example.demo.bean.Insurance; // Added Import
import com.example.demo.service.AdminService;
import com.example.demo.service.QueueService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private QueueService queueService;

    // Show the Admin Login page
    @GetMapping("/adminlogin")
    public String showAdminLoginPage() {
        return "adminlogin"; 
    }

    // Handle Admin Login form submission
    @PostMapping("/adminlogin")
    public String handleAdminLogin(@RequestParam String username, @RequestParam String password,
            RedirectAttributes redirectAttributes) {
        boolean isValid = adminService.validateAdmin(username, password);

        if (isValid) {
            return "redirect:/admin/adminhome";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            return "redirect:/admin/adminlogin"; 
        }
    }

    // Admin home page
    @GetMapping("/adminhome")
    public String showAdminHomePage() {
        return "adminhome"; 
    }

    // View all doctors
    @GetMapping("/doctorlist")
    public String viewAllDoctors(Model model) {
        Iterable<Doctor> doctors = adminService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "doctorlist";
    }

    // Show add doctor page
    @GetMapping("/adddoctor")
    public String showAddDoctorPage() {
        return "adddoctor"; 
    }

    // Handle add doctor form submission
    @PostMapping("/adddoctor")
    public String addDoctor(Doctor doctor) {
        adminService.addDoctor(doctor);
        System.out.println("created");
        return "redirect:/admin/doctorlist"; 
    }

    // Show edit doctor page
    @GetMapping("/editdoctor/{doctorId}")
    public String showEditDoctorPage(@PathVariable Long doctorId, Model model) {
        Doctor doctor = adminService.getDoctorById(doctorId);
        if (doctor != null) {
            model.addAttribute("doctor", doctor);
            return "editdoctor"; 
        }
        return "redirect:/admin/doctorlist"; 
    }

    // Handle edit doctor form submission
    @PostMapping("/editdoctor/{doctorId}")
    public String editDoctor(@PathVariable Long doctorId, Doctor updatedDoctor) {
        Doctor doctor = adminService.editDoctor(doctorId, updatedDoctor);
        if (doctor != null) {
            return "redirect:/admin/doctorlist"; 
        }
        return "redirect:/admin/doctorlist"; 
    }

    // Delete doctor
    @PostMapping("/deletedoctor/{doctorId}")
    public String deleteDoctor(@PathVariable Long doctorId) {
        adminService.deleteDoctor(doctorId);
        return "redirect:/admin/doctorlist"; 
    }

    // 1. View all users
    @GetMapping("/userlist")
    public String viewAllUsers(Model model) {
        Iterable<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "userlist"; 
    }

    // 2. Show add user page
    @GetMapping("/adduser")
    public String showAddUserPage(Model model) {
        model.addAttribute("user", new User()); 
        return "adduser"; 
    }

    // 3. Handle add user form submission
 // 3. Handle add user form submission
    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User user) {
        
        // Default role if missing
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        // --- PREVENT DB CRASH ---
        // If user typed an insurance number, but we don't have provider details,
        // we must remove the incomplete insurance object before saving.
        if (user.getInsurance() != null) {
            String num = user.getInsurance().getInsuranceNumber();
            if (num == null || num.trim().isEmpty()) {
                // Box was empty -> Remove the empty object
                user.setInsurance(null);
            } else {
                // Box had data -> BUT we are missing providerName (required by DB).
                // Option A: Hardcode a default provider to allow saving
                user.getInsurance().setProviderName("Pending Provider Details"); 
                user.getInsurance().setStatus(true);
                user.getInsurance().setUser(user); // Link relationship
                
                // Option B: Just delete it and warn (uncomment to use)
                // System.out.println("Skipping insurance - missing provider details");
                // user.setInsurance(null);
            }
        }

        adminService.addUser(user);
        return "redirect:/admin/userlist";
    }

    // 4. Show edit user page
    @GetMapping("/edituser/{userId}")
    public String showEditUserPage(@PathVariable Long userId, Model model) {
        User user = adminService.getUserById(userId);
        if (user != null) {
            model.addAttribute("user", user);
            return "edituser"; 
        }
        return "redirect:/admin/userlist";
    }

    // 5. Handle edit user form submission (UPDATED TO FIX 500 ERROR)
    @PostMapping("/edituser/{userId}")
    public String editUser(@PathVariable Long userId, @ModelAttribute User updatedUser) {
        
        // A. Fetch existing user from DB
        User existingUser = adminService.getUserById(userId);
        if (existingUser == null) {
            return "redirect:/admin/userlist";
        }

        // B. Update standard fields
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setAddress(updatedUser.getAddress());

        // Update password only if changed
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        // C. SAFE INSURANCE UPDATE
        // Check if the form sent any insurance data
        Insurance formInsurance = updatedUser.getInsurance();
        
        if (formInsurance != null && formInsurance.getInsuranceNumber() != null && !formInsurance.getInsuranceNumber().trim().isEmpty()) {
            // Case 1: User typed an Insurance Number
            if (existingUser.getInsurance() != null) {
                // If user ALREADY has insurance, update only the number.
                // This keeps 'providerName' (which is in DB) safe from being overwritten by null.
                existingUser.getInsurance().setInsuranceNumber(formInsurance.getInsuranceNumber());
            } else {
                // Case 2: User trying to ADD new insurance via this simple form.
                // We CANNOT save this because 'providerName' is mandatory in DB but missing in Form.
                // We skip this update to prevent the crash. 
                // (To fix this properly, you would need to add providerName input to your HTML).
                System.out.println("Warning: Skipping new insurance creation - missing providerName.");
            }
        } 
        // Case 3: If form box is empty, we do nothing. (Existing insurance stays untouched).

        // D. Save the existing (modified) user
        adminService.addUser(existingUser); 

        return "redirect:/admin/userlist";
    }

    // 6. Delete user
    @PostMapping("/deleteuser/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return "redirect:/admin/userlist";
    }

    // Handle Manual Reset
    @PostMapping("/resetSystem")
    public String resetSystemForNewDay(RedirectAttributes redirectAttributes) {
        queueService.resetDailyQueue();
        redirectAttributes.addFlashAttribute("message", "✅ System Reset Successful! A new day has started.");
        return "redirect:/admin/adminhome";
    }
}