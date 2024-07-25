package com.example.tornet.controller;

import com.example.tornet.configuration.UserDetailsServiceImpl;
import com.example.tornet.exception.CustomerNotFoundException;

import com.example.tornet.model.Customer;
import com.example.tornet.model.Role;
import com.example.tornet.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;

@Controller
@Slf4j
@AllArgsConstructor
public class UserController {

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final CartService cartService;
    private final EmailSenderService emailSenderService;



    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletRequest request,
                        Model model) {
        if (email == null || email.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Please enter a valid email address");
            return "login";
        }

        try {
            Customer customer = customerService.findCustomerByEmail(email);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User authenticatedUser = (User) authentication.getPrincipal();

            if (isAdmin(authenticatedUser)) {
                System.out.println(isAdmin(authenticatedUser));
                return "redirect:/Admin";
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
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "An unexpected error occurred");
             return "login";
        }
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private boolean isAdmin(User user) {
        Collection<GrantedAuthority> roles = user.getAuthorities();

        for (GrantedAuthority auth : roles) {
            if ("ROLE_ADMIN".equals(auth.getAuthority()))
                return true;
        }

        return false;
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
        try {
            if (customerResult.hasErrors() || addressResult.hasErrors()) {
                log.error("Validation errors occurred during user registration.");
                return "registration";
            }

            // Check if a customer with the same email exists
            if (customerService.findCustomerByEmail(customer.getEmail()) != null) {
                log.error("Email already exists during user registration.");
                customerResult.rejectValue("email", null, "Email already exists");
                return "registration";
            }

            customer.setRole(Role.Users);
            customer.setCreateDateCustomer(new Date());
            if (customerService.save(customer)) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(customer.getEmail());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                return "redirect:/Main";
            } else {
                log.error("Failed to save the customer during registration.");
                return "registration";
            }
        } catch (Exception e) {
            log.error("An error occurred during registration", e);
            model.addAttribute("error", true);
            model.addAttribute("message", "An unexpected error occurred");
            return "registration";
        }
    }











}








