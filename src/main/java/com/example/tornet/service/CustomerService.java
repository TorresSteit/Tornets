package com.example.tornet.service;

import com.example.tornet.configuration.UserDetailsServiceImpl;
import com.example.tornet.exception.CustomerNotFoundException;
import com.example.tornet.model.Customer;
import com.example.tornet.model.Role;
import com.example.tornet.reposotory.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public boolean save(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            log.error("Email already exists during user registration.");
            return false;
        }
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);

        customerRepository.save(customer);

        return true;
    }

    public Customer findCustomerByEmail(String email) {
        log.debug("Searching for customer with email: {}", email);
        return customerRepository.findByEmail(email);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    public Customer findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }
}



