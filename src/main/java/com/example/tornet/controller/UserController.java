package com.example.tornet.controller;


import com.example.tornet.model.Category;
import com.example.tornet.model.Customer;
import com.example.tornet.model.Product;
import com.example.tornet.model.Role;
import com.example.tornet.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@Slf4j
@AllArgsConstructor
public class UserController {

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String index(Model model, Pageable pageable) {
        User user = getCurrentUser();
        if (user == null) {
            log.info("No authenticated user found. Redirecting to login page.");
            return "redirect:/login";
        }

        log.info("Authenticated user: {}", user.getUsername());

        if (isAdmin(user)) {
            log.info("User is admin. Redirecting to admin page.");
            return "redirect:/Admin";
        }

        String email = user.getUsername();
        Customer customer = customerService.findCustomerByEmail(email);

        if (customer == null) {
            log.error("No customer found for email: {}", email);
            return "redirect:/errorPage";
        }

        pageable = PageRequest.of(pageable.getPageNumber(), 6);

        Page<Product> productPage = productService.getProductsPage(pageable);
        List<Category> categoryList = categoryService.getAllCategories();

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", categoryList);
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentUser", customer);

        return "Main";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email,
                               @RequestParam String password,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String street,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String state,
                               @RequestParam(required = false) String zip,
                               Model model) {
        if (email.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "Email and password are required");
            return "register";
        }

        if (customerService.findCustomerByEmail(email) != null) {
            model.addAttribute("exists", true);
            model.addAttribute("email", email);
            log.info("User is already registered. Redirecting to login page.");
            return "register";
        }

        String passHash = passwordEncoder.encode(password);

        log.info("Attempting to create user with email: {}", email);
        log.debug("Password hash: {}", passHash);
        log.debug("Phone: {}, Street: {}, City: {}, State: {}, Zip: {}", phone, street, city, state, zip);

        try {
            if (!customerService.addUser(email, passHash, Role.Users, "", "", phone, street, city, state, zip)) {
                model.addAttribute("error", "Registration failed. Please try again.");
                log.error("Failed to create user with email: {}", email);
                return "register";
            }
        } catch (Exception e) {
            log.error("Exception during user registration: ", e);
            model.addAttribute("error", "An error occurred during registration. Please try again.");
            return "register";
        }

        log.info("Created new user: {}", email);
        return "redirect:/login";
    }

    @GetMapping("/unauthorized")
    public String unauthorized(Model model) {
        User user = getCurrentUser();
        model.addAttribute("login", user != null ? user.getUsername() : "Guest");
        return "unauthorized";
    }

    @GetMapping("/errorPage")
    public String errorPage(Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        return "ErrorPage";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }

    private boolean isAdmin(User user) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_Admin".equals(auth.getAuthority()));
        log.info("Is user {} admin: {}", user.getUsername(), isAdmin);
        return isAdmin;
    }
}


























