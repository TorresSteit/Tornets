package com.example.tornet.controller;

import com.example.tornet.configuration.UserDetailsServiceImpl;
import com.example.tornet.exception.CustomerNotFoundException;
import com.example.tornet.model.Cart;
import com.example.tornet.model.Customer;
import com.example.tornet.model.Role;
import com.example.tornet.service.CartService;
import com.example.tornet.service.CustomerService;
import com.example.tornet.service.EmailSenderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;

@Controller
@Slf4j
@AllArgsConstructor
public class UserController {

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final CartService cartService;
    private  final EmailSenderService emailSenderService;

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
                               Model model) {

        // Check for validation errors
        if (customerResult.hasErrors()) {
            log.error("Validation errors occurred during user registration.");
            return "registration";
        }

        // Check if a customer with the same email exists
        try {
            customerService.findCustomerByEmail(customer.getEmail());
            log.error("Email already exists during user registration.");
            customerResult.rejectValue("email", null, "Email already exists");
            return "registration";
        } catch (CustomerNotFoundException e) {
            // Email is not found, so it's safe to proceed with registration
        }

        customer.setRole(Role.Users); // Set the user's role
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
        // Check for empty email
        if (email == null || email.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Please enter a valid email address");
            return "login";
        }

        try {
            // Retrieve the customer by email
            Customer customer = customerService.findCustomerByEmail(email);

            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Send email notification
            emailSenderService.sendEmail(email, "Login Notification", "You have successfully logged in.");
            log.error(email + " has been logged in.");

            // Redirect based on role
            if (customer.getRole() == Role.Admin) {
                return "redirect:/AdminPanel";
            } else {
                return "redirect:/Main";
            }
        } catch (CustomerNotFoundException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Email address not found");
            return "login";
        } catch (AuthenticationException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Invalid email or password");
            return "login";
        }
    }
    private boolean isAdmin(User user) {
        Collection<GrantedAuthority> roles = user.getAuthorities();

        for (GrantedAuthority auth : roles) {
            if ("ADMIN".equals(auth.getAuthority()))
                return true;
        }

        return false;
    }

}







