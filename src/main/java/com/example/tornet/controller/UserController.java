package com.example.tornet.controller;

import com.example.tornet.configuration.UserDetailsServiceImpl;
import com.example.tornet.exception.CustomerNotFoundException;

import com.example.tornet.model.Customer;
import com.example.tornet.model.Role;
import com.example.tornet.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


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
    private final CartService cartService;
    private final EmailSenderService emailSenderService;

    @GetMapping("/")
    public String index(Model model) {
        User user = getCurrentUser();
        String email = user.getUsername();
        Customer customer = customerService.findCustomerByEmail(email);
        model.addAttribute("email", email);
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("Admin", isAdmin(user));
        model.addAttribute("phone", customer.getPhone());
        model.addAttribute( "firstname", customer.getFirstname());
        model.addAttribute( "lastname", customer.getLastname());


        return "redirect:/login";
    }

    @GetMapping("/unauthorized")
    public String unauthorized(Model model) {
        User user = getCurrentUser();
        model.addAttribute("login", user.getUsername());
        return "unauthorized";
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
            log.info("Checking role: {}", auth.getAuthority());
            if ("ROLE_ADMIN".equals(auth.getAuthority())) {
                log.info("Admin role found for user: {}", user.getUsername());
                return true;
            }
        }
        log.info("No admin role found for user: {}", user.getUsername());
        return false;
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String register() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@RequestParam String email,
                               @RequestParam String password,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String address,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String state,
                               @RequestParam(required = false) String country,
                               @RequestParam(required = false) String zip,
                               Model model) {
        String passHash = passwordEncoder.encode(password);

        if (!customerService.addUser(email, passHash, Role.Users, "", "", phone, address, city, state, country, zip)) {
            model.addAttribute("exists", true);
            model.addAttribute("email", email);
            log.info("add user");
            return "registration";
        }

        return "redirect:/Main";
    }
}









