package com.example.tornet.controller;

import com.example.tornet.configuration.UserDetailsServiceImpl;
import com.example.tornet.model.Customer;
import com.example.tornet.model.Role;
import com.example.tornet.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@Slf4j
@AllArgsConstructor
public class UserController {

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("registration", "/registration");
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("customer") Customer customer, BindingResult customerResult,
                               BindingResult addressResult, Model model) {

        if (customerResult.hasErrors() || addressResult.hasErrors()) {
            log.error("Validation errors occurred during user registration.");
            return "registration";
        }

        // Check if a customer with the same email exists
        if (customerService.findCustomerByEmail(customer.getEmail()).isPresent()) {
            log.error("Email already exists during user registration.");
            customerResult.rejectValue("email", null, "Email already exists");
            return "registration";
        }

        customer.setRole(Role.Users); // Устанавливаем роль пользователя
        if (customerService.save(customer)) {
            // Authenticate the user
            UserDetails userDetails = userDetailsService.loadUserByUsername(customer.getEmail());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/Main";
        } else {
            return "registration";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpServletRequest request, Model model) {
        // Check for empty username
        if (email == null || email.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Please enter a valid email address");
            return "login";
        }

        log.debug("Attempting login with username: {}", email);

        Optional<Customer> customer = customerService.findCustomerByEmail(email);
        if (customer.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Email address not found");
            return "login";
        }

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            boolean isAdmin = customer.get().getRole() == Role.Admin;
            if (isAdmin) {
                return "redirect:/AdminPanel";
            } else {
                return "redirect:/Main";
            }
        } catch (AuthenticationException e) {
            model.addAttribute("error", true);
            log.error("Authentication error during user login: {}", e.getMessage());
            return "login";
        }
    }

}






