package com.example.tornet.service;

import com.example.tornet.exception.CustomerNotFoundException;
import com.example.tornet.model.Customer;
import com.example.tornet.reposotory.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Customer> getCustomerRepository() {
        return customerRepository.findAll();
    }

    @Transactional
    public boolean save(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            log.error("Email already exists during user registration.");
            return false;
        }

        // Хэширование пароля перед сохранением
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);

        customerRepository.save(customer);

        return true;
    }
    public Customer findCustomerByEmail(String email) {
        log.debug("Searching for customer with email: {}", email);
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found with email: " + email);
        }
        return customer;
    }


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
