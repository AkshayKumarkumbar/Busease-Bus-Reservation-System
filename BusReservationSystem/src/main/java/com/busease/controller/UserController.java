package com.busease.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;

import com.busease.model.User;
import com.busease.service.UserService;

/**
 * Controller for user-related operations including registration, login, and guest booking.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Show a simplified user info form that creates a guest session.
     */
    @GetMapping("/guest-booking")
    public String showGuestForm() {
        return "guest-booking";
    }

    /**
     * Create a simple guest session without full registration.
     */
    @PostMapping("/guest-booking")
    public String createGuestSession(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            // Store basic user info in session for later use in booking
            session.setAttribute("guestName", name);
            session.setAttribute("guestEmail", email);
            session.setAttribute("guestPhone", phone);
            
            // Generate a simple guest ID
            session.setAttribute("guestId", UUID.randomUUID().toString());
            
            // Add success message
            redirectAttributes.addFlashAttribute("message", "Welcome, " + name + "! You can now browse and book tickets.");
            
            // Redirect to search page
            return "redirect:/search";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating guest session: " + e.getMessage());
            return "redirect:/guest-booking";
        }
    }
    
    /**
     * Show login page
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("pageTitle", "Login - Busease");
        return "login";
    }
    
    /**
     * Process simple login (no Spring Security)
     */
    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            User user = userService.findByUsername(username);
            
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Invalid username or password");
                return "redirect:/login?error";
            }
            
            // Compare passwords directly (simplified approach)
            if (!userService.checkPassword(password, user.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Invalid username or password");
                return "redirect:/login?error";
            }
            
            // Store user info in session
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("authenticated", true);
            
            // Redirect based on role
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/search";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:/login?error";
        }
    }
    
    /**
     * Process logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully");
        return "redirect:/login?logout";
    }
    
    /**
     * Show registration page
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "register";
    }
    
    /**
     * Process user registration
     */
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Validate password match
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match");
                return "redirect:/register?error";
            }
            
            // Check if username or email already exists
            if (userService.isUsernameExists(username)) {
                redirectAttributes.addFlashAttribute("error", "Username already exists");
                return "redirect:/register?error";
            }
            
            if (userService.isEmailExists(email)) {
                redirectAttributes.addFlashAttribute("error", "Email already exists");
                return "redirect:/register?error";
            }
            
            // Create and save the new user
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password); // Will be encoded in service
            user.setRole("USER");
            
            userService.registerUser(user);
            
            // Redirect to login with success message
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please login to continue.");
            return "redirect:/login?success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/register?error";
        }
    }
}